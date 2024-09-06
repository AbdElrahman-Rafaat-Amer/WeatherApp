package com.abdelrahman.raafat.contactus

import androidx.annotation.DrawableRes

data class SocialMediaIconItem(
    @DrawableRes
    var imageResources: Int,
    var link: String = "",
)
