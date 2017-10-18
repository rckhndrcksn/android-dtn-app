package com.heb.dtn.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFlowDialogFragment
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import com.heb.dtn.fragment.DTNBaseDialogFragment
import com.inmotionsoftware.imsflow.FlowDialogFragment
import kotlinx.android.synthetic.main.password_sent_dialog.*

/**
 * DialogFragment for displaying a password reset
 */
class PasswordSentDialogFragment: BaseFlowDialogFragment<String, Unit>() {
    override val dialogTheme: Int = R.style.PasswordRecoveryDialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.password_sent_dialog, container, false)

    override fun flowWillRun(args: String) {
        ok.setOnClickListener { this.finish(Unit) }
        passwordResetEmail.text = args
    }

    override fun finish(result: Unit) {
        this.dismiss()
        super.finish(result = result)
    }
}