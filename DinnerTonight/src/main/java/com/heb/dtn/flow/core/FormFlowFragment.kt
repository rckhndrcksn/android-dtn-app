package com.heb.dtn.flow.core

import android.text.Editable
import android.text.TextWatcher

//
// Created by Khuong Huynh on 8/29/17.
//

abstract class FormFlowFragment<ARGS, RETURN> : BaseFlowFragment<ARGS, RETURN>(), TextWatcher {

    //
    // TextWatcher
    //
    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

}
