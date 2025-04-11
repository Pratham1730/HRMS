package com.example.hrms.signInModule.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrms.signInModule.domain.model.LoginDomainResponse
import com.example.hrms.signInModule.domain.model.UserDomain
import com.example.hrms.signInModule.domain.useCase.LoginUserUseCase
import com.example.hrms.common.ApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val getLoginUserUseCase: LoginUserUseCase) : ViewModel() {

    private val _login = MutableLiveData<ApiResultState<LoginDomainResponse>>()
    val login : LiveData<ApiResultState<LoginDomainResponse>> get() = _login

    private val _loginUserData = MutableLiveData<UserDomain>()
    val loginUserData: LiveData<UserDomain> get() = _loginUserData

    fun loginUser(method : String , userEmail : String , userPassword : String){
        _login.value = ApiResultState.Loading
        viewModelScope.launch {
            try {
                val result = getLoginUserUseCase.invoke(method , userEmail, userPassword)
                if (result.status == 200){
                    _login.value = ApiResultState.Success(result)
                    val user = result.user
                    _loginUserData.value = user!!
                }
                else{
                    _login.value = ApiResultState.ApiError(result.message.toString())
                }
            }
            catch (e : Exception){
                _login.value = ApiResultState.ApiError(e.message ?: "Unknown error")
            }
        }
    }
}