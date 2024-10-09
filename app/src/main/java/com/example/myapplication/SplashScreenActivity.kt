package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.activity.HomeActivity
import com.example.myapplication.activity.LoginActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.global.DB
import com.example.myapplication.manager.SessionManager
import com.google.android.gms.cast.framework.zzao
import com.google.android.gms.cast.framework.zzay
import com.google.android.gms.cast.framework.zzba
import com.google.android.gms.dynamic.IObjectWrapper

class SplashScreenActivity : AppCompatActivity(), zzay {
    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 3000 // 3 segundos
    var db: DB? = null
    var session: SessionManager? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the Android version is 12 or higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            // System handles the splash screen, continue to your next activity
            navigateToNextScreen()
        } else {
            // For Android versions below 12, show your custom splash screen
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            db = DB(this)
            session = SessionManager(this)

            insertAdminData()
            mDelayHandler = Handler(Looper.getMainLooper())
            mDelayHandler?.postDelayed(mRunnable, splashDelay)
        }
    }

    private fun navigateToNextScreen() {
        if (session?.isLoggedIn == true) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun SessionManager(activity: zzay): SessionManager {

        return TODO("Provide the return value")
    }

    private val mRunnable: Runnable = Runnable {

        if (session?.isLoggedIn == true){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun insertAdminData() {
        try {
            val sqlCheck = "SELECT * FROM ADMIN"
            db?.fireQuery(sqlCheck)?.use {
                if (it.count > 0) {
                    Log.d("SplashActivity", "data available")
                } else {
                    val sqlQuery =
                        "INSERT OR REPLACE INTO ADMIN(ID,USER_NAME,PASSWORD,MOBILE)VALUES('1','Admin','12345','8888888888')"
                    db?.executeQuery(sqlQuery)
                }

            }

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelayHandler?.removeCallbacks(mRunnable)
    }

    override fun asBinder(): IBinder {
        TODO("Not yet implemented")
    }

    override fun zze(): Int {
        TODO("Not yet implemented")
    }

    override fun zzf(): IObjectWrapper {
        TODO("Not yet implemented")
    }

    override fun zzg(): IObjectWrapper {
        TODO("Not yet implemented")
    }

    override fun zzh(p0: zzao?) {
        TODO("Not yet implemented")
    }

    override fun zzi(p0: zzba?) {
        TODO("Not yet implemented")
    }

    override fun zzj(p0: Boolean, p1: Boolean) {
        TODO("Not yet implemented")
    }

    override fun zzk(p0: zzao?) {
        TODO("Not yet implemented")
    }

    override fun zzl(p0: zzba?) {
        TODO("Not yet implemented")
    }

    override fun zzm(p0: Bundle?) {
        TODO("Not yet implemented")
    }
}
