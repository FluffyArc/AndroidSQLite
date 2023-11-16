package com.example.prak3_sqlitedbwithimage

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.prak3_sqlitedbwithimage.DBHelper.Companion.TABLENAME
import java.io.ByteArrayOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private var id        = 0
    private var resId     = 0
    private lateinit var dbHelper       : DBHelper
    private lateinit var sqLiteDatabase : SQLiteDatabase
    private lateinit var avatar         : ImageView
    private lateinit var name           : EditText
    private lateinit var submit         : Button
    private lateinit var display        : Button
    private lateinit var edit           : Button
    private lateinit var bitmap         : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        avatar   = findViewById(R.id.avatar)
        name     = findViewById(R.id.edit_name)
        submit   = findViewById(R.id.btn_submit)
        display  = findViewById(R.id.btn_display)
        edit     = findViewById(R.id.btn_edit)

        imagePick()
        submit.setOnClickListener {
            insertData()
        }
        display.setOnClickListener {
            displayData()
        }
        updateData()
    }

    private fun imagePick() {
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data!!
                val uri = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    avatar.setImageBitmap(bitmap)
                    resId = 1
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        avatar.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            activityResultLauncher.launch(intent)
        }
    }

    private fun insertData() {
        if (resId == 1){
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val ImageViewToByte = byteArrayOutputStream.toByteArray()
            val cv = ContentValues()
            cv.put("avatar", ImageViewToByte)
            cv.put("name",   name.text.toString())
            sqLiteDatabase = dbHelper.writableDatabase
            sqLiteDatabase.insert(TABLENAME, null, cv)
            Toast.makeText(this@MainActivity, "Inserted Successfully", Toast.LENGTH_SHORT).show()
            avatar.setImageResource(R.drawable.ic_upload)
            name.setText("")
            resId = 0
        }
        else{
            Toast.makeText(this,"Select the image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayData(){
        val intent = Intent(this@MainActivity, DisplayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun updateData(){
        val bundle = intent.getBundleExtra("userdata")
        if (bundle != null) {
            id          = bundle.getInt("id")
            val bytes   = bundle.getByteArray("avatar")
            bitmap      = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
            avatar.setImageBitmap(bitmap)
            name.setText(bundle.getString("name"))
            //visible edit button and hide submit button
            submit.visibility   = View.GONE
            edit.visibility     = View.VISIBLE

            edit.setOnClickListener{
                if (resId == 1) {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
                    val ImageViewToByte = byteArrayOutputStream.toByteArray()
                    val cv = ContentValues()
                    cv.put("avatar", ImageViewToByte)
                    cv.put("name",   name.text.toString())
                    sqLiteDatabase = dbHelper.writableDatabase
                    sqLiteDatabase.update(TABLENAME, cv, "id=$id", null)
                    Toast.makeText(this@MainActivity, "Update Successfully", Toast.LENGTH_SHORT).show()
                    avatar.setImageResource(R.drawable.ic_upload)
                    name.setText("")
                    resId = 0
                }
                else{
                    val cv = ContentValues()
                    cv.put("name",   name.text.toString())
                    sqLiteDatabase = dbHelper.writableDatabase
                    sqLiteDatabase.update(TABLENAME, cv, "id=$id", null)
                    Toast.makeText(this@MainActivity, "Update Successfully", Toast.LENGTH_SHORT).show()
                    avatar.setImageResource(R.drawable.ic_upload)
                    name.setText("")
                    resId = 0
                }
            }
        }
        else{
            Toast.makeText(this,"Select the image first", Toast.LENGTH_SHORT).show()
        }
    }
}