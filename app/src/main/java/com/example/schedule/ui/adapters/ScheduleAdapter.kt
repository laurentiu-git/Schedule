package com.example.schedule.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.databinding.ItemScheduleBinding
import javax.inject.Inject

class ScheduleAdapter @Inject constructor() : RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterViewHolder>() {

    class ScheduleAdapterViewHolder constructor(private val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: ScheduleInfo) {
            binding.location.text = schedule.location
            binding.startTime.text = schedule.startTime
            binding.endTime.text = schedule.endTime
            binding.firstTask.text = schedule.firstTask
            binding.taskName.text = schedule.taskName

            binding.showDescription.setOnClickListener {
                clickListener(binding.firstTask.isVisible)
            }

            binding.itemId.setOnClickListener {
                clickListener(binding.firstTask.isVisible)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ScheduleAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemScheduleBinding.inflate(layoutInflater, parent, false)

                return ScheduleAdapterViewHolder(binding)
            }
        }
        private fun clickListener(descriptionIsVisible: Boolean) {
            if (descriptionIsVisible) {
                binding.firstTask.visibility = View.GONE
                binding.addTask.visibility = View.GONE
            } else {
                binding.firstTask.visibility = View.VISIBLE
                binding.addTask.visibility = View.VISIBLE
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ScheduleInfo>() {

        override fun areItemsTheSame(oldItem: ScheduleInfo, newItem: ScheduleInfo): Boolean {
            return oldItem.primaryKey == newItem.primaryKey
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
