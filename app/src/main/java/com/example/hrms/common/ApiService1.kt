package com.example.hrms.common

import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveRequestResponse
import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveTypeResponse
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveListResponse
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveDeteleResponse
import com.example.hrms.holidaysModule.data.model.PublicHolidaysResponse
import com.example.hrms.signInModule.data.model.LoginResponse
import com.example.hrms.signUpModule.data.model.CompanyResponse
import com.example.hrms.signUpModule.data.model.ApiResponse
import com.example.hrms.signUpModule.data.model.DepartmentModel
import com.example.hrms.signUpModule.data.model.PositionResponse
import com.example.hrms.userProfileModule.data.model.UserDataResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.math.BigInteger

interface ApiService1 {

    @FormUrlEncoded
    @POST("HMRS/select_company.php")
    suspend fun selectCompany(
        @Field("method") method: String
    ): Response<CompanyResponse>

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

    @FormUrlEncoded
    @POST("HMRS/insert_api.php?insert=insert")
    suspend fun signUpUser(
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
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("HMRS/login_check_api.php")
    suspend fun setLogin(
        @Field("method") method: String,
        @Field("u_email") u_email: String,
        @Field("u_pass") u_pass: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("HMRS/fetch_all_data.php")
    suspend fun getUserData(
        @Field("method") method: String,
        @Field("u_email") u_email: String
    ): Response<UserDataResponse>

    @FormUrlEncoded
    @POST("HMRS/select_leave_type.php")
    suspend fun leaveType(
        @Field("method") method: String
    ): Response<LeaveTypeResponse>

    @FormUrlEncoded
    @POST("HMRS/leave_request.php")
    suspend fun applyLeave(
        @Field("insert") insert: String,
        @Field("company_id") company_id: Int,
        @Field("u_id") u_id: Int,
        @Field("leave_type_id") leave_type_id: Int,
        @Field("l_reason") l_reason: String,
        @Field("l_start_date") l_start_date: String

    ): Response<LeaveRequestResponse>

    @FormUrlEncoded
    @POST("HMRS/select_leave_master.php")
    suspend fun selectLeave(
        @Field("method") method: String,
        @Field("u_id") u_id :Int
    ):Response<LeaveListResponse>

    @FormUrlEncoded
    @POST("HMRS/delete_leave.php")
    suspend fun deleteLeave(
        @Field("delete") delete : String,
        @Field("l_id") l_id : Int,
        @Field("u_id") u_id : Int,
        @Field("company_id") company_id : Int
    ) : Response<LeaveDeteleResponse>

    @FormUrlEncoded
    @POST("HMRS/select_holidays.php")
    suspend fun getHolidays(
        @Field("method") method : String,
        @Field("company_id") company_id : Int
    ) : Response<PublicHolidaysResponse>
}