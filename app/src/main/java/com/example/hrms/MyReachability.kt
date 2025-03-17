package com.example.hrms

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object Constants {
    const val REACHABILITY_SERVER = "https://www.google.com" // or any endpoint you want to ping
    const val LANDING_SERVER = "http://192.168.4.140/" // replace with your server URL
}

object MyReachability {

    private fun hasNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network?.isConnected) ?: false
    }

    fun hasInternetConnected(context: Context): Boolean {
        if (hasNetworkAvailable(context)) {
            try {
                val connection = URL(Constants.REACHABILITY_SERVER).openConnection() as HttpURLConnection
                connection.setRequestProperty("User-Agent", "Test")
                connection.setRequestProperty("Connection", "close")
                connection.connectTimeout = 1500 // configurable
                connection.connect()
                return (connection.responseCode == 200)
            } catch (e: IOException) {
                //Log.e(classTag, "Error checking internet connection", e)
            }
        } else {
            //Log.w(classTag, "No network available!")
        }
        //Log.d(classTag, "hasInternetConnected: false")
        return false
    }

    fun hasServerConnected(context: Context): Boolean {
        if (hasNetworkAvailable(context)) {
            try {
                val connection = URL(Constants.LANDING_SERVER).openConnection() as HttpURLConnection
                connection.setRequestProperty("User-Agent", "Test")
                connection.setRequestProperty("Connection", "close")
                connection.connectTimeout = 5000
                connection.connect()
                Log.d("Reachability", "HTTP response code: ${connection.responseCode}")
                return (connection.responseCode == 200)
            } catch (e: IOException) {
                Log.e("Reachability", "Exception: ${e.localizedMessage}", e)
            }

        }
        return false
    }
}