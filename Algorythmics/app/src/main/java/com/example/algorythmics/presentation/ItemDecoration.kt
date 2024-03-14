package com.example.algorythmics.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration : RecyclerView.ItemDecoration() {
    private val spacing = 16 // A kívánt térköz értéke pixelekben

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // A RecyclerView elején és végén nincs térköz
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = spacing // Térköz bal oldalon
        }
    }
}
