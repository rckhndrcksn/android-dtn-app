//
// Created by Khuong Huynh on 2/16/17.
//

package com.heb.dtn.utils;

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class KeyboardUtils {

    companion object {
        fun dismissKeyboard(activity: Activity) {
            val view = activity.currentFocus ?: View(activity)
            KeyboardUtils.dismissKeyboard(context = activity, view = view)
        }

        fun dismissKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun requestFocus(context: Context, editText: EditText) {
            editText.requestFocus()
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

}
