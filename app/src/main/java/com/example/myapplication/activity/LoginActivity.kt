package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    var db: DB? = null
    var session: SessionManager? = null
    var edtUserName: EditText? = null
    var edtPassWord: EditText? = null
    lateinit var binding: ActivityLoginBinding

    fun validateLogin(): Boolean {
        if (edtUserName?.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Digite o usuario", Toast.LENGTH_LONG).show()
            return false
        } else if (edtPassWord?.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Digite a senha", Toast.LENGTH_LONG).show()
            return true
        }
        return true
    }

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
            if (validateLogin()) {
                getLogin()  // Chama a função para verificar o login
            }
        }

        binding.txtForgotPassaword.setOnClickListener {
            if (validateLogin()) {
                getLogin()
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getLogin() {
        try {
            val userName = edtUserName?.text.toString().trim()
            val password = edtPassWord?.text.toString().trim()

            // Adicionando logs para verificar os valores
            Log.d("LoginActivity", "Tentando login com: $userName / $password")

            // Removendo a condição de ID e ajustando a consulta
            val sqlQuery =
                "SELECT * FROM ADMIN WHERE USER_NAME='$userName' AND PASSWORD='$password'"

            // Adicionando um log para verificar a query
            Log.d("LoginActivity", "Query SQL: $sqlQuery")

            db?.fireQuery(sqlQuery)?.use { cursor ->
                if (cursor.count > 0) {
                    Log.d("LoginActivity", "Login bem-sucedido")
                    session?.setLogin(true)
                    Toast.makeText(this, "Login Bem Sucedido", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("LoginActivity", "Falha no login")
                    session?.setLogin(false)
                    Toast.makeText(this, "Falha ao logar", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LoginActivity", "Erro durante o login: ${e.message}")
            Toast.makeText(this, "Erro ao tentar logar", Toast.LENGTH_LONG).show()
        }
    }
}
