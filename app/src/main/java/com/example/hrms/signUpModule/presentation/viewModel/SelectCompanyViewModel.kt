package com.example.hrms.signUpModule.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.hrms.common.ApiResultState
import com.example.hrms.signUpModule.domain.model.CompanyDomainItem
import com.example.hrms.signUpModule.domain.model.CompanyDomainResponse
import com.example.hrms.signUpModule.domain.useCase.GetCompanyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectCompanyViewModel  @Inject constructor(private val getCompanyUseCase: GetCompanyUseCase) : ViewModel(){

    private val _company = MutableLiveData<ApiResultState<CompanyDomainResponse>>()
    val company : LiveData<ApiResultState<CompanyDomainResponse>> get() = _company

    private val _companyList = MutableLiveData<List<CompanyDomainItem?>>()
    val companyList: LiveData<List<CompanyDomainItem?>> get() = _companyList

    fun loadCompanies(method : String){
        _company.value = ApiResultState.Loading
        viewModelScope.launch {
            try {
                val result = getCompanyUseCase.invoke(method)
                _company.value = ApiResultState.Success(result)

                val companyItems = result.company
                _companyList.value = companyItems!!
            }
            catch (e : Exception){
                _company.value = ApiResultState.ApiError(e.message ?: "Unknown error")
                _companyList.value = emptyList()
            }
        }
    }



}