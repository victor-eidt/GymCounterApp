package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.global.DB
import com.example.myapplication.manager.SessionManager

class LoginActivity : AppCompatActivity() {
    var db:DB?=null
    var session: SessionManager?=null
    var edtUserName : EditText?=null
    var edtPassWord : EditText?=null
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DB(this)
        session = SessionManager(this)
        edtUserName = binding.edtUserName
        edtPassWord = binding.edtPassword

        binding.btnLogin.setOnClickListener {

        }

        binding.txtForgotPassaword.setOnClickListener {
            if (validateLogin()){
                getLogin()
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun getLogin(){
        try {

            val sqlQuery = "SELECT * FROM ADMIN WHERE USER_NAME='"+edtUserName?.text.toString().trim()+ "' " + "AND PASSWORD='"+edtPassWord?.text.toString().trim()+"' AND='1'"
            db?.fireQuery(sqlQuery)?.use {
                if (it.count > 0) {
                    session?.setLogin(true)
                    Toast.makeText(this, "Login Bem Sucedido", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    session?.setLogin(false)
                    Toast.makeText(this, "Falha ao logar", Toast.LENGTH_LONG).show()
                }
            }
            }catch (e:Exception){
                e.printStackTrace()
            }


    private fun validateLogin():Boolean{
        if(edtUserName?.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Digite o usuario",Toast.LENGTH_LONG).show()
            return false
        }else if (edtPassWord?.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Digite a senha",Toast.LENGTH_LONG).show()
        }
    }
}