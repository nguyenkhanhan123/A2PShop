package com.dragnell.a2p_shop.view.dialog

import CustomToast
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dragnell.a2p_shop.App
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.DialogAccountsBinding
import com.dragnell.a2p_shop.databinding.DialogFilterBinding
import com.dragnell.a2p_shop.model.Account
import com.dragnell.a2p_shop.model.req_api.AccountAddReq
import com.dragnell.a2p_shop.model.req_api.AccountFixReq
import com.dragnell.a2p_shop.model.res_api.AccountsFixRes
import com.dragnell.a2p_shop.model.res_api.RoleFixRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilterDialog(context: Context, private val onClick: (String) -> Unit) :
    BottomSheetDialog(context, R.style.CustomDialogStyle) {
    private val mbinding: DialogFilterBinding = DialogFilterBinding.inflate(layoutInflater)

    init {
        setContentView(mbinding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mbinding.btnApply.setOnClickListener {
            val sortType = when (mbinding.radioGroup.checkedRadioButtonId) {
                R.id.rb_price_low_to_high -> "PRICE_ASC"
                R.id.rb_price_high_to_low -> "PRICE_DESC"
                R.id.rb_a_z -> "AZ"
                R.id.rb_z_a -> "ZA"
                else -> null
            }

            if (sortType == null) {
                CustomToast.showToast(context, "Vui lòng chọn một tiêu chí sắp xếp!", CustomToast.ToastType.INFO)
            } else {
                onClick(sortType)
                dismiss()
            }
        }
    }

}
