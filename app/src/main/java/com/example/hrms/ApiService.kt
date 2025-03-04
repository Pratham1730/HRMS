package com.example.hrms

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.POST

interface ApiService {

    @POST("select_api_position.php")
    fun getDepartment() : Observable<DepartmentModel>

}