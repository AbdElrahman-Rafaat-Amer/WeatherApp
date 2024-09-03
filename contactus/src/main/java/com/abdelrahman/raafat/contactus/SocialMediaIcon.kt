package com.abdelrahman.raafat.contactus

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SocialMediaIcon(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
    )
}