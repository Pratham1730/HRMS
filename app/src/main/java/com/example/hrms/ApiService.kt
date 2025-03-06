package com.example.hrms

import com.example.hrms.Models.ApiResponse
import com.example.hrms.Models.DepartmentModel
import com.example.hrms.Models.LoginResponse
import com.example.hrms.Models.PositionResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.math.BigInteger

interface ApiService {

    @POST("HMRS/select_api_position.php")
    fun getDepartment(): Observable<DepartmentModel>

    @FormUrlEncoded
    @POST("HMRS/select_api_dept.php")
    fun setDept(@Field("method") method: String): Observable<DepartmentModel>
//    fun setDept(@Body dept : String) : Observable<DepartmentModel>

    @FormUrlEncoded
    @POST("HMRS/select_api_position.php")
    fun getPosition(@Field("method") method: String, @Field("dept_id") dept_id: Int) : Observable<PositionResponse>

    @FormUrlEncoded
    @POST("HMRS/login_check_api.php")
    fun setLogin(
        @Field("method") method: String,
        @Field("u_email") u_email: String,
        @Field("u_pass") u_pass: String
    ): Observable<LoginResponse>


    @FormUrlEncoded
    @POST("HMRS/insert_api.php?insert=insert")
    fun signUpUser(
        @Field("insert") insert: String,
        @Field("u_name") u_name: String,
        @Field("u_email") u_email: String,
        @Field("u_pass") u_pass: String,
        @Field("u_phone") u_phone: BigInteger,
        @Field("u_gender") u_gender: Int,
        @Field("dept_id") dept_id: Int,
        @Field("position_id") position_id: Int,
        @Field("u_salary") u_salary: Int,
        @Field("u_joining_date") u_joining_date: String,
        @Field("u_dob") u_dob: String,
        @Field("company_id") company_id : Int
    ): Observable<ApiResponse>

}