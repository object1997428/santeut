package com.santeut.ui.community.party

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.data.model.response.PartyResponse
import com.santeut.designsystem.theme.DarkGreen
import com.santeut.designsystem.theme.Green
import com.santeut.ui.party.PartyViewModel

@Composable
fun JoinPartyScreen(
    guildId: Int?,
    partyViewModel: PartyViewModel = hiltViewModel()
) {

    val partyList by partyViewModel.partyList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        partyViewModel.getPartyList(guildId = null, name = null, start = null, end = null)
    }

    Scaffold(
        topBar = {
            PartySearchBar(
                partyViewModel,
                onSearchTextChanged = {},
                onClickSearch = {},
                onClickFilter = {}
            )
        }, content = { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (partyList.isEmpty()) {
                    Text(
                        text = "소모임이 없습니다",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        items(partyList) { party ->
                            PartyCard(party, partyViewModel)
                        }
                    }
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyCard(party: PartyResponse, partyViewModel: PartyViewModel) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Card (
        modifier = Modifier
            .clickable(onClick = { showBottomSheet = true })
            .fillMaxWidth()
            .height(160.dp)  // 카드 높이 조정
            .padding(8.dp), // 카드 주변 여백
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Column (
          modifier = Modifier
              .padding(16.dp)
        ) {
            Text(
                text = party.partyName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
            // 일정
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "일정",
                    tint = Green,
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = party.schedule)
            }
            // 모임장소
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "위치",
                    tint = Green,
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = party.place)
            }
            // 산, 인원수
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(0.dp, 4.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardDoubleArrowUp,
                        contentDescription = "산",
                        tint = Green,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                    )
                    Text(text = party.mountainName)
                }
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "인원 수",
                        tint = Green,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                    )
                    Text(text = "${party.curPeople} / ${party.maxPeople} 명")
                }
            }
        }
    }
}


@Composable
fun PartySearchBar(
    partyViewModel: PartyViewModel,
    onSearchTextChanged: (String) -> Unit,
    onClickSearch: () -> Unit,
    onClickFilter: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 8.dp, bottom = 8.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Color(0xff666E7A)),
            value = name,
            onValueChange = { text ->
                onSearchTextChanged(text)
            },
            placeholder = {
                androidx.compose.material.Text(
                    text = "어느 소모임을 찾으시나요?",
                    color = Color(0xff666E7A)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done // 완료 액션 지정
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFD6D8DB),
                unfocusedContainerColor = Color(0xFFEFEFF0),
                focusedBorderColor = Color(0xFFD6D8DB),
                focusedContainerColor = Color(0xFFEFEFF0),
            ),
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color(0xff33363F),
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            partyViewModel.getPartyList(null, name, null, null)
                        }
                )
            }
        )
        Spacer(modifier = Modifier.width(15.dp))
        androidx.compose.material3.Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "필터",
            tint = Color(0xff335C49),
            modifier = Modifier.size(30.dp)
        )
    }
}
