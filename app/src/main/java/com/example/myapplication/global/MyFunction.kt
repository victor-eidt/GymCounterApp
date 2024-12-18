package com.example.myapplication.global

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat

class MyFunction {

    companion object {

        fun getValue(cursor: Cursor, columnName: String): String {
            var value: String = ""

            try {
                val col = cursor.getColumnIndex(columnName)
                value = cursor.getString(col)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("MyFunction","getValue ${e.printStackTrace()}")
                value = ""
            }

            return value
        }
    @SuppressLint("SimpleDateFormat")
    fun returnSQLDateFormat(date: String): String {
        try {
            if (date.trim().isNotEmpty()) {
                val dateFormat1 = SimpleDateFormat("dd/MM/yyyy")
                val firstDate = dateFormat1.parse(date)
                val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")
                return dateFormat2.format(firstDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
        @SuppressLint("SimpleDateFormat")
        fun returnUserDateFormat(date: String): String {
            try {
                if (date.trim().isNotEmpty()) {
                    val dateFormat1 = SimpleDateFormat("dd/MM/yyyy")
                    val firstDate = dateFormat1.parse(date)
                    val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")
                    return dateFormat2.format(firstDate)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }
}
