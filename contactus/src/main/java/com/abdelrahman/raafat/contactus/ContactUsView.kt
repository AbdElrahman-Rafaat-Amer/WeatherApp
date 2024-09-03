package com.abdelrahman.raafat.contactus

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdelrahman.raafat.contactus.ui.theme.WeatherAppTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun ContactUsView() {

    val context = LocalContext.current
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.contact_us
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 50.dp, bottom = 15.dp)
    ) {
        LottieAnimation(
            composition = preloaderLottieComposition,
            progress = { preloaderProgress },
            modifier = Modifier
                .size(200.dp)
        )


        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.get_in_touch),
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold

        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = stringResource(id = R.string.get_in_touch_desc),
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        val socialItems = listOf(
            SocialItem(
                stringResource(id = R.string.web_site),
                R.drawable.ic_telegram,
                stringResource(id = R.string.website_link)
            ),
            SocialItem(
                stringResource(id = R.string.telegram_user_name),
                R.drawable.ic_telegram,
                stringResource(id = R.string.telegram_link)
            ),
            SocialItem(
                stringResource(id = R.string.gmail_name),
                R.drawable.ic_gmail,
                isGmail = true
            ),
        )
        LazyColumn(modifier = Modifier.fillMaxWidth(0.9f)) {
            items(socialItems.size) { index ->
                Spacer(modifier = Modifier.height(20.dp))
                SocialRow(socialItems[index])
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(id = R.string.social_media),
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(25.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            val icons = listOf(
                SocialMediaIconItem(
                    R.drawable.ic_facebook,
                    stringResource(id = R.string.facebook_link)
                ),
                SocialMediaIconItem(
                    R.drawable.ic_instagram,
                    stringResource(id = R.string.instagram_link)
                ),
                SocialMediaIconItem(
                    R.drawable.ic_twitter,
                    stringResource(id = R.string.twitter_link)
                ),
                SocialMediaIconItem(
                    R.drawable.ic_linkedin,
                    stringResource(id = R.string.linkedin_link)
                ),
                SocialMediaIconItem(
                    R.drawable.ic_medium,
                    stringResource(id = R.string.medium_link)
                )
            )

            icons.forEach { item ->
                SocialMediaIcon(iconRes = item.imageResources) {
                    item.link.openLink(context = context)
                }
            }
        }
    }
}


@Preview
@Composable
fun ContactUsViewPreview() {
    WeatherAppTheme {
        ContactUsView()
    }

}