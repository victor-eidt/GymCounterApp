package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.activity.HomeActivity
import com.example.myapplication.activity.LoginActivity
import com.example.myapplication.global.DB
import com.example.myapplication.manager.SessionManager

class SplashScreenActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 3000 // 3 segundos
    var db: DB? = null
    var session: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelayHandler = Handler(Looper.getMainLooper())
        mDelayHandler?.postDelayed({
            navigateToNextScreen()
        }, splashDelay)

        db = DB(this)
        session = SessionManager(this)
        insertAdminData()
    }

    private fun navigateToNextScreen() {
        if (session?.isLoggedIn == true) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private fun insertAdminData() {
        try {
            val sqlCheck = "SELECT * FROM ADMIN"
            db?.fireQuery(sqlCheck)?.use {
                if (it.count > 0) {
                    Log.d("SplashActivity", "data available")
                } else {
                    val sqlQuery =
                        "INSERT OR REPLACE INTO ADMIN(ID,USER_NAME,PASSWORD,MOBILE) VALUES('1','Admin','12345','8888888888')"
                    db?.executeQuery(sqlQuery)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelayHandler?.removeCallbacksAndMessages(null)
    }
}
