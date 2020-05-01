package com.moskovkin.outlinetextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.cos
import kotlin.math.sin

class OutlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private val mainTextColor: Int = currentTextColor
    private var outlineColor: Int = currentTextColor
    private var outlineWidth: Float = 0F
    private var isDrawing: Boolean = false

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.OutlineTextView)

            outlineColor = typedArray.getColor(R.styleable.OutlineTextView_outlineColor, currentTextColor)
            outlineWidth = typedArray.getDimension(R.styleable.OutlineTextView_outlineWidth, 0F)

            setPadding(paddingStart + outlineWidth.toInt(), paddingTop + outlineWidth.toInt(), paddingEnd + outlineWidth.toInt(), paddingBottom + outlineWidth.toInt())

            typedArray.recycle()
        }
    }

    override fun invalidate() {
        if (isDrawing) return
        super.invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        if (outlineWidth > 0 && outlineColor != mainTextColor) {
            isDrawing = true
            val p: Paint = paint.apply {
                style = Paint.Style.STROKE
                strokeWidth = outlineWidth
            }

            setTextColor(outlineColor)

            canvas?.let {
                it.save()

                for (i in 0 until 360 step 15) {
                    it.translate(
                        (outlineWidth * cos(i.toDouble())).toFloat(),
                        (outlineWidth * sin(i.toDouble())).toFloat()
                    )
                    super.onDraw(canvas)
                    it.translate(
                        (-outlineWidth * cos(i.toDouble())).toFloat(),
                        (-outlineWidth * sin(i.toDouble())).toFloat()
                    )
                }

                it.restore()
            }

            p.style = Paint.Style.FILL

            setTextColor(mainTextColor)

            super.onDraw(canvas)

            isDrawing = false;
        } else {
            super.onDraw(canvas)
        }
    }
}