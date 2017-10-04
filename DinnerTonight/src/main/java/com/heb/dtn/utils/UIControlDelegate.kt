package com.heb.dtn.utils

import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.heb.dtn.R
import com.heb.dtn.foundation.promise.android.then
//import com.heb.pharmacy.app.ErrorView
import com.heb.dtn.extensions.show
//import com.heb.service.pharmacy.api.PharmacyServiceError
//import com.heb.service.pharmacy.api.PharmacyServiceErrorCode
import com.inmotionsoftware.promise.Promise

interface UIControlDelegate {
    var controlActivity: AppCompatActivity?
    var progressBar: ProgressBar?
    var fragmentContainerId: Int
    var fragment: Fragment?

    fun showActivityIndicator(): ProgressBar?
    fun showDialog(title: String? = null, message: String? = null, attributedMessage: String? = null, actions: Array<String>? = null, preferredActionIndex: Int? = null): Promise<Int>
    fun showDialog(@LayoutRes layoutId: Int, onInitCustomView: OnInitCustomView, actions: Array<String>? = null, preferredActionIndex: Int? = null): Promise<Int>
    fun showErrorScreen(error: Throwable)
    fun showErrorDialog(error: Throwable?): Promise<Unit>
}

open class UIControlDelegation : UIControlDelegate {

    companion object {
        val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG_UIControlDelegation"
        val ERROR_FRAGMENT_TAG = "ERROR_FRAGMENT_TAG_UIControlDelegation"
    }
    override var controlActivity: AppCompatActivity? = null
    override var progressBar: ProgressBar? = null
    override var fragmentContainerId: Int = -1
    override var fragment: Fragment? = null

    override fun showDialog(title: String?, message: String?, attributedMessage: String?, actions: Array<String>?, preferredActionIndex: Int?): Promise<Int> {
        return Promise{ resolve, reject ->
            val dialog = StandardDialog(
                    title = title
                    , message = message
                    , attributedMessage = attributedMessage
                    , actions = actions ?: kotlin.arrayOf("Ok"),
                    preferredActionIndex = preferredActionIndex
            )
            this.showAndResolveDialog(dialog, resolve)
        }
    }

    override fun showDialog(@LayoutRes layoutId: Int, onInitCustomView: OnInitCustomView, actions: Array<String>?, preferredActionIndex: Int?): Promise<Int> {
        return Promise { resolve, reject ->
            val dialog = StandardDialog(
                    customLayoutId = layoutId
                    , onInitCustomView = onInitCustomView
                    , actions = actions ?: kotlin.arrayOf("Ok")
                    , preferredActionIndex = preferredActionIndex ?: 0
            )
            this.showAndResolveDialog(dialog, resolve)
        }
    }

    private fun showAndResolveDialog(dialog: StandardDialog, resolve: (Int) -> Unit) {
        dialog.listener = object: StandardDialog.Listener {
            override fun onActionClicked(idx: Int) {
                resolve(idx)
                dialog.dismiss()
            }
        }
        dialog.show(this.controlActivity?.fragmentManager, DIALOG_FRAGMENT_TAG)
    }

    override fun showErrorScreen(error: Throwable) {
//        val fragmentContainer = this.fragment?.view?.findViewById(R.id.fragmentBaseFlowContainer) as? FrameLayout
//        fragmentContainer?.let {
//            val errorView = ErrorView(this.controlActivity)
//            errorView.error = error
//            it.addView(errorView)
//        }
    }

    override fun showErrorDialog(error: Throwable?): Promise<Unit> {
//        val serviceError = error as? PharmacyServiceError
//        val title: String
//        val message: String
//
//        if (serviceError != null) {
//            when(serviceError.code) {
//                PharmacyServiceErrorCode.NETWORK_CONNECTION_LOST, PharmacyServiceErrorCode.NOT_CONNECTED_TO_INTERNET -> {
//                    title = this.controlActivity?.getString(R.string.error_lost_connection_title) ?: ""
//                    message = this.controlActivity?.getString(R.string.error_lost_connection_message) ?: ""
//                }
//                else -> {
//                    title = this.controlActivity?.getString(R.string.error_no_server_response_title) ?: ""
//                    message = this.controlActivity?.getString(R.string.error_no_server_response_message) ?: ""
//                }
//            }
//        } else {
//            title = this.controlActivity?.getString(R.string.error) ?: ""
//            message = this.controlActivity?.getString(R.string.error_general_message) ?: ""
//        }
//
//        return this.showDialog(title = title, message = message, actions = kotlin.arrayOf("Ok")).then{ _ -> }
        return Promise<Unit>(value=Unit)
    }

    override fun showActivityIndicator(): ProgressBar? {
        this.progressBar?.show()
        return this.progressBar
    }
}
