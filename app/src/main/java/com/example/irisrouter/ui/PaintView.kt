package com.example.irisrouter.ui

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.annotation.Dimension

class PaintView(
    context: Context,
    @Dimension(unit = Dimension.PX) width: Int,
    @Dimension(unit = Dimension.PX) height: Int,
    val paintable: Paintable
): View(context) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let(paintable::paint)
    }
}