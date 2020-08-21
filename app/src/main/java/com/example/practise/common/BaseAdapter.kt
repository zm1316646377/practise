package com.example.practise.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practise.R

class BaseAdapter(private val context: Context): RecyclerView.Adapter<BaseAdapter.Companion.BaseViewHolder>() {

    private var data: List<String> = mutableListOf("Kathy1", "Kathy2","Kathy3","Kathy4","Kathy","Kathy","Kathy","Kathy","Kathy","Kathy","Kathy","Kathy","Kathy","Kathy","Kathy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return BaseViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.text.text = data[position]
    }

    companion object {
        class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
            var text: TextView = view.findViewById(R.id.text)
        }
    }
}