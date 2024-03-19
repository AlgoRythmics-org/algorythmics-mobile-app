package com.example.algorythmics.fragments.course

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

class SortedListAdapter : ListAdapter<ListUiItem, SortedListAdapter.ViewHolder>(DiffCallback) {

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


        val initialColor = Color.parseColor("#1BDBBE")
        val comparisonColor = Color.parseColor("#FF5733")
        val foundColor = Color.parseColor("#FFA500")

        // Az aktuális elem háttérszíne az összehasonlítás vagy találat alapján
        val backgroundColor = when {
            item.isCurrentlyCompared -> comparisonColor
            item.isFound -> foundColor // Ha már megtaláltuk az elemet
            else -> initialColor
        }

        holder.itemView.setBackgroundColor(backgroundColor)
    }

    private fun calculateHeight(value: Int): Int {
        val maxHeight = 60
        // Csak akkor alkalmazd a számítást, ha az érték meghaladja a maximális magasságot
        return if (value > maxHeight) {
            maxHeight
        } else {
            // A magasság beállítása valamilyen érték függvényében
            // Például: 50 pixel + (érték * 10 pixel)
            20 + (value * 10)
        }
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