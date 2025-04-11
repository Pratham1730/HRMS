package com.example.hrms.leaveModule.displayLeaveModule.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrms.common.ApiResultState
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDataDomainItem
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDeleteDomainResponse
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveListDomainResponse
import com.example.hrms.leaveModule.displayLeaveModule.domain.useCases.GetLeaveListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveListViewModel @Inject constructor(
    private val getLeaveListUseCase: GetLeaveListUseCase
) : ViewModel() {

    private val _leaveListResponse = MutableLiveData<ApiResultState<LeaveListDomainResponse>>()
    val leaveListResponse: LiveData<ApiResultState<LeaveListDomainResponse>> get() = _leaveListResponse

    private val _leaveListItems = MutableLiveData<List<LeaveDataDomainItem>>()
    val leaveListItems: LiveData<List<LeaveDataDomainItem>> get() = _leaveListItems

    private val _deleteLeaveResponse = MutableLiveData<ApiResultState<LeaveDeleteDomainResponse>>()
    val deleteLeaveResponse: LiveData<ApiResultState<LeaveDeleteDomainResponse>> get() = _deleteLeaveResponse


    fun loadLeaveList(method: String, userId: Int) {
        _leaveListResponse.value = ApiResultState.Loading

        viewModelScope.launch {
            try {
                val result = getLeaveListUseCase.invokeLeaveList(method, userId)
                _leaveListResponse.value = ApiResultState.Success(result)

                val listItems = result.leaveData?.filterNotNull() ?: emptyList()
                _leaveListItems.value = listItems
            } catch (e: Exception) {
                _leaveListResponse.value = ApiResultState.ApiError(e.message ?: "Unknown error")
                _leaveListItems.value = emptyList()
            }
        }
    }


    fun deleteLeave(
        delete: String,
        leaveId: Int,
        userId: Int,
        companyId: Int
    ) {
        _deleteLeaveResponse.value = ApiResultState.Loading

        viewModelScope.launch {
            try {
                val result = getLeaveListUseCase.invokeDeleteLeave(delete, leaveId, userId, companyId)
                _deleteLeaveResponse.value = ApiResultState.Success(result)
            } catch (e: Exception) {
                _deleteLeaveResponse.value = ApiResultState.ApiError(e.message ?: "Unknown error")
            }
        }
    }
}
