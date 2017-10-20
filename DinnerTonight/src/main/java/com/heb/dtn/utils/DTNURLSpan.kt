package com.heb.dtn.utils

import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import com.heb.dtn.extensions.launchUrl

/**
 * A Span containing a URL hyperlink which when clicked opens a WebView.
 */
class DTNURLSpan (url: String?, private val color: Int? = null) : URLSpan(url) {

    override fun onClick(widget: View?) {
        widget?.context?.launchUrl(url = url)
    }

    override fun updateDrawState(ds: TextPaint?) {
        this.color?.let { ds?.linkColor = it}
        super.updateDrawState(ds)
    }
}