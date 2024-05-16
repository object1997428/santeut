package com.santeut.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.santeut.ui.party.PartyViewModel
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyScheduleScreen(partyViewModel: PartyViewModel = hiltViewModel()) {
    val myScheduleList by partyViewModel.myScheduleList.observeAsState(emptyList())
    val activeDates = myScheduleList.map { LocalDate.parse(it) }

    val year = 2024
    val month = 5

    LaunchedEffect (key1 = year, key2 = month){
        partyViewModel.getMyScheduleList(year, month)
    }

    val pagerState = rememberPagerState()

    HorizontalPager(
        count = 12, // 예: 현재 연도의 12개월
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { pageIndex ->
        val yearMonth = YearMonth.now().withMonth(pageIndex + 1)
        MonthView(yearMonth, activeDates)
    }
}

@Composable
fun MonthView(yearMonth: YearMonth, activeDates: List<LocalDate>) {
    val daysInMonth = remember(yearMonth) {
        List(yearMonth.lengthOfMonth()) { day ->
            yearMonth.atDay(day + 1)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(daysInMonth) { date ->
            DateCell(date, activeDates.contains(date))
        }
    }
}

@Composable
fun DateCell(date: LocalDate, hasEvent: Boolean) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = date.dayOfMonth.toString())
        if (hasEvent) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "Event marker",
                tint = Color.Green,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
