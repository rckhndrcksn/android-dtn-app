package com.heb.dtn.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.fragment.DTNBaseDialogFragment
import kotlinx.android.synthetic.main.password_sent_dialog.*

/**
 * DialogFragment for displaying a password reset
 */
class PasswordSentDialogFragment: DTNBaseDialogFragment() {
    override val dialogTheme: Int = R.style.PasswordRecoveryDialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.password_sent_dialog, container, false)
}