package com.heb.dtn.login

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.heb.dtn.R
import com.inmotionsoftware.imsflow.FlowDialogFragment
import kotlinx.android.synthetic.main.fragment_login_create_account_selection.view.*

//
// Created by Khuong Huynh on 10/13/17.
//

class LoginCreateAccountSelectionFragment : FlowDialogFragment<Unit, LoginCreateAccountSelectionFragment.Result>() {

    sealed class Result {
        class Login: Result()
        class SignUp: Result()
        class AsGuest: Result()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.fragment_login_create_account_selection, container, false)

        val listener = View.OnClickListener {
            when (it.id) {
                R.id.loginButton -> this.finish(result = Result.Login())
                R.id.createAccountButton -> this.finish(result = Result.SignUp())
                R.id.continueAsGuestButton -> this.finish(result = Result.AsGuest())
            }
        }

        view.loginButton.setOnClickListener(listener)
        view.createAccountButton.setOnClickListener(listener)
        view.continueAsGuestButton.setOnClickListener(listener)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(this.activity, this.theme)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return dialog
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)

        // Treat swipe down/cancel action to continue as guest
        this.finish(result = Result.AsGuest())
    }

    override fun finish(result: Result) {
        when (result) {
            is Result.Login, is Result.SignUp -> this.dismiss()
        }
        super.finish(result)
    }

}
