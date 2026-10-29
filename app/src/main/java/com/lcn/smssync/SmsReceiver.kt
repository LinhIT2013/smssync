package com.lcn.smssync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import com.google.firebase.firestore.FirebaseFirestore

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            
            // Đọc thông tin tài khoản thủ công đã lưu trong Cài đặt của ứng dụng
            val sharedPreferences = context.getSharedPreferences("LCN_Storage", Context.MODE_PRIVATE)
            val savedUser = sharedPreferences.getString("auth_user", null)
            val isSyncEnabled = sharedPreferences.getBoolean("sync_enabled", false)

            // Chỉ đồng bộ nếu người dùng đã đăng nhập thủ công và bật tính năng đồng bộ
            if (isSyncEnabled && !savedUser.isNullOrEmpty()) {
                val db = FirebaseFirestore.getInstance()

                for (sms in messages) {
                    val smsData = hashMapOf(
                        "sender" to (sms.displayOriginatingAddress ?: "Ẩn danh"),
                        "body" to (sms.displayMessageBody ?: ""),
                        "timestamp" to sms.timestampMillis,
                        "deviceName" to "${Build.MANUFACTURER} ${Build.MODEL}"
                    )

                    // Đẩy lên Firestore theo cụm tài khoản thủ công của công ty LCN
                    db.collection("lcn_manual_users").document(savedUser)
                        .collection("sms_logs")
                        .add(smsData)
                }
            }
        }
    }
}
