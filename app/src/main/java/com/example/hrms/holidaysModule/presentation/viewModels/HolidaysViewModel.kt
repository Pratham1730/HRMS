package com.example.hrms.holidaysModule.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrms.common.ApiResultState
import com.example.hrms.holidaysModule.domain.model.response.HolidaysDomainItem
import com.example.hrms.holidaysModule.domain.model.response.PublicHolidaysDomainResponse
import com.example.hrms.holidaysModule.domain.useCases.GetHolidaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HolidaysViewModel @Inject constructor(
    private val getHolidaysUseCase: GetHolidaysUseCase
) : ViewModel() {

    private val _holidaysResponse = MutableLiveData<ApiResultState<PublicHolidaysDomainResponse>>()
    val holidaysResponse: LiveData<ApiResultState<PublicHolidaysDomainResponse>> get() = _holidaysResponse

    private val _holidaysList = MutableLiveData<List<HolidaysDomainItem>>()
    val holidaysList: LiveData<List<HolidaysDomainItem>> get() = _holidaysList

    fun loadHolidays(method: String, companyId: Int) {
        _holidaysResponse.value = ApiResultState.Loading

        viewModelScope.launch {
            try {
                val result = getHolidaysUseCase.invokeGetHolidays(method, companyId)
                _holidaysResponse.value = ApiResultState.Success(result)

                val items = result.holidays?.filterNotNull() ?: emptyList()
                _holidaysList.value = items
            } catch (e: Exception) {
                _holidaysResponse.value = ApiResultState.ApiError(e.message ?: "Unknown error")
                _holidaysList.value = emptyList()
            }
        }
    }
}
