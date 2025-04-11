package com.example.hrms.userProfileModule.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrms.common.ApiResultState
import com.example.hrms.signInModule.domain.model.LoginDomainResponse
import com.example.hrms.signInModule.domain.model.UserDomain
import com.example.hrms.userProfileModule.domain.model.UserDataDomainResponse
import com.example.hrms.userProfileModule.domain.model.UserDomainData
import com.example.hrms.userProfileModule.domain.useCase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) : ViewModel(){

    private val _user = MutableLiveData<ApiResultState<UserDataDomainResponse>>()
    val user : LiveData<ApiResultState<UserDataDomainResponse>> get() = _user

    private val _userData = MutableLiveData<UserDomainData>()
    val userData: LiveData<UserDomainData> get() = _userData

    fun userData(method : String , userEmail : String){
        _user.value = ApiResultState.Loading
        viewModelScope.launch {
            try {
                val result = userProfileUseCase.invoke(method, userEmail)
                if (result.status == 200) {
                    _user.value = ApiResultState.Success(result)
                    val user = result.user
                    _userData.value = user!!
                } else {
                    _user.value = ApiResultState.ApiError(result.message.toString())
                }
            } catch (e: Exception) {
                _user.value = ApiResultState.ApiError(e.message ?: "Unknown error")
            }
        }
    }
}