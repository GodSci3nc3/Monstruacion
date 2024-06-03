package com.example.monstruacion

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_INFORMATION")

open class UserData : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    fun welcomeChooseDate() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.choose_date_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val dpFecha = dialog.findViewById<DatePicker>(R.id.dpFecha)

        val btnConfirmar = dialog.findViewById<Button>(R.id.btnConfirmar)

        btnConfirmar?.setOnClickListener {
            val lastperiod = getFechaDatePicker(dpFecha)
            lifecycleScope.launch(Dispatchers.IO) {
            storeValues("", "", lastperiod)
            }
            dialog.dismiss()

        }
        dialog.show()

    }

    fun getFechaDatePicker(dpFecha: DatePicker):String{
        var dia=dpFecha?.dayOfMonth.toString().padStart(2,'0')
        var mes=(dpFecha!!.month + 1).toString().padStart(2,'0')
        var año=dpFecha?.year.toString().padStart(4,'0')
        return año + "," + mes + "," + dia
    }

    fun getUser() = dataStore.data.map { preferences ->
        User(
            name = preferences[stringPreferencesKey("name")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            lastperiod = preferences[stringPreferencesKey("lastperiod")].orEmpty()
        )
    }

    suspend fun storeValues(username: String, email: String, lastperiod: String) {
        Log.d("DataStore", "Storing username: $username and email: $email")

        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("name")] = username
            preferences[stringPreferencesKey("email")] = email
            preferences[stringPreferencesKey("lastperiod")] = lastperiod

        }

    }

    data class User(
        val name: String,
        val email: String,
        val lastperiod: String
    )
}