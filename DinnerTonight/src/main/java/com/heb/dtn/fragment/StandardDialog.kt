package com.heb.dtn.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import kotlinx.android.synthetic.main.dialog_default_layout.view.*

/**
 * Created by jcarbo on 10/19/17.
 */
class StandardDialog : DTNBaseDialogFragment() {
    override var dialogTheme: Int = R.style.BaseCustomDialogFragment

    private val layoutRes: Int = R.layout.dialog_default_layout

    private var titleString: String? = null
    private var messageString: String? = null

    private var customContentLayoutResId: Int? = null
    private var customContentView: View? = null

    private var positiveButtonString: String? = null
    private var negativeButtonString: String? = null
    private var positiveButtonListener: View.OnClickListener? = null
    private var negativeButtonListener: View.OnClickListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(layoutRes, container, false)

        customContentLayoutResId?.let {
            customContentView = inflater.inflate(it, container, false)
            layout.contentPanel.addView(customContentView)
            layout.contentPanel.visibility = View.GONE
        }

        titleString?.let {
            layout.title.text = it
        }

        messageString?.let {
            layout.message.text = messageString
            layout.customPanel.visibility = View.GONE
        }

        if (positiveButtonString == null && negativeButtonString == null) {
            layout.buttonPanel.visibility = View.GONE
        }

        positiveButtonString?.let {
            layout.positiveButton.text = it
            layout.positiveButton.visibility = View.VISIBLE
        }

        negativeButtonString?.let {
            layout.negativeButton.text = it
            layout.negativeButton.visibility = View.VISIBLE
        }

        this.positiveButtonListener?.let {
            layout.positiveButton.setOnClickListener(it)
        }

        this.negativeButtonListener?.let {
            layout.negativeButton.setOnClickListener(it)
        }

        return layout
    }

    class Builder {
        private var context: Context
        private var fragment: StandardDialog

        constructor(context: Context) {
            this.context = context
            this.fragment = StandardDialog()
        }

        constructor(context: Context, style: Int) {
            this.context = context
            this.fragment = StandardDialog()
            this.fragment.dialogTheme = style
        }

        fun setTitleResId(@StringRes resId: Int):Builder = setTitle(context.getString(resId))

        fun setTitle(title: String):Builder {
            this.fragment.titleString = title
            return this
        }

        fun setMessageResId(resId: Int):Builder = setMessage(context.getString(resId))

        fun setMessage(message: String):Builder {
            this.fragment.messageString = message
            return this
        }

        fun setCustomContentRes(@LayoutRes layoutRes: Int):Builder {
            this.fragment.customContentView = null
            this.fragment.customContentLayoutResId = layoutRes
            return this
        }

        fun setCustomContent(content: View):Builder {
            this.fragment.customContentLayoutResId = 0
            this.fragment.customContentView = content
            return this
        }

        fun setPositiveButton(buttonTextResId: Int, listener: View.OnClickListener):Builder =
                setPositiveButton(context.getString(buttonTextResId), listener)

        fun setPositiveButton(buttonText: String, listener: View.OnClickListener):Builder {
            this.fragment.positiveButtonString = buttonText
            this.fragment.positiveButtonListener = listener
            return this
        }

        fun setNegativeButton(buttonTextResId: Int, listener: View.OnClickListener):Builder =
                setNegativeButton(context.getString(buttonTextResId), listener)

        fun setNegativeButton(buttonText: String, listener: View.OnClickListener):Builder {
            this.fragment.negativeButtonString = buttonText
            this.fragment.negativeButtonListener = listener
            return this
        }

        fun build(): StandardDialog = this.fragment

        fun show(manager: FragmentManager, tag: String) = fragment.show(manager, tag)

        fun show(transaction: FragmentTransaction, tag: String) = fragment.show(transaction, tag)
    }
}