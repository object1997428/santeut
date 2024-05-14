package com.santeut.ui.mypage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.ui.party.PartyViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyHikingScreen(
    partyViewModel: PartyViewModel = hiltViewModel()
) {

    val myRecordList by partyViewModel.myRecordList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        partyViewModel.getMyRecordList()
    }

    Scaffold {
        if (myRecordList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "산행 기록이 없습니다.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn() {
                items(myRecordList) { record ->
                    MyRecord(record)
                }
            }
        }
    }
}

@Composable
fun MyRecord(record: MyRecordResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(4.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Row {
                Text(text = record.partyName, style = MaterialTheme.typography.headlineSmall)
                Text(text = record.guildName, style = MaterialTheme.typography.bodyMedium)
            }

            Row {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = "산 정보",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                )
                Text(
                    text = record.mountainName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "등산 날짜",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                )
                Text(
                    text = record.schedule,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "이동거리 ${record.distance}km",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "이동시간 ${record.duration}", // 이동 시간을 보여줍니다.
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "최고 고도 ${record.height}m", // 최고 고도를 보여줍니다.
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
