package com.heb.dtn.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.ColorUtils
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.Button
import com.heb.dtn.R
import com.heb.dtn.utils.VersionUtils

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

    @SuppressLint("NewApi")
    private fun createBackground(): Drawable {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorControlHighlight, typedValue, true)
        val pressedColor = typedValue.data
        val strokeWidth = context.resources.getDimensionPixelOffset(R.dimen.rounded_button_stroke_width)

        if (VersionUtils.isLollipopOrGreater()) {
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
        } else {
            val statesArray = arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_pressed),
                    intArrayOf(android.R.attr.state_pressed),
                    intArrayOf(android.R.attr.state_empty))
            val fillColorsArray = intArrayOf(disabledColor, fillColor, pressedColor,  fillColor)
            val borderColorsArray = intArrayOf(disabledColor, borderColor, pressedColor, borderColor)

            val stateListDrawable = StateListDrawable()
            var index = statesArray.size - 1
            while (index >= 0) {
                val states = statesArray[index]
                val gradientDrawable = createGradientDrawable()

                gradientDrawable.setColor(fillColorsArray[index])
                if (borderColor != fillColor) {
                    gradientDrawable.setStroke(strokeWidth, borderColorsArray[index])
                }

                stateListDrawable.addState(states, gradientDrawable)
                index--
            }

            return stateListDrawable
        }
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