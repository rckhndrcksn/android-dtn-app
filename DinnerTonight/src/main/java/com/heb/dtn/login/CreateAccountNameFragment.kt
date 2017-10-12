package com.heb.dtn.login

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import kotlinx.android.synthetic.main.fragment_create_account_name.*

/**
 * Fragment that allows the user to enter their first and last name and first screen in the create account flow.
 */
class CreateAccountNameFragment : BaseFormFlowDialogFragment<Unit, CreateAccountNameFragment.Result>() {

    data class Result(val firstName: String, val lastName: String = "")

    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_account_name, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.firstNameEditText.addTextChangedListener(this)
        this.lastNameEditText.addTextChangedListener(this)

        this.nextButton.setOnClickListener {
            this.finish(result = Result(firstName = this.firstNameEditText.text.toString(), lastName = this.lastNameEditText.text.toString()))
        }
        this.nextButton.isEnabled = false
    }

    override fun afterTextChanged(s: Editable?) {
        this.nextButton.isEnabled = !this.firstNameEditText.text.isNullOrBlank() && !this.lastNameEditText.text.isNullOrBlank()
    }

    override fun finish(result: Result) {
        this.dismiss()
        super.finish(result = result)
    }

}
