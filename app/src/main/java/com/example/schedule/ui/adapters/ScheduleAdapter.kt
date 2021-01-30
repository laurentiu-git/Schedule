package com.example.schedule.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.data.models.ScheduleInfo
import javax.inject.Inject

class ScheduleAdapter @Inject constructor() : RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterViewHolder>() {

    inner class ScheduleAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<ScheduleInfo>() {

        override fun areItemsTheSame(oldItem: ScheduleInfo, newItem: ScheduleInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ScheduleInfo, newItem: ScheduleInfo): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapterViewHolder {
        return ScheduleAdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_schedule, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ScheduleAdapterViewHolder, position: Int) {
        holder.itemView.apply {
            val taskName = findViewById<TextView>(R.id.taskName)
            val description = findViewById<TextView>(R.id.description)
            val year = findViewById<TextView>(R.id.year)

            taskName.text = differ.currentList[position].taskName
            description.text = differ.currentList[position].description
            year.text = differ.currentList[position].year
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
