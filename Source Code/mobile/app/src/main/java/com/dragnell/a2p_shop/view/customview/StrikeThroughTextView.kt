package com.dragnell.a2p_shop.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class StrikeThroughTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = currentTextColor
        paint.strokeWidth = textSize / 12

        val middleY = height / 2f
        val startX = paddingLeft.toFloat()
        val stopX = width - paddingRight.toFloat()

        canvas.drawLine(startX, middleY, stopX, middleY, paint)
    }
}
