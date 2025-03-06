package com.example.hrms.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE)


    fun saveUserId(userId : Int){
        sharedPreferences.edit().putInt("USER_ID" , userId).apply()
    }


    fun getUserId() : Int {
        return sharedPreferences.getInt("USER_ID" , -1)
    }

    fun saveUserEmail(userEmail : String){
        sharedPreferences.edit().putString("USER_EMAIL" , userEmail).apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("USER_EMAIL", "")
    }

    fun saveUserName(userName : String){
        sharedPreferences.edit().putString("USER_NAME" , userName).apply()
    }

    fun getUserName() : String? {
        return sharedPreferences.getString("USER_NAME" , "")
    }

    fun saveUserPhone(phoneNo : String){
        sharedPreferences.edit().putString("USER_PHONE" , phoneNo).apply()
    }

    fun getUserPhone() : String? {
        return sharedPreferences.getString("USER_PHONE" , "")
    }

    fun saveUserDOB(userName : String){
        sharedPreferences.edit().putString("USER_DOB" , userName).apply()
    }

    fun getUserDOB() : String? {
        return sharedPreferences.getString("USER_DOB" , "")
    }
}