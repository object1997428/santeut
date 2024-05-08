package com.santeut.ui.community.guild

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santeut.R

@Composable
fun JoinGuildScreen() {

    LazyColumn {
        items(10) {
            GuildCard()
        }
    }
}

@Composable
fun GuildCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Box(
            modifier = Modifier.height(150.dp),
        ) {
            Row {

                Image(
                    painter = painterResource(R.drawable.dummy),
                    contentDescription = "Dummy",
                    contentScale = ContentScale.FillHeight,
                )

                Column {
                    Text(text = "동호회 이름")
                    Text(text = "88명")
                    Text(text = "부산")
                }
            }
        }
    }
}