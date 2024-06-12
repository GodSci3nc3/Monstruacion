package com.example.monstruacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Carga : UserData() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carga)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            checkUser()
        }, 2500)

    }


    private fun checkUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userProfile = getUserProfile()
            if (userProfile.name.isNotEmpty() && userProfile.email.isNotEmpty()) {
                // Si el usuario ya se ha logueado antes:

                startActivity(Intent(this@Carga, MainActivity::class.java))
                finish()


            } else {
                withContext(Dispatchers.Main) {

                    val i = Intent(this@Carga, RegisterUser::class.java)
                    startActivity(i)
                    finish()

                }
            }
        }
    }
}