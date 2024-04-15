package com.example.algorythmics.fragments.course

import android.animation.ObjectAnimator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Color
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.presentation.ListUiItem
import com.example.algorythmics.retrofit.models.SelectionSortInfo


class SelectionSortListAdapter : ListAdapter<ListUiItem, SelectionSortListAdapter.ViewHolder>(DiffCallback) {

    private var smallestItemIndex: Int = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionSortListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sorted_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.textView.text = item.value.toString()
        holder.textView.gravity = Gravity.CENTER

        val initialColor = Color.parseColor("#1BDBBE")
        val smallestItemColor = Color.RED
        val replacedItemColor = Color.GRAY

        // Determine the background color based on the state of the item
        val backgroundColor = when {
            position == smallestItemIndex -> smallestItemColor // Smallest item is red
            item.isCurrentlyCompared -> initialColor // Other currently compared items stay green
            else -> initialColor // Default color for other items
        }

        holder.itemView.setBackgroundColor(backgroundColor)
    }

    // Update the index of the smallest item and notify adapter of the change
    fun updateSmallestItemIndex(index: Int) {
        smallestItemIndex = index
        notifyDataSetChanged()
    }

    fun resetSmallestItemIndex() {
        smallestItemIndex = -1 // Reset the index when the smallest item is replaced
        notifyDataSetChanged()
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
