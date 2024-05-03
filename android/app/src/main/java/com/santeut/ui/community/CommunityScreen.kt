package com.santeut.ui.community

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santeut.designsystem.theme.SanteutTheme
import com.santeut.ui.navigation.bottom.BottomNavBar

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun CommunityScreen() {
    Scaffold(
        topBar = { ToolBar() },
        content = { Tips() },
    )
}


@Composable
fun ToolBar() {
    TopAppBar(
        title = { Text(text = "커뮤니티") },
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Message,
                    contentDescription = "Message"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}

@Composable
fun TabRow() {

}

@Composable
fun Tips() {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.Green)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(10) {
            TipDetail()
        }
    }
}

@Composable
fun TipDetail() {
    Column {
        Row {
            Text(text = "Title ")
            Text(text = "Writer")
        }
        Text(text = "내용")
    }
}

@Composable
@Preview(device = Devices.PIXEL_2)
fun Preview() {
    SanteutTheme {
        CommunityScreen()
    }
}