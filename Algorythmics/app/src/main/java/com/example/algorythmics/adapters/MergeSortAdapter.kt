package com.example.algorythmics.adapters

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.presentation.ListUiItem

class MergeSortAdapter : ListAdapter<ListUiItem, MergeSortAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sorted_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.textView.text = item.value.toString()
        holder.textView.gravity = Gravity.CENTER
        val height = calculateHeight(item.value)
        holder.textView.layoutParams.height = height

        val backgroundColor = when {
            item.isCurrentlyCompared -> Color.parseColor("#FF5733") // Red color for compared items
            item.isSorted -> Color.parseColor("#A9A9A9") // Grey color for sorted items
            item.isFound -> Color.parseColor("#FFA500") // Orange color for found items
            else -> Color.parseColor("#63A46C") // Default color
        }

        holder.itemView.setBackgroundColor(backgroundColor)
    }

    private fun calculateHeight(value: Int): Int {
        val minHeight = 100
        val maxHeight = 400
        return minHeight + (value * (maxHeight - minHeight) / 150)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ListUiItem>() {
            override fun areItemsTheSame(oldItem: ListUiItem, newItem: ListUiItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListUiItem, newItem: ListUiItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
