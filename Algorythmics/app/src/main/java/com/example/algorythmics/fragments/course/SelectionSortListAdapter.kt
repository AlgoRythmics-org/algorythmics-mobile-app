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
        init {
            // Állíts be egy kis padding-et az elem aljához
            val layoutParams = itemView.layoutParams as RecyclerView.LayoutParams
            layoutParams.bottomMargin = 8 // Válassz megfelelő padding-et vagy margin-t
            itemView.layoutParams = layoutParams
        }
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

        // Az aktuális elem háttérszíne az összehasonlítás alapján
        val backgroundColor = if (item.isCurrentlyCompared) {
            smallestItemColor // Az az elem, ami éppen az aktuális legkisebb, piros legyen
        } else {
            initialColor // Egyébként a többi elem maradjon az eredeti háttérszínén
        }

        holder.itemView.setBackgroundColor(backgroundColor)

        // Az animáció meghívása minden elemre
        animateCup(holder.itemView)
    }

    // A függvényt módosítani kell, hogy a megfelelő nézetet kapja meg paraméterként
    private fun animateCup(view: View) {
        val animation = TranslateAnimation(0f, 0f, 0f, -100f)
        animation.duration = 500

        val cupImageView = view.findViewById<View>(R.id.rv_container)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                cupImageView.translationY = 0f
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        cupImageView.startAnimation(animation)
    }


    fun updateSmallestItemIndex(index: Int) {
        smallestItemIndex = index
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