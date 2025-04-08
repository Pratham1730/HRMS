package com.example.hrms.common

import com.example.hrms.signUpModule.data.model.DepartmentModel
import com.example.hrms.signUpModule.data.model.PositionResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService1 {

    @FormUrlEncoded
    @POST("HMRS/select_api_dept.php")
    suspend fun setDept(
        @Field("method") method: String,
        @Field("company_id") company_id: Int
    ): Response<DepartmentModel>

    @FormUrlEncoded
    @POST("HMRS/select_api_position.php")
    suspend fun getPosition(
        @Field("method") method: String,
        @Field("dept_id") dept_id: Int
    ): Response<PositionResponse>
}