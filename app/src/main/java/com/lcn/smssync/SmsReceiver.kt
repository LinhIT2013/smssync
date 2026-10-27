package com.lcn.smssync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Thay đổi hành động lắng nghe thụ động tại đây
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val currentUser = FirebaseAuth.getInstance().currentUser
            
            if (currentUser != null) {
                val db = FirebaseFirestore.getInstance()
                val userEmail = currentUser.email ?: "unknown"

                for (sms in messages) {
                    val smsData = hashMapOf(
                        "sender" to sms.displayOriginatingAddress,
                        "body" to sms.displayMessageBody,
                        "timestamp" to sms.timestampMillis,
                        "deviceName" to Build.MANUFACTURER + " " + Build.MODEL
                    )

                    db.collection("lcn_sync_users").document(userEmail)
                        .collection("sms_logs")
                        .add(smsData)
                }
            }
        }
    }
}
