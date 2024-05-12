package com.santeut.ui.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.ui.party.PartyViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MyScheduleScreen(partyViewModel: PartyViewModel = hiltViewModel()) {
    val myScheduleList by partyViewModel.myScheduleList.observeAsState(emptyList())

    // 서버로부터 받은 날짜에 이벤트가 있는지 확인할 리스트
    val activeDates = myScheduleList.map { LocalDate.parse(it) }

    // 날짜 범위를 설정하여 달력을 생성
    val startDate = YearMonth.now().atDay(1)
    val endDate = YearMonth.now().atEndOfMonth()

    val dateList = remember(startDate, endDate) {
        generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDate) }
            .map { date -> ScheduleDate(date, activeDates.contains(date)) }
            .toList()
    }

    CalendarView(dateList)
}

@Composable
fun CalendarView(dateList: List<ScheduleDate>) {
    LazyColumn {
        items(dateList) { scheduleDate ->
            DateRow(scheduleDate)
        }
    }
}

@Composable
fun DateRow(scheduleDate: ScheduleDate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = scheduleDate.date.toString(), style = MaterialTheme.typography.body1)
        if (scheduleDate.hasEvent) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "Event marker",
                tint = Color.Green,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class ScheduleDate(
    val date: LocalDate,
    val hasEvent: Boolean
)
