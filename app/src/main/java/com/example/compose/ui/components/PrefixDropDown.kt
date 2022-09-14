package com.example.compose.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun PrefixDropDown(modifier: Modifier, text: String, doSomething: (String) -> Unit){
    val list = listOf("any", "0910", "0911", "0912", "0913", "0914", "0915", "0916", "0917", "0918",
        "0919", "0990", "0991", "0992", "0993" ,"0996", "0900", "0901", "0902", "0903", "0904", "0905", "0930",
        "0932", "0933", "0935", "0936", "0937", "0938", "0939", "0920", "0921", "0922", "0923", "0999")
    var expanded by remember {
        mutableStateOf(false)
    }
    Button(onClick = { expanded = !expanded }, modifier = modifier) {
        Text(text = text)
        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        list.forEach { label ->
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    doSomething(label)
                }
            ) {
                Text(text = label)
            }
        }
    }
}