package com.example.schedule.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R

class TimeAdapter : RecyclerView.Adapter<TimeAdapter.TimeAdapterViewHolder>() {

    inner class TimeAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAdapterViewHolder {
        return TimeAdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rows_for_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeAdapterViewHolder, position: Int) {
        holder.itemView.apply {
            val text = findViewById<TextView>(R.id.timeText)

            val realPos = position % differ.currentList.size

            text.text = differ.currentList[realPos]
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}
