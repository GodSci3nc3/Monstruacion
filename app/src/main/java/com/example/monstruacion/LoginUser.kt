package com.example.monstruacion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginUser : UserData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)


        val nombre = findViewById<EditText>(R.id.nombre)
        val email = findViewById<EditText>(R.id.email)
        val contraseña = findViewById<EditText>(R.id.contraseña)
        val boton1 = findViewById<Button>(R.id.button)

        boton1.setOnClickListener{

            Login(nombre.text.toString(), email.text.toString(), contraseña.text.toString())
        }


    }
    fun Login(nombre: String, email: String, contraseña: String){
        val URL = "http://192.168.43.236/login.php"
        val queue = Volley.newRequestQueue(this)

        val r = object :  StringRequest(Method.POST,URL, Response.Listener { response ->

            if (response == "Bienvenida") {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

                lifecycleScope.launch(Dispatchers.IO) {
                    storeValues(nombre, email)
                }

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            }


        }, Response.ErrorListener { error ->
            Toast.makeText(this,"Error: $error", Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()
                parameters.put("nombre",nombre)
                parameters.put("email",email)
                parameters.put("contraseña",contraseña)

                return parameters
            }
        }
        queue.add(r)

    }
}