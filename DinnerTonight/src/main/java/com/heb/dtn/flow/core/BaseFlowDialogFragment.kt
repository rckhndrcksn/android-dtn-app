package com.heb.dtn.flow.core

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import com.heb.dtn.R
import com.heb.dtn.utils.KeyboardUtils
import com.inmotionsoftware.imsflow.FlowDialogFragment

//
// Created by Khuong Huynh on 10/13/17.
//

abstract class BaseFlowDialogFragment<ARGS, RETURN> : FlowDialogFragment<ARGS, RETURN>() {

    abstract val dialogTheme: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(DialogFragment.STYLE_NORMAL, dialogTheme)
    }

    override fun onStart() {
        super.onStart()

        this.dialog?.let {
            it.window.setBackgroundDrawableResource(R.drawable.dialog_background)
            it.setCanceledOnTouchOutside(false)
            it.window.setWindowAnimations(R.style.LoginCreateDialogAnimations)
        }
    }

    override fun onPause() {
        KeyboardUtils.dismissKeyboard(this.activity)
        super.onPause()
    }

}

abstract class BaseFormFlowDialogFragment<ARGS, RETURN> : BaseFlowDialogFragment<ARGS, RETURN>(), TextWatcher {

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}
