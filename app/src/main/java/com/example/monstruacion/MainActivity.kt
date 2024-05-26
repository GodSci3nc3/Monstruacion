package com.example.monstruacion

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : UserData() {
    var  txtfecha: EditText?=null
    var  dpFecha: DatePicker?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme (R.style.Base_Theme_Monstruacion)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
        val sombreadoRango = ContextCompat.getDrawable(this, R.color.pink)

        val fechaInicio = CalendarDay.from(2024, 5, 15)
        val fechaFin = CalendarDay.from(2024, 5, 20)

        calendarView.addDecorator(RangeDecorator(sombreadoRango!!, fechaInicio, fechaFin))



        val goabout = findViewById<Button>(R.id.goabout)
        val saludo = findViewById<TextView>(R.id.nombreUsuario)


        lifecycleScope.launch(Dispatchers.IO){
            getUser().collect {
                Log.d("DataStore", "Store name: ${it.name}")
                withContext(Dispatchers.Main){

                    if (it.name.isNotEmpty()) {
                        saludo.text = "Bienvenida, ${it.name}"


                    } else {
                        if ((it.name.isNotEmpty())){
                            saludo.text = "Bienvenida, ${it.name}"

                        }
                    }


                }
            }
        }




        txtfecha?.setText(getFechaDatePicker(dpFecha))



        goabout.setOnClickListener { goabout() }

        /*
datePicker?.setOnDateChangedListener{ datePicker, year, month, dayOfMonth ->
    datePicker?.Visibility= View.GONE
    txtFecha?.setText(getFechaCal)

}
*/
    }



    fun muestraCalendario(view: View){
        dpFecha?.visibility=View.VISIBLE
    }

    fun goabout() {
        startActivity(Intent(this@MainActivity, about::class.java))
    }




}