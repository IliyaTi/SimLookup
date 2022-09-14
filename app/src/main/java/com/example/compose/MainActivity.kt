package com.example.compose

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.compose.ui.components.ListItemCard
import com.example.compose.ui.components.PrefixDropDown
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.ui.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val prefix = remember { mutableStateOf("any") }
                    val avail = remember { mutableStateOf("") }
                    val res = remember { mutableStateOf("") }
                    val showSpinner = remember { mutableStateOf(false) }

                    var results = arrayListOf<NumberItem>()

                    val wholeSet = ConstraintSet {
                        val topPart = createRefFor("top")
                        val belowPart = createRefFor("below")
                        val progressbar = createRefFor("progressbar")

                        constrain(topPart){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }

                        constrain(belowPart){
                            top.linkTo(topPart.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }

                        constrain(progressbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                    }

                    val topSet = ConstraintSet {
                        val title = createRefFor("title")
                        val prefixField = createRefFor("prefix")
                        val textField = createRefFor("field")
                        val button = createRefFor("button")
                        val lazyCol = createRefFor("lazyCol")


                        constrain(title) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }

                        constrain(prefixField){
                            start.linkTo(parent.start)
                            top.linkTo(title.bottom)
                        }

                        constrain(textField) {
                            start.linkTo(prefixField.end)
                            top.linkTo(title.bottom)
                            end.linkTo(parent.end)
                        }

                        constrain(button) {
                            start.linkTo(parent.start)
                            top.linkTo(textField.bottom)
                            end.linkTo(parent.end)
                        }

                        constrain(lazyCol) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }

                    }

                    ConstraintLayout(constraintSet = wholeSet) {

                        Box(modifier = Modifier.layoutId("top")){

                            ConstraintLayout(constraintSet = topSet) {
                                Text(text = "Please enter your desired number", fontSize = 20.sp, modifier = Modifier
                                    .layoutId("title")
                                    .padding(top = 6.dp, bottom = 12.dp))

                                PrefixDropDown(modifier = Modifier
                                    .layoutId("prefix")
                                    .padding(start = 10.dp), text = prefix.value){
                                    prefix.value = it
                                }

                                TextField (
                                    modifier = Modifier.layoutId("field"),
                                    value = avail.value,
                                    onValueChange = {avail.value = it; res.value = ""},
                                )
                                Button(
                                    onClick = {
                                        showSpinner.value = true
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                results = fetchExistence(prefix.value, avail.value)
                                                showSpinner.value = false
                                            } catch (e: IllegalStateException){
                                                showSpinner.value = false
                                                results = arrayListOf()
                                                showToast(applicationContext, "No records found!")
                                            } catch (e: Exception){
                                                Log.e("mainCompose", "Errorrrrr!!")
                                                showSpinner.value = false
                                                showToast(applicationContext, "an unexpected error occurred!")
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .layoutId("button")
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Text(text = "fetch")
                                }
                            }
                        }

///////////////////////////////////////////////////////////////////////////////////////////////////////

                        Box (
                            modifier = Modifier
                                .layoutId("below")
                                .border(width = 1.dp, color = Color.Gray, shape = RectangleShape)
                        ) {
                            LazyColumn() {
                                items(results.size) {
                                    ListItemCard(item = results[it])
                                }
                            }
                        }

                        if (showSpinner.value) {
                            Box(modifier = Modifier
                                .layoutId("progressbar")
                                .fillMaxSize()
                                .background(Color(0, 0, 0, 99))){
                                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(modifier = Modifier
                                        .fillMaxSize()
                                        .padding(80.dp)
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTheme {
        Greeting("Android")
    }
}



//@Composable
//fun DropDownMenu(modifier: Modifier = Modifier){
//    var expanded by remember { mutableStateOf(false) }
//
//    val list = listOf("any", "0910", "0911", "0912", "0913", "0914", "0915", "0916", "0917", "0918", "0919", "0990", "0991", "0992", "0993" ,"0996", "0905", "0901")
//
//    var selectedPrefix by remember { mutableStateOf(list[0]) }
//    var textFieldSize by remember { mutableStateOf(Size.Zero) }
//
//    val icon = if (expanded) {
//        Icons.Filled.KeyboardArrowUp
//    } else {
//        Icons.Filled.KeyboardArrowDown
//    }
//
//    Column(modifier = Modifier.padding(10.dp)) {
//        OutlinedTextField(
//            value = selectedPrefix,
//            onValueChange = { selectedPrefix = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .onGloballyPositioned { coordinates ->
//                    // this value is used to assign to the DropDown the same width
//                    textFieldSize = coordinates.size.toSize()
//                },
//            label = { Text(text = "Label") },
//            trailingIcon = {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = "ContentDescription",
//                    modifier = Modifier.clickable { expanded = !expanded }
//                )
//            }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.width(with(LocalDensity.current){ textFieldSize.width.toDp() })
//        ) {
//            list.forEach { prefix ->
//                DropdownMenuItem(onClick = {
//                    selectedPrefix = prefix
//                    expanded = false
//                }) {
//                    Text(text = prefix)
//                }
//            }
//        }
//
//    }
//
//}

