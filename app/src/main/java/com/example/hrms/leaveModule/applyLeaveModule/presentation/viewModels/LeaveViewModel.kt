package com.example.hrms.leaveModule.applyLeaveModule.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrms.common.ApiResultState
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveTypeDomainResponse
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveTypesDomainItem
import com.example.hrms.leaveModule.applyLeaveModule.domain.useCases.LeaveUseCase
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import com.example.hrms.signUpModule.domain.model.DepartmentsDomainItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveViewModel @Inject constructor(private val leaveUseCase: LeaveUseCase) : ViewModel(){


    private val _leaveType = MutableLiveData<ApiResultState<LeaveTypeDomainResponse>>()
    val leaveType : LiveData<ApiResultState<LeaveTypeDomainResponse>> get() = _leaveType

    private val _leaveTypeList = MutableLiveData<List<LeaveTypesDomainItem>>()
    val leaveTypeList: LiveData<List<LeaveTypesDomainItem>> get() = _leaveTypeList

    fun loadLeaveTypes(method : String){
        _leaveType.value = ApiResultState.Loading

        viewModelScope.launch {
            try {
                val result = leaveUseCase.invokeLeaveType(method)
                _leaveType.value = ApiResultState.Success(result)

                val deptItems = result.leaveType?.filterNotNull() ?: emptyList()
                _leaveTypeList.value = deptItems
            }
            catch (e : Exception){
                _leaveType.value = ApiResultState.ApiError(e.message ?: "Unknown error")
                _leaveTypeList.value = emptyList()
            }
        }
    }

}