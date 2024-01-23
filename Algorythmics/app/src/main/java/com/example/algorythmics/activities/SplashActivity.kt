package com.example.algorythmics.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.example.algorythmics.R


class SplashActivity : AppCompatActivity() {

    companion object{
        const val TAG = "SplashActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.


    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStar method called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume method called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause method called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop method called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy method called")
    }

//    override fun onRestart() {
//        super.onRestart()
//        Log.d(TAG, "onRestart method called")
//    }


}