package com.santeut.ui.party

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.santeut.ui.community.party.CustomDatePickerDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DatePickerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle?,
) : ViewModel() {
    val customDatePickerDialogState: MutableState<CustomDatePickerDialogState?> =
        mutableStateOf(null)

    init {
        customDatePickerDialogState.value = CustomDatePickerDialogState(
            onClickConfirm = { yyyyMMdd ->
                customDatePickerDialogState.value = customDatePickerDialogState.value?.copy(
                    isShowDialog = false,
                    selectedDate = yyyyMMdd
                )
            },
            onClickCancel = {
                customDatePickerDialogState.value = customDatePickerDialogState.value?.copy(
                    isShowDialog = false
                )
            }
        )
    }

    fun showDatePickerDialog() {
        customDatePickerDialogState.value =
            customDatePickerDialogState.value?.copy(isShowDialog = true)
    }
}