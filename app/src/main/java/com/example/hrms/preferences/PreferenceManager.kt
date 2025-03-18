package com.example.hrms.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.hrms.responses.DepartmentsItem

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()



    fun saveUserId(userId : Int){
        sharedPreferences.edit().putInt("USER_ID" , userId).apply()
    }

    fun getUserId() : Int {
        return sharedPreferences.getInt("USER_ID" , -1)
    }

    fun saveCompanyId(companyId : Int){
        sharedPreferences.edit().putInt("COMPANY_ID" , companyId).apply()
    }

    fun getCompanyId() : Int {
        return sharedPreferences.getInt("COMPANY_ID" , -1)
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

    fun isPrevSignIn(isPrevSignIn : Boolean) {
        sharedPreferences.edit().putBoolean("IS_PREV_SIGN_IN" , isPrevSignIn).apply()
    }

    fun getIsPrevSignIn() : Boolean{
        return sharedPreferences.getBoolean("IS_PREV_SIGN_IN" , false)
    }

    fun isPunchIn(isPunchIn : Boolean){
        sharedPreferences.edit().putBoolean("IS_PUNCHED_IN", isPunchIn).apply()
    }
    fun getIsPunchIn() : Boolean{
        return sharedPreferences.getBoolean("IS_PUNCHED_IN" , false)
    }
    fun savePunchInTime(punchInTime: Long){
        sharedPreferences.edit().putLong("PUNCH_IN_TIME", punchInTime).apply()
    }
    fun getPunchInTime() : Long{
        return sharedPreferences.getLong("PUNCH_IN_TIME" , 0L)
    }
    fun removePunchInTime(){
        sharedPreferences.edit().remove("PUNCH_IN_TIME").apply()
    }
    fun saveUserImageUrl(url: String) {
        sharedPreferences.edit().putString("USER_IMAGE_URL", url).apply()
    }
    fun getUserImageUrl() : String? {
        return sharedPreferences.getString("USER_IMAGE_URL" , "")
    }
    fun logout() {
        editor.clear()
        editor.apply()
    }
}