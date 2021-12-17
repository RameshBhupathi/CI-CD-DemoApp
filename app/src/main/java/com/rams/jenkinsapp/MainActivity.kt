package com.rams.jenkinsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.rams.jenkinsapp.ui.theme.JenkinsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JenkinsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.onPrimary) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Image(painterResource(id = R.drawable.banner_image_new),
        contentDescription = "haaaa",
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    )
    GreetingText()
}

@Composable
fun GreetingText() {
    Text(text = "WellCome to CI-CD")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JenkinsAppTheme {
        Greeting()
    }
}