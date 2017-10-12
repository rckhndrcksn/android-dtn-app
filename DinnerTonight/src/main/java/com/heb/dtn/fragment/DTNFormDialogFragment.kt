package com.heb.dtn.fragment

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by jcarbo on 10/11/17.
 */
abstract class DTNFormDialogFragment(): DTNBaseDialogFragment(), TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}