package com.heb.dtn.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.Button
import com.heb.dtn.R

/**
 * Button that is drawn either as a rectangle with rounded edges or as an oval or circle.
 */
class DTNRoundedButton(context: Context, attrs: AttributeSet?) : Button(context, attrs) {
    private enum class Shape(val value: Int) {
        Rectangle(0),
        Oval(1)
    }

    private var borderColor: Int = Color.WHITE
    private var fillColor: Int = Color.WHITE
    private var disabledColor: Int = Color.WHITE
    private var buttonShape: Int = Shape.Rectangle.value
    private var cornerRadius: Float = 0F

    init {
        initialize(context = context, attrs = attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
        cornerRadius = context.resources.getDimension(R.dimen.rounded_button_corner_radius)
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorButtonNormal, typedValue, true)
        val defaultColor = typedValue.data

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DTNRoundedButton)
        borderColor = typedArray.getColor(R.styleable.DTNRoundedButton_borderColor, defaultColor)
        fillColor = typedArray.getColor(R.styleable.DTNRoundedButton_fillColor, defaultColor)

        val defaultDisabledColor = ColorUtils.setAlphaComponent(borderColor, 127)
        disabledColor = typedArray.getColor(R.styleable.DTNRoundedButton_disabledColor, defaultDisabledColor)
        buttonShape = typedArray.getInt(R.styleable.DTNRoundedButton_shape, buttonShape)
        typedArray.recycle()

        background = createBackground()
    }

    private fun createBackground(): Drawable {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorControlHighlight, typedValue, true)
        val pressedColor = typedValue.data
        val strokeWidth = context.resources.getDimensionPixelOffset(R.dimen.rounded_button_stroke_width)

        val statesArray = arrayOf(intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf())
        val fillColorsArray = intArrayOf(disabledColor, fillColor, pressedColor, fillColor)
        val fillInStateList = ColorStateList(statesArray, fillColorsArray)
        val borderColorsArray = intArrayOf(disabledColor, borderColor, pressedColor, borderColor)
        val borderStateList = ColorStateList(statesArray, borderColorsArray)

        val gradientDrawable = createGradientDrawable()
        gradientDrawable.color = fillInStateList

        if (borderColor != fillColor) {
            gradientDrawable.setStroke(strokeWidth, borderStateList)
        }

        return RippleDrawable(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_pressed)),
                intArrayOf(pressedColor)), gradientDrawable, gradientDrawable)
    }

    private fun createGradientDrawable(): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = if (buttonShape == Shape.Rectangle.value) {
            GradientDrawable.RECTANGLE
        } else {
            GradientDrawable.OVAL
        }
        gradientDrawable.cornerRadius = cornerRadius

        return gradientDrawable
    }
}