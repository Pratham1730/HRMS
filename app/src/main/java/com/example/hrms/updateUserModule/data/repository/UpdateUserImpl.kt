package com.example.hrms.updateUserModule.data.repository

import com.example.hrms.common.ApiService1
import com.example.hrms.updateUserModule.domain.mapper.UpdateUserMapper
import javax.inject.Inject

class UpdateUserImpl @Inject constructor(
    private val apiService1: ApiService1,
    private val mapper : UpdateUserMapper
){


}