package com.example.wisatabandung

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = getSharedPreferences("LoginSession", MODE_PRIVATE)

        // Cek apakah pengguna sudah login
        if (session.getBoolean("sudahLogin", false)) {
            masukHome()
            return
        }

        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    this,
                    "Email dan password harus diisi!",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (
                email == "atifah@gmail.com" &&
                password == "123456"
            ) {

                session.edit()
                    .putBoolean("sudahLogin", true)
                    .putString("nama", "Atifah")
                    .putString("email", email)
                    .apply()

                Toast.makeText(
                    this,
                    "Login berhasil!",
                    Toast.LENGTH_SHORT
                ).show()

                masukHome()

            } else {

                Toast.makeText(
                    this,
                    "Email atau password salah!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun masukHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}