package com.heb.dtn.flow.core

import android.support.annotation.StringRes
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.heb.dtn.R
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.recoverp
import com.heb.dtn.foundation.promise.android.thenp
import com.heb.dtn.app.AppProxy
import com.heb.dtn.utils.UIControlDelegate
import com.heb.dtn.utils.UIControlDelegation
//import com.heb.service.pharmacy.api.PharmacyServiceError
//import com.heb.service.pharmacy.api.PharmacyServiceErrorCode
import com.inmotionsoftware.imsflow.*
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 8/31/17.
//

open class Retry<T>(val times: Int = 1, val value: T) {
    fun again(): Retry<T>? {
        if (this.times <= 0) return null
        return Retry(times = this.times - 1, value = this.value)
    }
}

class RetryNoValue(times: Int = 1) : Retry<Unit>(times = times, value = Unit)

class AuthenticateTransition<STATE : FlowState>(val state: STATE, val args: Any?)

class AuthenticateContext<STATE : FlowState>(
        val complete: AuthenticateTransition<STATE>?
        , val back: AuthenticateTransition<STATE>?
        , val cancel: AuthenticateTransition<STATE>?
        , val redirect: AuthenticateTransition<STATE>? = null) {

    constructor(redirect: AuthenticateTransition<STATE>?) : this(complete = null, back = null, cancel = null, redirect = redirect)

}

abstract class BaseFlowController<STATE : FlowState, ARGS, RETURN>(private val context: AppCompatActivity, private val fragmentContainerView: Int)
    : BaseFragmentFlowController<STATE, ARGS, RETURN>(context = context, fragmentContainerView = fragmentContainerView)
        , UIControlDelegate by UIControlDelegation() {

    override fun initialize() {
        super.initialize()

        if (this.context is UIControlDelegate) {
            this.controlActivity = this.context.controlActivity
            this.progressBar = this.context.progressBar
            this.fragmentContainerId = this.context.fragmentContainerId
        }

    }

    protected fun getString(@StringRes resId: Int): String = this.context.getString(resId)
    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = this.context.getString(resId, formatArgs)

    override fun configureFragmentTransaction(fragmentTransaction: FragmentTransaction) {
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left
                                                , R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

//    protected fun reauthenticate(): FlowPromise<Boolean> {
//        val proxy = AppProxy.proxy
//        val reauthEmail = proxy.accountManager().userProfile?.email
//
//        fun authenticate(): FlowPromise<Boolean> {
//            return this.showDialog(title = this.getString(R.string.error_session_timeout_title)
//                                    , message = this.getString(R.string.error_session_timeout_message))
//                        .thenp {
//                            proxy.flow.login(context = this.context
//                                                , fragmentContainerView = this.fragmentContainerView
//                                                , reauthenticateWithEmail = reauthEmail)
//                        }
//        }
//
//        return proxy.accountManager().reauthenticate()
//                .thenp{ success ->
//                    if (success) {
//                        FlowPromise(FlowResult.Complete(result = true))
//                    } else {
//                        authenticate()
//                    }
//                }
//                .recoverp{
//                    authenticate()
//                }
//    }

//    protected fun onAuthenticateWithContext(state: STATE, context: AuthenticateContext<STATE>) {
//        if (context.redirect != null) {
//            this.transitionAsync(from = state, to = context.redirect.state, with = context.redirect.args)
//        } else {
//            val back = context.back!!
//            val cancel = context.cancel!!
//            val complete = context.complete!!
//
//            this.reauthenticate()
//                .back { this.transition(from = state, to = back.state, with = back.args) }
//                .cancel { this.transition(from = state, to = cancel.state, with = cancel.args) }
//                .complete { this.transition(from = state, to = complete.state, with = complete.args) }
//                .catch { this.transition(from = state, to = cancel.state, with = cancel.args) }
//        }
//    }

    //protected fun isUnauthorizedError(error: Throwable): Boolean {
    //    val serviceError = error as? PharmacyServiceError ?: return false
    //    return serviceError.code == PharmacyServiceErrorCode.UNAUTHORIZED || serviceError.code == PharmacyServiceErrorCode.UNAUTHORIZED_LOCAL_SESSION
    //}

}
