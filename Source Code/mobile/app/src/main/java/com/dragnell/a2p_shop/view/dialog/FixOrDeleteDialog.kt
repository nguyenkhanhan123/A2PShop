package com.dragnell.a2p_shop.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.DialogFixOrDeleteBinding


class FixOrDeleteDialog(
    context: Context,
    private val onFix: () -> Unit,
    private val onDelete: () -> Unit
) : Dialog(context, R.style.CustomDialogStyle) {
    private val mbinding: DialogFixOrDeleteBinding =
        DialogFixOrDeleteBinding.inflate(layoutInflater)

    init {
        setContentView(mbinding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mbinding.fix.setOnClickListener {
            onFix()
            dismiss()
        }
        mbinding.delete.setOnClickListener {
            onDelete()
            dismiss()
        }
    }

}
