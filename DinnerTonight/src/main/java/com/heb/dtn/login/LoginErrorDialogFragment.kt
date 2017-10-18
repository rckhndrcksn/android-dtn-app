package com.heb.dtn.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFlowDialogFragment
import kotlinx.android.synthetic.main.login_error_dialog.*

/**
 * DialogFragment used to display an error at login.
 */
class LoginErrorDialogFragment: BaseFlowDialogFragment<Unit, Unit>() {
    override val dialogTheme: Int = R.style.LoginErrorStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.login_error_dialog, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ok.setOnClickListener { this.finish(Unit) }
    }

    override fun finish(result: Unit) {
        this.dismiss()
        super.finish(result = result)
    }
}