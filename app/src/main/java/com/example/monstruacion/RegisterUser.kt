package com.example.monstruacion
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterUser : UserData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        // Declaración de variables 
        val nombre = findViewById<EditText>(R.id.nombre)
        val appaterno = findViewById<EditText>(R.id.apPaterno)
        val apmaterno = findViewById<EditText>(R.id.apMaterno)
        val edad = findViewById<EditText>(R.id.edad)
        val correo = findViewById<EditText>(R.id.email)
        val contraseña = findViewById<EditText>(R.id.contraseña)
        val boton1 = findViewById<Button>(R.id.button)
        val loginbutton = findViewById<TextView>(R.id.loginbutton)

        // El usuario ingresó sus datos y le dió a Registrarse
        boton1.setOnClickListener{

            lifecycleScope.launch(Dispatchers.IO) {  }
            Registros(nombre.text.toString(), appaterno.text.toString(), apmaterno.text.toString(), edad, correo.text.toString(), contraseña.text.toString())
        }

        loginbutton.setOnClickListener { startActivity(Intent(this@RegisterUser, LoginUser::class.java)) }


    }

    // Función que se encarga de enviar los datos del usuario a la API para guardarlos en la base de datos 
    fun Registros(nombre: String, appaterno: String, apmaterno: String, edad: EditText, correo: String, contraseña: String){
        val URL = "http://192.168.43.236/registro.php"
        val queue = Volley.newRequestQueue(this)

        val r = object :  StringRequest(Method.POST,URL, Response.Listener { response ->
            Toast.makeText(this,response, Toast.LENGTH_SHORT).show()

            lifecycleScope.launch(Dispatchers.IO) {
                // Insertar los datos en la base de datos. Excepto el último periodo porque aún no se ha calculado
                storeValues(nombre, correo)
            }

            val i = Intent(this@RegisterUser, Cuestionario::class.java)
            
            i.putExtra("nombre", nombre)
            startActivity(i)
            finish()

        }, Response.ErrorListener { error ->
            Toast.makeText(this,"Error: $error", Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()
                parameters.put("nombre",nombre)
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