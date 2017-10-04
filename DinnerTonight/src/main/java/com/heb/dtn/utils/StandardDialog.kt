package com.heb.dtn.utils

import android.app.DialogFragment
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.heb.dtn.R
import com.heb.dtn.extensions.hide
import com.heb.dtn.extensions.show
import kotlinx.android.synthetic.main.dialog_default_view.*
import java.awt.font.TextAttribute

typealias OnInitCustomView = (view: View) -> Unit

class StandardDialog : DialogFragment {

    constructor() : super()
    constructor(@LayoutRes customLayoutId: Int, onInitCustomView: OnInitCustomView, actions: Array<String>?, preferredActionIndex: Int?) : super() {
        this.customLayoutId = customLayoutId
        this.onInitCustomView = onInitCustomView
        this.actions = actions
        this.preferredActionIndex = preferredActionIndex
    }

    constructor(title: String?, message: String?, attributedMessage: String?, actions: Array<String>?, preferredActionIndex: Int?) : super() {
        this.title = title
        this.message = message
        this.attributedMessage = attributedMessage
        this.actions = actions
        this.preferredActionIndex = preferredActionIndex
    }

    interface Listener {
        fun onActionClicked(idx: Int)
    }

    // TODO: newInstance companion object to keep track of state
    var listener: Listener? = null
    private var title: String? = null
    private var message: String? = null
    private var attributedMessage: String? = null
    private var actions: Array<String>? = null
    private var preferredActionIndex: Int? = null
    private var customLayoutId: Int? = null
    private var onInitCustomView: OnInitCustomView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.RxApp_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.dialog_default_view, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        this.isCancelable = false

        val actions = this.actions
        if (actions != null && actions.isNotEmpty()) {
            val boldIdx = this.preferredActionIndex
            actions.forEachIndexed { index, action ->
                val button = View.inflate(view?.context, R.layout.dialog_button, null) as Button
                button.text = action
                if (boldIdx != null && boldIdx == index) {
                    button.setTypeface(button.typeface, Typeface.BOLD)
                }

                button.setOnClickListener({ this.listener?.onActionClicked(index) })

                this.dialogButtonsContainer.addView(button)
            }
            this.dialogButtonsContainer.show()
        }

        if (this.customLayoutId == null) {
            this.dialogTitle?.text = this.title
            this.dialogMessage?.text = this.message
            this.dialogAttributedMessage?.text = this.attributedMessage

            this.title?.let { this.dialogTitle.show() }
            this.message?.let { this.dialogMessage.show() }
            this.attributedMessage?.let { this.dialogAttributedMessage.show() }
        }

        this.customLayoutId?.let {
            val inflater = this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val customView = inflater.inflate(customLayoutId!!, customLayoutContainer, true)
            this.customLayoutContainer?. let { this.customLayoutContainer.show() }
            this.standardDialogContainer?. let { this.standardDialogContainer.hide() }
            this.onInitCustomView?.invoke(customView!!)
        }
    }

}
