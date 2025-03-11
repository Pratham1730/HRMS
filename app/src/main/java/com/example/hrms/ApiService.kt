package com.example.hrms

import com.example.hrms.models.UpdateDataModel
import com.example.hrms.responses.ApiResponse
import com.example.hrms.responses.CompanyResponse
import com.example.hrms.responses.DepartmentModel
import com.example.hrms.responses.LeaveDeteleResponse
import com.example.hrms.responses.LeaveListResponse
import com.example.hrms.responses.LeaveRequestResponse
import com.example.hrms.responses.LeaveTypeResponse
import com.example.hrms.responses.LoginResponse
import com.example.hrms.responses.PositionResponse
import com.example.hrms.responses.UpdateDataResponse
import com.example.hrms.responses.UpdatePasswordResponse
import com.example.hrms.responses.UserDataResponse
import com.example.hrms.responses.VerifyOtpResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.math.BigInteger
import java.sql.Date
import java.text.SimpleDateFormat

interface ApiService {


    @FormUrlEncoded
    @POST("HMRS/select_api_dept.php")
    fun setDept(
        @Field("method") method: String,
        @Field("company_id") company_id: Int
    ): Observable<DepartmentModel>

    @FormUrlEncoded
    @POST("HMRS/select_api_position.php")
    fun getPosition(
        @Field("method") method: String,
        @Field("dept_id") dept_id: Int
    ): Observable<PositionResponse>

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
        @Field("company_id") company_id: Int
    ): Observable<ApiResponse>

    @FormUrlEncoded
    @POST("HMRS/fetch_all_data.php")
    fun getUserData(
        @Field("method") method: String,
        @Field("u_email") u_email: String
    ): Observable<UserDataResponse>

    @FormUrlEncoded
    @POST("HMRS/update_user.php")
    fun updateUser(@Body model: UpdateDataModel): Observable<UpdateDataResponse>

    @FormUrlEncoded
    @POST("HMRS/select_leave_type.php")
    fun leaveType(
        @Field("method") method: String
    ): Observable<LeaveTypeResponse>

    @FormUrlEncoded
    @POST("HMRS/leave_request.php")
    fun applyLeave(
        @Field("insert") insert: String,
        @Field("company_id") company_id: Int,
        @Field("u_id") u_id: Int,
        @Field("leave_type_id") leave_type_id: Int,
        @Field("l_reason") l_reason: String,
        @Field("l_start_date") l_start_date: String

    ): Observable<LeaveRequestResponse>

    @FormUrlEncoded
    @POST("HMRS/select_leave_master.php")
    fun selectLeave(
        @Field("method") method: String,
        @Field("u_id") u_id :Int
    ):Observable<LeaveListResponse>

    @FormUrlEncoded
    @POST("HMRS/select_company.php")
    fun selectCompany(
        @Field("method") method: String
    ): Observable<CompanyResponse>

    @FormUrlEncoded
    @POST("HMRS/new_generate_otp_api.php")
    fun getOtp(
        @Field("method") method: String,
        @Field("u_email") u_email: String,
        @Field("u_phone") u_phone: BigInteger
    ) : Observable<ResponseBody>

    @FormUrlEncoded
    @POST("HMRS/new_verify_otp_api.php")
    fun verifyOtp(
        @Field("u_email") u_email : String,
        @Field("otp_code") otp_code : Int
    ) : Observable<VerifyOtpResponse>

    @FormUrlEncoded
    @POST("HMRS/update_password.php")
    fun updatePassword(
        @Field("u_email") u_email: String,
        @Field("new_password") new_password: String
    ):Observable<UpdatePasswordResponse>

}