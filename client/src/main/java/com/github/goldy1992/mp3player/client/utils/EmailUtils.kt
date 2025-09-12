package com.github.goldy1992.mp3player.client.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.Email
import com.github.goldy1992.mp3player.client.utils.ContextUtils.getActivity

object EmailUtils {
    const val LOG_TAG = "EmailUtils"
    private const val email = "goldy131992@gmail.com"
    private const val email_title_template = "MP3 Player Bug Report - Please Give a Bug Description"

    fun sendBugReportEmail(context : Context) {
        val appCompatActivity = context.getActivity()
        if (appCompatActivity != null) {
            val bugReportEmail = Email.BUG_REPORT
            sendEmail(bugReportEmail, context)
        }
    }

    fun sendFeatureRequestEmail(context : Context) {
        val appCompatActivity = context.getActivity()
        if (appCompatActivity != null) {
            val featureRequestEmail = Email.FEATURE_REQUEST
            sendEmail(featureRequestEmail, context)
        }
    }

    fun sendFeedbackEmail(context : Context) {
        val appCompatActivity = context.getActivity()
        if (appCompatActivity != null) {
            val featureRequestEmail = Email.FEEDBACK
            sendEmail(featureRequestEmail, context)
        }
    }

    private fun sendEmail(email: Email, context: Context) {
        Log.v(
            LOG_TAG,
            "sendEmail() invoked with subject: ${email.subject}, toAddresses: ${email.toAddresses} and ccAddresses: ${email.ccAddresses}"
        )
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                val addresses: Array<String> = email.toAddresses
                putExtra(Intent.EXTRA_SUBJECT, email.subject)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, addresses)
            }
            try {
                val sendEmailTitle = context.getString(R.string.send_email)
                context.startActivity(Intent.createChooser(emailIntent, sendEmailTitle))
            } catch (ex : ActivityNotFoundException) {
                Log.e(LOG_TAG, "No email client found on device")
                Toast.makeText(
                    context,
                    "Could not find an email application on your device",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
}