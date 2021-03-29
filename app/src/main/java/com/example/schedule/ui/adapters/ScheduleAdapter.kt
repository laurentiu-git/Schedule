package com.example.schedule.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.databinding.ItemScheduleBinding
import com.example.schedule.ui.adapters.ScheduleAdapter.ScheduleAdapterViewHolder.Companion.onAddItemClickListener
import com.example.schedule.ui.adapters.ScheduleAdapter.ScheduleAdapterViewHolder.Companion.onItemClickListener
import javax.inject.Inject

class ScheduleAdapter @Inject constructor() : RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterViewHolder>() {

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnAddClickListener(listener: (ScheduleInfo) -> Unit) {
        onAddItemClickListener = listener
    }

    class ScheduleAdapterViewHolder constructor(private val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {

        lateinit var taskListAdapter: TaskListAdapter

        fun bind(schedule: ScheduleInfo) {
            binding.location.text = schedule.location
            binding.startTime.text = schedule.startTime
            binding.endTime.text = schedule.endTime
            binding.taskName.text = schedule.taskName

            binding.showDescription.setOnClickListener {
                clickListener(binding.addTask.isVisible)
            }

            binding.itemId.setOnClickListener {
                clickListener(binding.addTask.isVisible)
            }

            binding.extraTaskName.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    onItemClickListener?.let {
                        it(adapterPosition)
                    }
                }
            }
            taskListAdapter = TaskListAdapter()
            taskListAdapter.differ.submitList(schedule.taskList)
            binding.listView.apply {
                adapter = taskListAdapter
                layoutManager = LinearLayoutManager(binding.listView.context)
            }

            binding.addTask.setOnClickListener {
                val text = binding.extraTaskName.text.toString()
                val addTaskList = mutableListOf(text)
                val newScheduleList = schedule.taskList + addTaskList
                taskListAdapter.differ.submitList(newScheduleList)
                binding.listView.adapter?.notifyItemChanged(newScheduleList.size)
                binding.extraTaskName.text.clear()
                schedule.taskList = newScheduleList as MutableList<String>
                onAddItemClickListener?.let {
                    it(schedule)
                }
            }
        }

        companion object {
            var onItemClickListener: ((Int) -> Unit)? = null
            var onAddItemClickListener: ((ScheduleInfo) -> Unit)? = null

            fun from(parent: ViewGroup): ScheduleAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemScheduleBinding.inflate(layoutInflater, parent, false)

                return ScheduleAdapterViewHolder(binding)
            }
        }

        private fun clickListener(descriptionIsVisible: Boolean) {
            if (descriptionIsVisible) {
                binding.addTask.visibility = View.GONE
                binding.extraTaskName.visibility = View.GONE
                binding.listView.visibility = View.GONE
            } else {
                binding.addTask.visibility = View.VISIBLE
                binding.extraTaskName.visibility = View.VISIBLE
                binding.listView.visibility = View.VISIBLE
            }

            onItemClickListener?.let {
                it(-1)
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
