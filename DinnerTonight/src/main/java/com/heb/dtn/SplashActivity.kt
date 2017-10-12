package com.heb.dtn;

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.heb.dtn.login.LoginOrSignUpActivity
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*

class SplashActivity : AppCompatActivity() {
    private companion object {
        const val SPLASH_DELAY_INTERVAL: Long = DateUtils.SECOND_IN_MILLIS * 2   // 2 secs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lock orientation to Portrait Mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

        this.setContentView(R.layout.splash_activity)

        if (supportFragmentManager.findFragmentByTag(BottomSheetDialogFragment.TAG) == null) {
            Handler().postDelayed({
                BottomSheetDialogFragment().show(supportFragmentManager, BottomSheetDialogFragment.TAG)
            }, SPLASH_DELAY_INTERVAL)
        }
    }

    class BottomSheetDialogFragment() : DialogFragment() {
        companion object {
            const val TAG: String = "BottomSheetDialog"
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view  = inflater.inflate(R.layout.bottom_sheet_dialog, container, false)

            val listener = View.OnClickListener {
                val intent = Intent(this.activity, LoginOrSignUpActivity::class.java)

                // Set the correct action type for the activity to start the correct flow
                if (it.id == R.id.createAccount) {
                    intent.action = LoginOrSignUpActivity.CREATE_ACCOUNT_ACTION
                }
                this.activity.startActivity(intent)
            }

            view.login.setOnClickListener(listener)
            view.createAccount.setOnClickListener(listener)

            view.guest.setOnClickListener {
                this.activity.startActivity(Intent(this.activity, HomeActivity::class.java))
                this.activity.finish()
            }

            return view
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = BottomSheetDialog(activity, theme)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            return dialog
        }

        override fun onCancel(dialog: DialogInterface?) {
            super.onCancel(dialog)
            this.activity.finish()
        }
    }
}
