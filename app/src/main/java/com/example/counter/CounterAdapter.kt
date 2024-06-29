package com.example.counter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CounterAdapter(private val context: Context, private var counterList: MutableList<Counter>) :
    RecyclerView.Adapter<CounterAdapter.CounterViewHolder>() {

    class CounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.counter_title)
        val tvValue: TextView = itemView.findViewById(R.id.counter_value)


        val tmblSet: ImageView = itemView.findViewById(R.id.setCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_counter, parent, false)
        return CounterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        val currentItem = counterList[position]
        holder.tvTitle.text = currentItem.title
        holder.tvValue.text = currentItem.value.toString()
        holder.tmblSet.setOnClickListener {
            val textView = (context as MainActivity).findViewById<TextView>(R.id.counter_value)
            val tekstoolbar = (context as MainActivity).findViewById<TextView>(R.id.toolbar_title)
            tekstoolbar.text = currentItem.title
            textView.text = currentItem.value.toString()
        }
    }

    override fun getItemCount() = counterList.size

    // Method to update the dataset and refresh the RecyclerView
    fun updateData(newList: MutableList<Counter>) {
        counterList = newList
        notifyDataSetChanged()
    }
}
