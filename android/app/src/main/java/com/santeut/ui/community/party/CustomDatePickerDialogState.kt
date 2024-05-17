package com.santeut.ui.community.party

data class CustomDatePickerDialogState(
    var selectedDate: String? = null,
    var isShowDialog: Boolean = false,
    val onClickConfirm: (yyyyMMdd: String) -> Unit = {},
    val onClickCancel: () -> Unit = {},
)
