/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 * Original Author: https://github.com/pnc/IconButton
 */
package co.sampingan.android.dynamic_ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatButton
import android.util.AttributeSet
import com.sampingan.agentapp.dynamic_ui.R


/**
 * Use this to show a button with icon
 */
class DrawableButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    private var drawableWidth = 0
    private var drawablePosition: DrawablePositions? = null
    var iconPadding = 0
        get() {
            requestLayout()
            return field
        }

    // Cached to prevent allocation during onLayout
    private var bounds: Rect? = null

    private enum class DrawablePositions {
        NONE, LEFT_AND_RIGHT, LEFT, RIGHT
    }

    init {
        bounds = Rect()
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        // Slight contortion to prevent allocating in onLayout
        if (null == bounds) {
            bounds = Rect()
        }
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.DrawableButton)
        val paddingId =
            typedArray.getDimensionPixelSize(R.styleable.DrawableButton_drawablePadding, 0)
        iconPadding = paddingId
        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val textPaint: Paint = paint
        val word = "$text"
        textPaint.getTextBounds(word, 0, word.length, bounds)

        val textWidth: Int = bounds?.width() ?: 0
        val factor = if (drawablePosition == DrawablePositions.LEFT_AND_RIGHT) 2 else 1
        val contentWidth = drawableWidth + iconPadding * factor + textWidth
        val horizontalPadding = (width / 2.0 - contentWidth / 2.0).toInt()
        compoundDrawablePadding = -horizontalPadding + iconPadding

        when (drawablePosition) {
            DrawablePositions.LEFT -> setPadding(horizontalPadding, paddingTop, 0, paddingBottom)
            DrawablePositions.RIGHT -> setPadding(0, paddingTop, horizontalPadding, paddingBottom)
            DrawablePositions.LEFT_AND_RIGHT -> setPadding(
                horizontalPadding,
                paddingTop,
                horizontalPadding,
                paddingBottom
            )
            else -> setPadding(0, paddingTop, 0, paddingBottom)
        }
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
        if (left != null && right != null) {
            drawableWidth = left.intrinsicWidth + right.intrinsicWidth
            drawablePosition = DrawablePositions.LEFT_AND_RIGHT
        } else if (left != null) {
            drawableWidth = left.intrinsicWidth
            drawablePosition = DrawablePositions.LEFT
        } else if (right != null) {
            drawableWidth = right.intrinsicWidth
            drawablePosition = DrawablePositions.RIGHT
        } else {
            drawablePosition = DrawablePositions.NONE
        }
        requestLayout()
    }
}