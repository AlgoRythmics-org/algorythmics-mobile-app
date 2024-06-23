package com.example.algorythmics.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.presentation.ListUiItem


class SelectionSortListAdapter : ListAdapter<ListUiItem, SelectionSortListAdapter.ViewHolder>(
    DiffCallback
) {

    private var smallestItemIndex: Int = -1

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

        val initialColor = Color.parseColor("#63A46C")
        val smallestItemColor = Color.RED
        val sortedColor = Color.parseColor("#A9A9A9")

        val backgroundColor = when {
            item.isSorted -> sortedColor
            position == smallestItemIndex -> smallestItemColor
            item.isCurrentlyCompared -> smallestItemColor
            else -> initialColor
        }

        holder.itemView.setBackgroundColor(backgroundColor)
    }

    private fun calculateHeight(value: Int): Int {

        val minHeight = 100
        val maxHeight = 400

        return minHeight + (value * (maxHeight - minHeight) / 100)
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
