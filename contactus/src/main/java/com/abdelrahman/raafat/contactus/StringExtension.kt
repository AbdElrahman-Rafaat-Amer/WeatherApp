package com.abdelrahman.raafat.contactus

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun String.openLink(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
    context.startActivity(intent)
}

fun String.openGmail(context: Context) {
    val recipientEmail: String = context.getString(R.string.email)
    val emailSubject: String = context.getString(R.string.email_title)
    val emailIntent = Intent(Intent.ACTION_SENDTO)

    emailIntent.setType("message/rfc822")

    val uri = Uri.parse("mailto:" + recipientEmail + "?subject=" + Uri.encode(emailSubject))
    emailIntent.setData(uri)

    try {
        context.startActivity(emailIntent)
    } catch (exception: ActivityNotFoundException) {
       //Show snack bar
    }
}