package com.heb.dtn.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R

/**
 * DialogFragment used for the user to login.
 */
class LoginDialogFragment: DTNBaseDialogFragment() {
    override val dialogTheme: Int = R.style.LoginDialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.login_dialog, container, false)
}