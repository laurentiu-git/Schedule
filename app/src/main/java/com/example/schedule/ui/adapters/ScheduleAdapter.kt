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
import com.example.schedule.databinding.ItemScheduleBinding
import javax.inject.Inject

class ScheduleAdapter @Inject constructor() : RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterViewHolder>() {

     class ScheduleAdapterViewHolder constructor(private val biding: ItemScheduleBinding) : RecyclerView.ViewHolder(biding.root) {

         fun bind(schedule: ScheduleInfo) {
            biding.year.text = schedule.hour
            biding.description.text = schedule.description
            biding.taskName.text = schedule.taskName
        }

         companion object {
             fun from(parent: ViewGroup): ScheduleAdapterViewHolder {
                 val layoutInflater = LayoutInflater.from(parent.context)
                 val binding = ItemScheduleBinding.inflate(layoutInflater, parent, false)

                 return  ScheduleAdapterViewHolder(binding)
             }
         }
    }

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
        return ScheduleAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScheduleAdapterViewHolder, position: Int) {
      val schedule = differ.currentList[position]
        holder.bind(schedule)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
