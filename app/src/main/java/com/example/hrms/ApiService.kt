package com.example.hrms

import com.example.hrms.responses.ApiResponse
import com.example.hrms.responses.AttendanceResponse
import com.example.hrms.responses.CompanyResponse
import com.example.hrms.responses.DashboardResponse
import com.example.hrms.responses.DepartmentEmployeeResponse
import com.example.hrms.responses.DepartmentModel
import com.example.hrms.responses.EnterAttendanceResponse
import com.example.hrms.responses.InsertAbsentResponse
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveDeteleResponse
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveListResponse
import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveRequestResponse
import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveTypeResponse
import com.example.hrms.responses.LoginResponse
import com.example.hrms.holidaysModule.data.model.PublicHolidaysResponse
import com.example.hrms.updateUserModule.data.model.UpdateDataResponse
import com.example.hrms.responses.UpdatePasswordResponse
import com.example.hrms.userProfileModule.data.model.UserDataResponse
import com.example.hrms.responses.VerifyOtpResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.math.BigInteger

interface ApiService {


    @FormUrlEncoded
    @POST("HMRS/select_api_dept.php")
    fun setDept(
        @Field("method") method: String,
        @Field("company_id") company_id: Int
    ): Observable<DepartmentModel>



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

    @Multipart
    @POST("HMRS/update_user.php")
    fun updateUser(
        @Part("method") method : RequestBody,
        @Part("u_id") u_id : RequestBody,
        @Part("u_name") u_name : RequestBody,
        @Part("u_phone") u_phone : RequestBody,
        @Part("u_dob") u_dob : RequestBody,
        @Part user_img : MultipartBody.Part?
    ): Observable<UpdateDataResponse>

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

    @FormUrlEncoded
    @POST("HMRS/delete_leave.php")
    fun deleteLeave(
        @Field("delete") delete : String,
        @Field("l_id") l_id : Int,
        @Field("u_id") u_id : Int,
        @Field("company_id") company_id : Int
    ) : Observable<LeaveDeteleResponse>

    @FormUrlEncoded
    @POST("HMRS/select_user_by_company.php")
    fun departmentEmployee(
        @Field("method") method : String,
        @Field("dept_id") dept_id : Int,
        @Field("company_id") company_id : Int
    ) : Observable<DepartmentEmployeeResponse>

    @FormUrlEncoded
    @POST("HMRS/select_holidays.php")
    fun getHolidays(
        @Field("method") method : String,
        @Field("company_id") company_id : Int
    ) : Observable<PublicHolidaysResponse>

    @FormUrlEncoded
    @POST("HMRS/insert_attendace.php")
    fun insertAttendance(
        @Field("user_id") user_id : Int,
        @Field("a_date") a_date : String,
        @Field("a_punch_time") a_punch_time : String,
        @Field("company_id") company_id : Int,
        @Field("action") action : String,
        @Field("latitude") latitude : Double,
        @Field("longitude") longitude : Double
    ) : Observable<EnterAttendanceResponse>

    @FormUrlEncoded
    @POST("HMRS/attendance_data_month.php")
    fun getAttendance(
        @Field("u_id") u_id : Int,
        @Field("month") month : Int,
        @Field("year") year : Int
    ) : Observable<AttendanceResponse>

    @FormUrlEncoded
    @POST("HMRS/insert_absent.php")
    fun insertAbsent(
        @Field("user_id") user_id : Int,
        @Field("month") month : Int,
        @Field("year") year : Int
    ) : Observable<InsertAbsentResponse>


    @FormUrlEncoded
    @POST("HMRS/select_dashboard.php")
    fun dashboard(
        @Field("method") method : String,
        @Field("u_id") u_id :String
    ) : Observable<DashboardResponse>
}