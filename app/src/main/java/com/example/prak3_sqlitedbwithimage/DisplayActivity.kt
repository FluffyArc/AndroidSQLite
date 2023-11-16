package com.example.prak3_sqlitedbwithimage

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prak3_sqlitedbwithimage.DBHelper.Companion.TABLENAME


class DisplayActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var recyclerView: RecyclerView
    private var models = ArrayList<Model>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        dbHelper     = DBHelper(this)
        recyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        displayData()
    }

    private fun displayData() {
        sqLiteDatabase = dbHelper.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM $TABLENAME", null)
        var model: Model
        while (cursor.moveToNext()) {
            model = Model()
            model.id      = cursor.getInt(0)
            model.proavatar  = cursor.getBlob(1)
            model.name    = cursor.getString(2)
            models.add(model)
        }
        cursor.close()
        recyclerView.adapter =MyAdapter(this@DisplayActivity,models,sqLiteDatabase)
    }
}