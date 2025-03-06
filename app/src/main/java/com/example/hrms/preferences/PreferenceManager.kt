package com.example.hrms.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE)
         private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveUserEmail(userEmail : String){
        sharedPreferences.edit().putString("USER_EMAIL" , userEmail).apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("USER_EMAIL", "")
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}