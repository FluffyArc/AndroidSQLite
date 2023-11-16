package com.example.prak3_sqlitedbwithimage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DBNAME, null, VER) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val query = "create table " + TABLENAME + "(id integer primary key, avatar blob, name text)"
        sqLiteDatabase.execSQL(query)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        val query = "drop table if exists " + TABLENAME + ""
        sqLiteDatabase.execSQL(query)
    }

    companion object {
        const val DBNAME = "praktek.db"
        const val TABLENAME = "user"
        const val VER = 1
    }
}