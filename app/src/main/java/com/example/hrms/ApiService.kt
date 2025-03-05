package com.example.hrms

import android.view.Display.Mode
import com.example.hrms.Models.LoginResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("HMRS/select_api_position.php")
    fun getDepartment() : Observable<DepartmentModel>

    @FormUrlEncoded
    @POST("HMRS/select_api_dept.php")
    fun setDept(@Field("method") method: String): Observable<DepartmentModel>
//    fun setDept(@Body dept : String) : Observable<DepartmentModel>


    @FormUrlEncoded
    @POST("HMRS/login_check_api.php")
    fun setLogin(
        @Field("method") method: String,
        @Field("u_email") u_email: String,
        @Field("u_pass") u_pass : String
    ): Observable<LoginResponse>


    @FormUrlEncoded
    @POST("HMRS/insert_api.php?insert=insert")
    fun signUpUser(
        @Field("insert") insert : String,
        @Field("u_name") u_name : String,
        @Field("u_email") u_email : String,
        @Field("u_pass") u_pass : String,
        @Field("u_phone") u_phone : String,
        @Field("u_gender") u_gender : String,
        @Field("dept_id") dept_id : String,
        @Field("position_id") position_id: String,
        @Field("u_salary") u_salary: String,
        @Field("u_joining_date") u_joining_date: String,
        @Field("u_dob") u_dob: String
    ) : Observable<ApiResponse>

}