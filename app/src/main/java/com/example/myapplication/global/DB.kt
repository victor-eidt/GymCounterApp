package com.example.myapplication.global

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.techplus.gymmanagement.global.SqlTable

class DB(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SqlTable.admin)
        db.execSQL(SqlTable.member)
        db.execSQL(SqlTable.fee)

        val insertAdminQuery = "INSERT INTO ADMIN(USER_NAME, PASSWORD, MOBILE) VALUES ('Admin', '123456', '1234567890')"
        db.execSQL(insertAdminQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun executeQuery(sql: String): Boolean {
        return try {
            val database = this.writableDatabase
            database.execSQL(sql)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun fireQuery(sql: String): Cursor? {
        var temCursor: Cursor? = null
        try {
            val database = this.writableDatabase
            temCursor = database.rawQuery(sql, null)
            if (temCursor != null && temCursor.count > 0) {
                temCursor.moveToFirst()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return temCursor
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "Gym.DB"
    }
}