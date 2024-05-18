package com.santeut.ui.party

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santeut.data.model.request.CreatePartyRequest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreatePartyScreen(
    navController: NavController,
    partyViewModel: PartyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var partyName by remember { mutableStateOf("") }
    var maxPeople by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }

    val selectedCourse = listOf<Int>()

    val partyCreationSuccess by partyViewModel.partyCreationSuccess.observeAsState()

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            calendar.set(year, month, day)
            TimePickerDialog(context, { _, hour: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                schedule =
                    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = "소모임 이름",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        TextField(
            value = partyName,
            onValueChange = { partyName = it },
            label = { },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "최대 인원",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        TextField(
            value = maxPeople,
            onValueChange = { maxPeople = it },
            label = { },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "모임 장소",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        TextField(
            value = place,
            onValueChange = { place = it },
            label = { },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "일정",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        TextField(
            value = schedule,
            onValueChange = {},
            label = { },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Select Date")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "산 선택하기",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        SelectedMountainBar(navController)

        Button(onClick = {
            partyViewModel.createParty(
                CreatePartyRequest(
                    schedule,
                    partyName,
                    1, // mountainId,
                    "승학산", //mountainName,
                    maxPeople.toInt(),
                    null, // guildId,
                    place,
                    selectedCourse
                )
            )

            if (partyCreationSuccess == true) {
                // 로직 추가
            }
        }) {
            Text("소모임 생성", fontSize = 18.sp)
        }
    }
}

@Composable
fun SelectedMountainBar(navController: NavController) {
    Text(text = "산 선택하기")
}
