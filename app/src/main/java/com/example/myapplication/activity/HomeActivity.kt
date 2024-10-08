package com.example.myapplication.activity

import android.nfc.Tag
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityHomeBinding
import com.google.android.gms.cast.framework.SessionManager

class HomeActivity : AppCompatActivity() {
    private val TAG="homeActivity"
    var session:SessionManager?
    lateinit var  binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        }
    }
}