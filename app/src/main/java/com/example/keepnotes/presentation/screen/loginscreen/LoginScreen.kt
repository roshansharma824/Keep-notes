package com.example.keepnotes.presentation.screen.loginscreen

import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.keepnotes.R
import com.example.keepnotes.data.auth.SignInState
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_0dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.ui.theme.*
import com.example.keepnotes.ui.theme.TEXT_SIZE_24sp

@Composable
fun LoginScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.note_logo).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Capture anything",
                    fontSize = TEXT_SIZE_24sp,
                    color = GrayTextColor,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 25.dp)
                )
                Spacer(modifier = Modifier.height(DIMENS_10dp))
                Text(
                    text = "Make lists, takes photos, speak your mind - whatever works for you, works in keep.",
                    textAlign = TextAlign.Center,
                    color = GrayTextColor,
                    fontSize = TEXT_SIZE_12sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(DIMENS_32dp))

                Button(
                    onClick = { onSignInClick() },
                    colors =  ButtonDefaults.buttonColors(containerColor = ButtonBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = TEXT_SIZE_18sp,
                            fontWeight = FontWeight.Normal,
//                            modifier = Modifier.padding(10.dp),
                        )
                        Spacer(modifier = Modifier.size(DIMENS_16dp))
                        Icon(
                            painter = painterResource(id = R.drawable.logo_google),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        }
    }
}