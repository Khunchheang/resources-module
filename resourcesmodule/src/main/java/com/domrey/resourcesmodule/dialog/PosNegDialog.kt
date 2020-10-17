package com.domrey.resourcesmodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.base.fragment.BaseDialogFragment
import com.domrey.resourcesmodule.databinding.DialogPositiveNegativeBinding

open class PosNegDialog :
    BaseDialogFragment<DialogPositiveNegativeBinding>(R.layout.dialog_positive_negative),
    View.OnClickListener {

    private var positiveClickListener: (() -> Unit)? = null
    private var negativeClickListener: (() -> Unit)? = null
    private var textBtnPos: String? = null
    private var textBtnNeg: String? = null
    private var message: String? = null
    private var title: String? = null

    fun newInstance(
        posClickListener: (() -> Unit)? = null,
        negClickListener: (() -> Unit)? = null,
        textBtnPos: String? = null,
        textBtnNeg: String? = null,
        message: String? = null,
        title: String? = null
    ) {
        this.positiveClickListener = posClickListener
        this.negativeClickListener = negClickListener
        this.textBtnPos = textBtnPos
        this.textBtnNeg = textBtnNeg
        this.message = message
        this.title = title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message = message
        title?.let { binding.title = it }
        binding.textBtnPos = textBtnPos ?: getString(R.string.yes)
        textBtnNeg?.let { binding.textBtnNeg = textBtnNeg }

        binding.btnNegative.setOnClickListener(this)
        binding.btnPositive.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        positiveClickListener = null
        negativeClickListener = null
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnPositive) positiveClickListener?.invoke()
        else if (view.id == R.id.btnNegative) negativeClickListener?.invoke()
        dialog?.dismiss()
    }

    open class Builder(private val fragmentManager: FragmentManager) {

        private var posClickListener: (() -> Unit)? = null
        private var negClickListener: (() -> Unit)? = null
        private var textBtnPos: String? = null
        private var textBtnNeg: String? = null
        private var message: String? = null
        private var title: String? = null

        fun setMessage(msg: String) = apply { this.message = msg }

        fun setOnPosClickListener(positiveClickListener: () -> Unit) =
            apply { this.posClickListener = positiveClickListener }

        fun setOnNegClickListener(ngClick: (() -> Unit)) =
            apply { this.negClickListener = ngClick }

        fun setTextBtnPos(textBtnPos: String) = apply { this.textBtnPos = textBtnPos }

        fun setWithBtnNeg(textBtnNeg: String) = apply { this.textBtnNeg = textBtnNeg }

        fun setTitle(txt: String) = apply { this.title = txt }

        open fun show(isCancelable: Boolean = true) {
            val dialog = build()
            dialog.isCancelable = isCancelable
            synchronized(this) {
                fragmentManager.let {
                    it.executePendingTransactions()
                    val fragmentTransaction = it.beginTransaction()
                    if (!dialog.isAdded) fragmentTransaction.add(dialog, dialog.tag)
                    fragmentTransaction.commitAllowingStateLoss()
                }
            }
        }

        fun build(): PosNegDialog {
            val dialog = PosNegDialog()
            dialog.newInstance(
                posClickListener,
                negClickListener,
                textBtnPos,
                textBtnNeg,
                message,
                title
            )
            clearBuilderReferences()
            return dialog
        }

        open fun buildPosNegSingleton(): PosNegSingletonDialog {
            val dialog = PosNegSingletonDialog()
            dialog.newInstance(
                posClickListener,
                negClickListener,
                textBtnPos,
                textBtnNeg,
                message,
                title
            )
            clearBuilderReferences()
            return dialog
        }

        private fun clearBuilderReferences() {
            posClickListener = null
            negClickListener = null
        }
    }
}