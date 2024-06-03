package com.example.monstruacion

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
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
    var  dpFecha: DatePicker?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme (R.style.Base_Theme_Monstruacion)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val goabout = findViewById<Button>(R.id.goabout)
        val saludo = findViewById<TextView>(R.id.nombreUsuario)


        lifecycleScope.launch(Dispatchers.IO){
            getUser().collect {
                Log.d("DataStore", "Store name: ${it.name}")
                withContext(Dispatchers.Main){

                    if (it.name.isNotEmpty()) {
                        saludo.text = "Bienvenida, ${it.name}"


                    }
                    if (it.lastperiod.isNotEmpty()) {

                        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
                        val sombreado = ContextCompat.getDrawable(this@MainActivity, R.color.pink)


                        val lastPeriodDate = it.lastperiod.split(",")
                        val year = lastPeriodDate[0].toInt()
                        val month = lastPeriodDate[1].toInt()
                        val day = lastPeriodDate[2].toInt()

                        val fecharesaltada = CalendarDay.from(year, month, day)

                        calendarView.addDecorator(EventDecorator(sombreado!!, listOf(fecharesaltada)))

                    } else {
                        welcomeChooseDate()
                    }


                }
            }
        }






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