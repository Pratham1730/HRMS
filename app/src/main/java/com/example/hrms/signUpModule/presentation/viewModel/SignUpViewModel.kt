package com.example.hrms.signUpModule.presentation.viewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrms.common.ApiResultState
import com.example.hrms.signUpModule.domain.model.ApiDomainResponse
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import com.example.hrms.signUpModule.domain.model.DepartmentsDomainItem
import com.example.hrms.signUpModule.domain.model.PositionDomainResponse
import com.example.hrms.signUpModule.domain.model.PositionsDomainItem
import com.example.hrms.signUpModule.domain.useCase.GetDeptUseCase
import com.example.hrms.signUpModule.domain.useCase.GetPositionUseCase
import com.example.hrms.signUpModule.domain.useCase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel  @Inject constructor(private val getDeptUseCase: GetDeptUseCase , private val getPositionUseCase: GetPositionUseCase , private val signUpUseCase: SignUpUseCase) : ViewModel(){

    private val _deptList = MutableLiveData<ApiResultState<DepartmentDomainModel>>()
    val deptList : LiveData<ApiResultState<DepartmentDomainModel>> get() = _deptList

    private val _departments = MutableLiveData<List<DepartmentsDomainItem>>()
    val departments: LiveData<List<DepartmentsDomainItem>> get() = _departments

    private val _position = MutableLiveData<ApiResultState<PositionDomainResponse>>()
    val position : LiveData<ApiResultState<PositionDomainResponse>> get() = _position

    private val _positionsList = MutableLiveData<List<PositionsDomainItem>>()
    val positionsList: LiveData<List<PositionsDomainItem>> get() = _positionsList

    private val _signUpUser = MutableLiveData<ApiResultState<ApiDomainResponse>>()
    val signUpUser: LiveData<ApiResultState<ApiDomainResponse>> get() = _signUpUser


    fun loadDept(method : String , companyId : Int){
        _deptList.value = ApiResultState.Loading

        viewModelScope.launch {
            try {
                val result = getDeptUseCase.invoke(method , companyId)
                _deptList.value = ApiResultState.Success(result)

                val deptItems = result.departments?.filterNotNull() ?: emptyList()
                _departments.value = deptItems
            }
            catch (e : Exception){
                _deptList.value = ApiResultState.ApiError(e.message ?: "Unknown error")
                _departments.value = emptyList()
            }
        }
    }

    fun loadPositions(method : String , deptId : Int){
        _position.value = ApiResultState.Loading
        viewModelScope.launch {
            try {
                val result = getPositionUseCase.invoke(method , deptId)
                _position.value = ApiResultState.Success(result)

                val deptItems = result.positions
                _positionsList.value = deptItems
            }
            catch (e : Exception){
                _position.value = ApiResultState.ApiError(e.message ?: "Unknown error")
                _positionsList.value = emptyList()
            }
        }
    }

    fun signUpUser(
        method : String,
        name: String,
        mobile: BigInteger,
        email: String,
        department: Int,
        gender: Int,
        position: Int,
        salary: Int,
        dob: String,
        joining: String,
        password: String,
        company: Int
    ){
        _signUpUser.value = ApiResultState.Loading

        viewModelScope.launch {
            val result = signUpUseCase.invoke(method, name, mobile , email , department , gender , position , salary, dob, joining, password, company)
            if (result.status == 200) {
                _signUpUser.value = ApiResultState.Success(result)
            }
            else{
                _signUpUser.value = ApiResultState.ApiError(result.message)
            }
        }
    }

}