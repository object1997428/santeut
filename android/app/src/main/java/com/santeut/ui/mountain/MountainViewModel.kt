package com.santeut.ui.mountain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import com.santeut.data.model.response.MountainResponse
import com.santeut.domain.usecase.MountainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(
    private val mountainUseCase: MountainUseCase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _mountains = MutableLiveData<List<MountainResponse>>()
    val mountains: LiveData<List<MountainResponse>> = _mountains

    fun searchMountain(name: String, region: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val data = mountainUseCase.searchMountain(name, region)
//                _mountains.postValue(data.result)

                mountainUseCase.searchMountain(name, region).let{ data ->
                    Log.d("", data.result.get(0).mountainName)
                    _mountains.postValue(data.result)
                }

            } catch (e: Exception) {
                _error.postValue("산 목록 조회 실패: ${e.message}")
            }
        }
    }
}