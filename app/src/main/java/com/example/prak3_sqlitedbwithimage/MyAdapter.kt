package com.example.prak3_sqlitedbwithimage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.prak3_sqlitedbwithimage.DBHelper.Companion.TABLENAME
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MyAdapter(private var context: Context, private var modelArrayList: ArrayList<Model>, private var sqLiteDatabase: SQLiteDatabase) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.singledata, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model   = modelArrayList[position]
        val image   = model.proavatar
        val bitmap  = BitmapFactory.decodeByteArray(image, 0, image!!.size)
        holder.imageavatar.setImageBitmap(bitmap)
        holder.txtname.text = model.name
        holder.flowmenu.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.flowmenu)
            popupMenu.inflate(R.menu.flow_menu)
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.edit_menu -> {
                        val bundle = Bundle()
                        bundle.putInt("id", model.id)
                        bundle.putByteArray("avatar", model.proavatar)
                        bundle.putString("name", model.name)
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("userdata", bundle)
                        context.startActivity(intent)
                    }

                    R.id.delete_menu -> {
                        MaterialAlertDialogBuilder(context).setTitle("Delete").setMessage("Yakin hapus?")
                            .setPositiveButton("Delete") { _, _ ->
                                val dbHelper = DBHelper(context)
                                sqLiteDatabase = dbHelper.readableDatabase
                                sqLiteDatabase.delete(TABLENAME, "id="+model.id, null)
                                Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            }
                            .setNegativeButton("Cancel"){_,_->}.show()
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageavatar     : ImageView
        var txtname         : TextView
        var flowmenu        : ImageButton

        init {
            imageavatar = itemView.findViewById(R.id.viewavatar)
            txtname     = itemView.findViewById(R.id.txt_name)
            flowmenu    = itemView.findViewById(R.id.flowmenu)
        }
    }
}
