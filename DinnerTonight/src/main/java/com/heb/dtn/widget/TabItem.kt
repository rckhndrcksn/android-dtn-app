package com.heb.dtn.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.design.R
import android.support.v7.content.res.AppCompatResources
import android.util.AttributeSet
import android.view.View

/**
 * TabItem is a special 'view' which allows you to declare tab items for a [DTNTabLayout]
 * within a layout. This view is not actually added to TabLayout, it is just a dummy which allows
 * setting of a tab items's text, icon and custom layout. See TabLayout for more information on how
 * to use it.
 *
 * @attr ref android.support.design.R.styleable#TabItem_android_icon
 * @attr ref android.support.design.R.styleable#TabItem_android_text
 * @attr ref android.support.design.R.styleable#TabItem_android_layout
 *
 * @see DTNTabLayout
 */
class TabItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    internal val mText: CharSequence
    internal val mIcon: Drawable?
    internal val mCustomLayout: Int

    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.TabItem)
        mText = ta.getText(R.styleable.TabItem_android_text)
        mIcon = getDrawable(context, ta, R.styleable.TabItem_android_icon)
        mCustomLayout = ta.getResourceId(R.styleable.TabItem_android_layout, 0)
        ta.recycle()
    }

    private fun getDrawable(context: Context, ta: TypedArray, index: Int): Drawable? {
        if (ta.hasValue(index)) {
            val resourceId = ta.getResourceId(index, 0)
            if (resourceId != 0) {
                return AppCompatResources.getDrawable(context, resourceId)
            }
        }
        return ta.getDrawable(index)
    }

}
