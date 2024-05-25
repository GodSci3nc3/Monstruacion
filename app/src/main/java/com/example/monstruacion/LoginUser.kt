package com.example.monstruacion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginUser : UserData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val nombre1 = findViewById<EditText>(R.id.nombre)
        val appaterno = findViewById<EditText>(R.id.apPaterno)
        val apmaterno = findViewById<EditText>(R.id.apMaterno)
        val edad = findViewById<EditText>(R.id.edad)
        val correo = findViewById<EditText>(R.id.email)
        val contraseña = findViewById<EditText>(R.id.contraseña)
        val boton1 = findViewById<Button>(R.id.button)

        boton1.setOnClickListener{

            lifecycleScope.launch(Dispatchers.IO) {  }
             Registros(nombre1.text.toString(), appaterno.text.toString(), apmaterno.text.toString(), edad, correo.text.toString(), contraseña.text.toString())
        }


    }
    fun Registros(nombre1: String, appaterno: String, apmaterno: String, edad: EditText, correo: String, contraseña: String){
        val URL = "http://192.168.0.20/registro.php"
        val queue = Volley.newRequestQueue(this)

        val r = object :  StringRequest(Method.POST,URL, Response.Listener { response ->
            Toast.makeText(this,response, Toast.LENGTH_SHORT).show()

            lifecycleScope.launch(Dispatchers.IO) {
                storeValues(nombre1, correo)
            }

            startActivity(Intent(this, Cuestionario::class.java))

        }, Response.ErrorListener { error ->
            Toast.makeText(this,"Error: $error", Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()
                parameters.put("nombre",nombre1)
                parameters.put("correo",correo)
                parameters.put("contraseña",contraseña)
                parameters.put("apellidoPaterno",appaterno)
                parameters.put("apellidoMaterno",apmaterno)
                parameters.put("edad",edad.text.toString())

                return parameters
            }
        }
        queue.add(r)

    }
}