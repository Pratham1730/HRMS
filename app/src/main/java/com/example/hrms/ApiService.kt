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

//    @POST("localhost/HMRS/insert_api.php?insert=insert")
//    fun registerUser(
//        @field:("u_name") name :RequestBody,
//    )
@Multipart
@POST("your_api_endpoint.php") // Replace with your actual endpoint
fun signUpUser(
    @Part("insert") insert: RequestBody,
    @Part("u_name") name: RequestBody,
    @Part("u_email") email: RequestBody,
    @Part("u_pass") password: RequestBody,
    @Part("u_phone") phone: RequestBody,
    @Part("u_gender") gender: RequestBody,
    @Part("dept_id") deptId: RequestBody,
    @Part("position_id") positionId: RequestBody,
    @Part("u_salary") salary: RequestBody,
    @Part("u_joining_date") joiningDate: RequestBody,
    @Part("u_dob") dob: RequestBody
): Observable<ApiResponse>

    @FormUrlEncoded
    @POST("HMRS/login_check_api.php")
    fun setLogin(
        @Field("method") method: String,
        @Field("u_email") u_email: String,
        @Field("u_pass") u_pass : String
    ): Observable<LoginResponse>



}