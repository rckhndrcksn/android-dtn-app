package com.heb.dtn.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.heb.dtn.R

/**
 * Abstract DialogFragment that applies a specified theme and custom background with rounded edges.
 */
abstract class DTNBaseDialogFragment: DialogFragment() {
    abstract val dialogTheme: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, dialogTheme)
    }

    override fun onStart() {
        super.onStart()

        dialog?.let {
            it.window.setBackgroundDrawableResource(R.drawable.dialog_background)
            it.setCanceledOnTouchOutside(false)
            it.window.setWindowAnimations(R.style.LoginCreateDialogAnimations)
        }
    }
}