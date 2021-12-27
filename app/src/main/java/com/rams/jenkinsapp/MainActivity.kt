package com.rams.jenkinsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.rams.jenkinsapp.ui.theme.JenkinsAppTheme

class MainActivity : ComponentActivity() {
    var result = mutableStateOf(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JenkinsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.onPrimary) {
                    CalculatorView(result)
                }
            }
        }
    }
}

@Composable
fun CalculatorView(result: MutableState<Int>) {
   /* Image(painterResource(id = R.drawable.banner_image_new),
        contentDescription = "haaaa",
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )*/
    Row() {
        Text(text = "Result "+result)
        Button(onClick = {
            result.value= Calculator.addition(3,4)
        },content = { Text(text = "Addition")})
        Button(onClick = {
            result.value= Calculator.multiplication(3,4)
        },content = { Text(text = "Multi")})
        Button(onClick = {
            result.value= Calculator.subtraction(3,4)
        },content = { Text(text = "Sub")})
    }
}



/*@Composable
fun GreetingText(activity: Activity) {
    Button(onClick = {
        activity.startActivity(Intent(activity,InAppUpdateActivity::class.java))
    }){
        Text(text = "Wellcommme")
    }
}*/

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JenkinsAppTheme {
        //Greeting()
    }
}