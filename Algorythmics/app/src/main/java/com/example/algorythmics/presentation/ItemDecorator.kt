package com.example.algorythmics.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(private val verticalSpace: Int, private val horizontalSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        // Egyenletes távolság hozzáadása az elemek köré
        outRect.top = verticalSpace
        outRect.bottom = verticalSpace
        outRect.left = horizontalSpace
        outRect.right = horizontalSpace
    }
}
