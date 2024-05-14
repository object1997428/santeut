package com.santeut.ui.party

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NotStarted
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.ui.community.party.PartySearchBar

@Composable
fun MyPartyListScreen(
    partyViewModel: PartyViewModel = hiltViewModel()
) {
    val myPartyList by partyViewModel.myPartyList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        partyViewModel.getMyPartyList(date = null, includeEnd = false, page = null, size = null)
    }

    Column {
        PartySearchBar(
            partyViewModel,
            onSearchTextChanged = {},
            onClickSearch = {},
            onClickFilter = {}
        )

        if (myPartyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "가입된 소모임이 없습니다.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                items(myPartyList) { party ->
                    MyPartyCard(party)
                }
            }
        }

    }
}

@Composable
fun MyPartyCard(party: MyPartyResponse) {
    Card {
        Column(Modifier.fillMaxWidth()) {
            Row {
                Text(text = party.partyName)
                Text(text = party.guildName)
                if (party.status == "P") {
                    Icon(
                        imageVector = Icons.Outlined.Sensors,
                        contentDescription = "진행 중"
                    )
                }
            }
            Row {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "일정"
                )
                Text(text = party.schedule)
            }
            Row {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "위치"
                )
                Text(text = party.place)
            }
            Row {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardDoubleArrowUp,
                        contentDescription = "산"
                    )
                    Text(text = party.mountainName)
                }
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "인원 수"
                    )
                    Text(text = "${party.curPeople} / ${party.maxPeople} 명")
                }
                Button(onClick = {/* 소모임 시작 버튼 로직 추가 */ }) {
                    Icon(
                        imageVector = Icons.Outlined.NotStarted,
                        contentDescription = "시작 버튼"
                    )
                }
            }
        }
    }
}
