package com.heb.dtn.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFlowDialogFragment
import kotlinx.android.synthetic.main.fragment_create_account_complete.*

/**
 * Fragment to inform the user he has successfully created an account.
 */

class CreateAccountSuccessFragment: BaseFlowDialogFragment<String, Unit>() {

    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_account_complete, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.okButton.setOnClickListener {
            this.finish(result = Unit)
        }
    }

}
