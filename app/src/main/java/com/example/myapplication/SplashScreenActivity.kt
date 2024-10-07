package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.activity.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 3000 // 3 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDelayHandler = Handler(Looper.getMainLooper())
        mDelayHandler?.postDelayed(mRunnable, splashDelay)
    }

    private val mRunnable: Runnable = Runnable {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
