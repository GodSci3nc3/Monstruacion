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
import java.util.Calendar

class MainActivity : UserData() {
    var  dpFecha: DatePicker?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme (R.style.Base_Theme_Monstruacion)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val goabout = findViewById<Button>(R.id.goabout)
        val saludo = findViewById<TextView>(R.id.nombreUsuario)
        val logout = findViewById<Button>(R.id.logout)
        val newPeriod = findViewById<Button>(R.id.newPeriod)


        lifecycleScope.launch(Dispatchers.IO){
            getUser().collect {
                Log.d("DataStore", "Store name: ${it.name}")
                withContext(Dispatchers.Main){

                    if (it.name.isNotEmpty()) {
                        saludo.text = "Bienvenida, ${it.name}"


                    }
                    else {
                        saludo.text = "Bienvenida, Alondra"
                    }
                    if (it.lastperiod != "0" && it.lastperiod.isNotEmpty()) {

                        // Funciones para resaltar el día que le bajó y los cinco días de periodo
                        lastPeriod(it.lastperiod)
                        periodRange(it.lastperiod)

                        // Funciones para resaltar la predicción del siguiente periodo y el rango de ovulación
                        nextPeriod(it.lastperiod)
                        ovulationRange(it.lastperiod)

                    } else {
                        welcomeChooseDate()
                    }


                }
            }
        }






        goabout.setOnClickListener { goabout() }
        logout.setOnClickListener {
            val dataStoreManager = UserData()
            dataStoreManager.clearData()

            startActivity(Intent(this@MainActivity, Carga::class.java))
            finish() }

        newPeriod.setOnClickListener {chooseDate() }


        /*
datePicker?.setOnDateChangedListener{ datePicker, year, month, dayOfMonth ->
    datePicker?.Visibility= View.GONE
    txtFecha?.setText(getFechaCal)

}
*/
    }


/*
    fun muestraCalendario(view: View){
        dpFecha?.visibility=View.VISIBLE
    }
*/
    fun lastPeriod(lastperiod: String) {
        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
        val sombreado = ContextCompat.getDrawable(this@MainActivity, R.drawable.ranges_calendar_background)


        val lastPeriodDate = lastperiod.split(",")
        val year = lastPeriodDate[0].toInt()
        val month = lastPeriodDate[1].toInt()
        val day = lastPeriodDate[2].toInt()

        val fecharesaltada = CalendarDay.from(year, month, day)

        calendarView.addDecorator(EventDecorator(sombreado!!, listOf(fecharesaltada)))

    }

    fun nextPeriod(lastPeriod: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            getUser().collect {
                withContext(Dispatchers.Main){ // Se usó la librería Calendar para manejar las fechas con eficiencia
                when (it.duracion_p) {
                    "De 25 a 30 días" -> {
                        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
                        val sombreado = ContextCompat.getDrawable(this@MainActivity, R.drawable.prediction_background)

                        val lastPeriodDate = lastPeriod.split(",")
                        val year = lastPeriodDate[0].toInt()
                        val month = lastPeriodDate[1].toInt() - 1 // Los meses en Calendar empiezan desde 0, desconozco por qué
                        val day = lastPeriodDate[2].toInt()

                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, day)
                        calendar.add(Calendar.DAY_OF_MONTH, 25) // Se le suman 25 días

                        val nextPeriodDate = CalendarDay.from(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )

                        calendarView.addDecorator(EventDecorator(sombreado!!, listOf(nextPeriodDate)))
                    }
                    "De 21 a 35 días" -> {
                        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
                        val sombreado = ContextCompat.getDrawable(this@MainActivity, R.drawable.prediction_background)

                        val lastPeriodDate = lastPeriod.split(",")
                        val year = lastPeriodDate[0].toInt()
                        val month = lastPeriodDate[1].toInt() - 1
                        val day = lastPeriodDate[2].toInt()

                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, day)
                        calendar.add(Calendar.DAY_OF_MONTH, 28) // Se le suman 28 días

                        val nextPeriodDate = CalendarDay.from(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )

                        calendarView.addDecorator(EventDecorator(sombreado!!, listOf(nextPeriodDate)))
                    }
                    else -> {
                        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
                        val sombreado = ContextCompat.getDrawable(this@MainActivity, R.drawable.prediction_background)

                        val lastPeriodDate = lastPeriod.split(",")
                        val year = lastPeriodDate[0].toInt()
                        val month = lastPeriodDate[1].toInt() - 1
                        val day = lastPeriodDate[2].toInt()

                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, day)
                        calendar.add(Calendar.DAY_OF_MONTH, 30) // Se le suman 30 días

                        val nextPeriodDate = CalendarDay.from(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )

                        calendarView.addDecorator(EventDecorator(sombreado!!, listOf(nextPeriodDate)))
                    }
                }
            }}
        }
    }

    fun periodRange(lastPeriod: String) {
        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
        val sombreado = ContextCompat.getDrawable(this@MainActivity, R.color.pinktwo)
        val lastPeriodDate = lastPeriod.split(",")
        val year = lastPeriodDate[0].toInt()
        val month = lastPeriodDate[1].toInt() - 1
        val day = lastPeriodDate[2].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val startPeriodDate = CalendarDay.from(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        calendar.add(Calendar.DAY_OF_MONTH, 4)
        val finalPeriodDate = CalendarDay.from(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        calendarView.addDecorator(RangeDecorator(sombreado!!, startPeriodDate, finalPeriodDate))
    }

    fun ovulationRange(lastPeriod: String) {
        val calendarView = findViewById<MaterialCalendarView>(R.id.dpFecha)
        val sombreado = ContextCompat.getDrawable(this@MainActivity, R.color.pinkthree)
        val lastPeriodDate = lastPeriod.split(",")
        val year = lastPeriodDate[0].toInt()
        val month = lastPeriodDate[1].toInt() - 1
        val day = lastPeriodDate[2].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        calendar.add(Calendar.DAY_OF_MONTH, 11)
        val startPeriodDate = CalendarDay.from(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        calendar.add(Calendar.DAY_OF_MONTH, 4)
        val finalPeriodDate = CalendarDay.from(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        calendarView.addDecorator(RangeDecorator(sombreado!!, startPeriodDate, finalPeriodDate))
    }

    fun goabout() {
        startActivity(Intent(this@MainActivity, about::class.java))
    }




}