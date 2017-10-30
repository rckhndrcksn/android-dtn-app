package com.heb.dtn.widget

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.toolbar.view.*

/**
 * Created by jcarbo on 10/25/17.
 */
class DTNToolbar : Toolbar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun setTitle(resId: Int) {
        setTitle(context.getString(resId))
    }

    override fun setTitle(title: CharSequence?) {
        titleTextView.text = title
    }

//    override fun setTitleMarginStart(margin: Int) {
//        super.setTitleMarginStart(margin)
//    }

    override fun setTitleTextColor(color: Int) {
        titleTextView.setTextColor(color)
    }

//    override fun setTitleMarginTop(margin: Int) {
//        super.setTitleMarginTop(margin)
//    }

//    override fun setTitleMargin(start: Int, top: Int, end: Int, bottom: Int) {
//        super.setTitleMargin(start, top, end, bottom)
//    }

    override fun setTitleTextAppearance(context: Context?, resId: Int) {
        titleTextView.setTextAppearance(context, resId)
    }

//    override fun setTitleMarginBottom(margin: Int) {
//        super.setTitleMarginBottom(margin)
//    }

    override fun isTitleTruncated(): Boolean {
        val titleLayout = titleTextView.layout ?: return false

        val lineCount = titleLayout.lineCount
        var idx = -1
        while (++idx <= lineCount) {
            if (titleLayout.getEllipsisCount(idx) > 0) {
                return true
            }
        }
        return false
    }

//    override fun getTitleMarginStart(): Int {
//        return super.getTitleMarginStart()
//    }

//    override fun setTitleMarginEnd(margin: Int) {
//        super.setTitleMarginEnd(margin)
//    }

    override fun getTitle(): CharSequence = titleTextView.text


    override fun setSubtitle(resId: Int) {
        setSubtitle(context.getString(resId))
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        if (subtitle == null) {
            if (subtitleTextView.visibility == View.VISIBLE) {
                subtitleTextView.visibility = View.GONE
            }
        } else if (subtitleTextView.visibility != View.VISIBLE) {
            subtitleTextView.visibility = View.VISIBLE
        }
        subtitleTextView.text = subtitle
    }

    override fun setSubtitleTextColor(color: Int) {
        subtitleTextView.setTextColor(color)
    }

    override fun getSubtitle(): CharSequence = subtitleTextView.text


    override fun setSubtitleTextAppearance(context: Context?, resId: Int) {
        subtitleTextView.setTextAppearance(context, resId)
    }

    private fun shouldLayout(view: View): Boolean = view.parent === this && view.visibility != View.GONE

}