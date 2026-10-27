package com.lcn.smssync

import android.app.IntentService
import android.content.Intent

class HeadlessSmsSendService : IntentService("HeadlessSmsSendService") {
    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {}
}
