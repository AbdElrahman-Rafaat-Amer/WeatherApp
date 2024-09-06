package com.abdelrahman.raafat.contactus

import androidx.annotation.DrawableRes

data class SocialItem(
    var name: String,
    @DrawableRes
    var imageResources: Int,
    var link: String = "",
    var isGmail: Boolean = false
)
