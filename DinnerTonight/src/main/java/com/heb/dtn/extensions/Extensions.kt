package com.heb.dtn.extensions

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.heb.dtn.R
import com.heb.dtn.app.AppProxy
import com.heb.dtn.utils.KeyboardUtils

//
// Float
//

fun Float.metersToMiles() : Double {
    return this * 0.000621371
}

//
// Double
//

fun Double.roundTo(digits: Int) : Double {
    val divisor = Math.pow(10.0, digits.toDouble())

    return Math.round((this * divisor)) / divisor
}

//
// Int
//
fun Int.toDP(context: Context) : Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

//
// Resources
//
fun Resources.getDoubleFromString(@StringRes stringId: Int): Double = this.getString(stringId).toDouble()

//
// View
//
fun View.getCompatColor(@ColorRes colorId: Int): Int = ContextCompat.getColor(this.context, colorId)

inline fun View.snack(
        @StringRes stringId: Int,
        @ColorRes textColorId: Int = R.color.snackBarText,
        @DimenRes textSizeId: Int = R.dimen.text_medium,
        length: Int = Snackbar.LENGTH_LONG,
        init: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, this.context.getText(stringId), length)
    snack.setTextColor(textColorId)
    snack.setTextSize(textSizeId)
    snack.init()
    snack.show()
}

inline fun View.snack(
        text: String,
        @ColorRes textColorId: Int = R.color.snackBarText,
        @DimenRes textSizeId: Int = R.dimen.text_medium,
        length: Int = Snackbar.LENGTH_LONG,
        init: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, text, length)
    snack.setTextColor(textColorId)
    snack.setTextSize(textSizeId)
    snack.init()
    snack.show()
}

fun View.hide(remove: Boolean = false) {
    this.visibility = if (remove) View.GONE else View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

//
// Snackbar
//
fun Snackbar.setTextSize(@DimenRes dimenId: Int) {
    this.view.resources.getDimensionPixelSize(R.dimen.form_padding)
    (this.view.findViewById(android.support.design.R.id.snackbar_text) as TextView).setTextSize(TypedValue.COMPLEX_UNIT_PX, this.view.resources.getDimension(dimenId))
}

fun Snackbar.setTextColor(@ColorRes colorId: Int) {
    (this.view.findViewById(android.support.design.R.id.snackbar_text) as TextView).setTextColor(ContextCompat.getColor(AppProxy.proxy, colorId))
}

fun Snackbar.action(@StringRes stringId: Int, @ColorRes colorId: Int = R.color.snackBarActionText, clickListener: (View) -> Unit) {
    this.setAction(stringId, clickListener)
    this.setActionTextColor(ContextCompat.getColor(AppProxy.proxy, colorId))
}

fun Snackbar.action(text: String, @ColorRes colorId: Int = R.color.snackBarActionText, clickListener: (View) -> Unit) {
    this.setAction(text, clickListener)
    this.setActionTextColor(ContextCompat.getColor(AppProxy.proxy, colorId))
}

fun Context.launchUrl(url: String) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
    builder.setStartAnimations(this, R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    builder.setExitAnimations(this, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    builder.setShowTitle(true)

    val uri = if (url.endsWith(".PDF", true)) {
        Uri.parse("http://docs.google.com/gview?embedded=true&url=$url")
    } else {
        Uri.parse(url)
    }

    builder.build().launchUrl(this, uri)
}
