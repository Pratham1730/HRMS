package com.example.hrms.activity

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hrms.MyReachability
import com.example.hrms.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var isServerConnected = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        isConnected()

    }

    private fun launchActivity(){
        val logo = findViewById<ImageView>(R.id.logo)
        logo.alpha = 0f
        logo.animate().alpha(1f).setDuration(1500).start()


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }, 2000)
    }

    private fun alertDialog(){
        AlertDialog.Builder(this)
            .setTitle("Server Not Connected")
            .setMessage("Restart The App After Connecting to Server")
            .setCancelable(false)
            .show()
    }

    private fun isConnected() {
        lifecycleScope.launch {
            val isServerReachable = withContext(Dispatchers.IO) {
                MyReachability.hasServerConnected(this@MainActivity)
            }
            isServerConnected = isServerReachable

            if (!isServerReachable){
                alertDialog()
            }
            else{
                launchActivity()
            }
        }
    }
}