package com.example.monstruacion

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    var  txtfecha: EditText?=null
    var  btnfecha: ImageButton?=null
    var  dpFecha: DatePicker?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme (R.style.Base_Theme_Monstruacion)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dpFecha=findViewById(R.id.dpFecha)
        val goabout = findViewById<Button>(R.id.goabout)

        txtfecha?.setText(geFechaDatePicker())

        dpFecha?.setOnDateChangedListener{
           dpFecha,año,mes,dia->
            txtfecha?.setText(geFechaDatePicker())
            dpFecha?.visibility=View.GONE
        }

        goabout.setOnClickListener { goabout() }

        /*
datePicker?.setOnDateChangedListener{ datePicker, year, month, dayOfMonth ->
    datePicker?.Visibility= View.GONE
    txtFecha?.setText(getFechaCal)

}
*/
    }

    fun geFechaDatePicker():String{
        var dia=dpFecha?.dayOfMonth.toString().padStart(2,'0')
        var mes=(dpFecha!!.month+1).toString().padStart(2,'0')
        var año=dpFecha?.year.toString().padStart(4,'0')
        return dia+"/"+mes+"/"+año
    }

    fun muestraCalendario(view: View){
        dpFecha?.visibility=View.VISIBLE
    }

    fun goabout() {
        startActivity(Intent(this@MainActivity, about::class.java))
    }

}