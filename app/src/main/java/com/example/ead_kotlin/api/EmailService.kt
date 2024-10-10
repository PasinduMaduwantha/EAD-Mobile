package com.example.ead_kotlin.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

object EmailService {
    fun sendEmailNotification(context: Context, recipient: String, subject: String, body: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        println("email data"+recipient+body)
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, body)

        val TAG = "Email Intent"


        try {
            if (mIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(Intent.createChooser(mIntent, "Send email using..."))
            } else {
                Log.e(TAG, "No email client available")
                // You might want to show a toast or dialog to inform the user
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending email", e)
            // Handle the exception (e.g., show an error message to the user)
        }

    }
}
