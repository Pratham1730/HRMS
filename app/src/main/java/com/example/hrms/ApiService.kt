package com.example.hrms

import android.view.Display.Mode
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("HMRS/select_api_position.php")
    fun getDepartment() : Observable<DepartmentModel>

    @FormUrlEncoded
    @POST("HMRS/select_api_dept.php")
    fun setDept(@Field("method") method: String): Observable<DepartmentModel>
//    fun setDept(@Body dept : String) : Observable<DepartmentModel>

}