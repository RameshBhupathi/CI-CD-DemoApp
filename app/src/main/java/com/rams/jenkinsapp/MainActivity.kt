package com.rams.jenkinsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                    //Greeting()
                }
            }
        }
    }
}/*

@Composable
fun Greeting() {
    Image(painterResource(id = R.drawable.banner_image_new),
        contentDescription = "haaaa",
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
    GreetingText(this)
}

@Composable
fun GreetingText(activity: Activity) {
    Button(onClick = {
        activity.startActivity(Intent(activity,InAppUpdateActivity::class.java))
    }){
        Text(text = "Wellcommme")
    }
}
*/
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JenkinsAppTheme {
        //Greeting()
    }
}