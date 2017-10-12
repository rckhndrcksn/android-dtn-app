package com.heb.dtn.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.fragment.DTNBaseDialogFragment

/**
 * Fragment to inform the user he has successfully created an account.
 */
class CreateAccountCompleteDialogFragment(): DTNBaseDialogFragment()  {
    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.create_account_complete_fragment, container, false)
}