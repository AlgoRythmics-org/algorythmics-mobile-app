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

        val initialColor = Color.parseColor("#1BDBBE")
        val comparisonColor = Color.parseColor("#FF5733")
        val foundColor = Color.parseColor("#FFA500")
        val swappedColor = Color.parseColor("#FFFF00") // Új szín a csere jelzésére

        // Az aktuális elem háttérszíne az összehasonlítás, találat vagy csere alapján
        val backgroundColor = when {
            item.isCurrentlyCompared -> comparisonColor
            item.isFound -> foundColor
            item.needsColorUpdate -> swappedColor // Ha színezést igényel
            else -> initialColor
        }

        holder.itemView.setBackgroundColor(backgroundColor)
    }


    fun updateList(newList: List<ListUiItem>) {
        submitList(newList)
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