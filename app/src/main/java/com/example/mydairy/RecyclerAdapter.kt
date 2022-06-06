package com.example.mydairy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent

class RecyclerAdapter(private val userList : ArrayList<User>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val current_item = userList[position]

        holder.itemdate.text = current_item.date
        holder.itemtitle.text = current_item.title
        holder.itemcontent.text = current_item.content
        holder.itempage.text = "Page " + current_item.page_no.toString()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemtitle: TextView = itemView.findViewById(R.id.title_display)
        var itemdate: TextView = itemView.findViewById(R.id.content_display)
        var itemcontent: TextView = itemView.findViewById(R.id.date_display)
        var itempage: TextView = itemView.findViewById(R.id.PageNo_display)
    }
}

