package com.example.irisrouter.ui

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import kotlin.math.min

class CirclePaintable(
    @ColorInt val color: Int
): Paintable {
    val paint = Paint().apply {
        color = this@CirclePaintable.color
    }

    override fun paint(canvas: Canvas) {
        val cx = canvas.width / 2.0F
        val cy = canvas.height / 2.0F
        val radius = min(cx, cy)
        canvas.drawCircle(cx, cy, radius, paint)
    }
}