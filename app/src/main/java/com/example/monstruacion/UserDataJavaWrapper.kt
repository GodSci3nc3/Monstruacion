package com.example.monstruacion

class UserDataJavaWrapper(private val userData: UserData) {
    fun callChangeMyPeriodInformation(key: String, value: String) {
        userData.callChangeMyPeriodInformation(key, value)
    }
}