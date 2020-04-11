package com.andrescalco.workhourscalculator

import android.content.Context
import android.content.SharedPreferences

object TimeStoreManager {
    private const val TIME_ARRIVED = "TIME_ARRIVED"
    private const val TIME = "time"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            TIME_ARRIVED,
            Context.MODE_PRIVATE
        )
    }

    fun getArrivedTime(context: Context, fallback: String): String? {
        return getSharedPreferences(context)
            .getString(TIME, fallback)
    }

    fun setArrivedTime(
        context: Context,
        newValue: String?
    ) {
        val editor =
            getSharedPreferences(context).edit()
        editor.putString(TIME, newValue)
        editor.apply()
    }
}