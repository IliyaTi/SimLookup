package com.example.compose.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.NumberItem

@Composable
fun ListItemCard(
    modifier: Modifier = Modifier,
    item: NumberItem
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = item.phoneNo)
            Row(modifier = Modifier){
                Text(text = item.price)
                Text(text = "IRT", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}