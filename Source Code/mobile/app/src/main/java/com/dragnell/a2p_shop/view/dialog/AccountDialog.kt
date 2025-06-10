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


class AccountDialog(context: Context, val account: Account?, private val onSucess: () -> Unit) :
    BottomSheetDialog(context, R.style.CustomDialogStyle) {
    private val mbinding: DialogAccountsBinding = DialogAccountsBinding.inflate(layoutInflater)

    init {
        setContentView(mbinding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val titles = App.getInstance().storage.listRole.map { it.title }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, titles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mbinding.roleSpinner.adapter = adapter

        mbinding.roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedRoleId = App.getInstance().storage.listRole[position].id
                mbinding.role.tag = selectedRoleId
                val selectedRole = parent.getItemAtPosition(position).toString()
                mbinding.role.text = selectedRole
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        mbinding.btOpen.setOnClickListener {
            mbinding.roleSpinner.performClick()
        }

        if (account == null) {
            mbinding.btnApply.setOnClickListener {
                callAddAccount()
            }
        } else {
            mbinding.nameDialog.text = "Chỉnh sửa Account"
            mbinding.fullName.setText(account.fullName)
            mbinding.Email.setText(account.email)
            mbinding.phone.setText(account.phone)
            val roleIndex = App.getInstance().storage.listRole.indexOfFirst { it.id == account.roleId }
            if (roleIndex != -1) {
                mbinding.roleSpinner.setSelection(roleIndex)
                mbinding.role.text = titles[roleIndex]
            }
            mbinding.ln2.visibility=View.GONE
            mbinding.ln1.visibility=View.GONE
            mbinding.btnApply.setOnClickListener {
                callEditAccount()
            }
        }
    }

    private fun callEditAccount() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.adminFixAccount(
            AccountFixReq(
                account!!.id,
                mbinding.fullName.text.toString().trim(),
                mbinding.Email.text.toString().trim(),
                mbinding.phone.text.toString().trim(),
                mbinding.role.tag.toString().trim()
            ), "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<AccountsFixRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<AccountsFixRes>, response: Response<AccountsFixRes>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "cap nhap thanh cong!") {
                        CustomToast.showToast(
                            context, "Sửa account thành công!", CustomToast.ToastType.SUCCESS
                        )
                        dismiss()
                        onSucess()
                    } else {
                        CustomToast.showToast(
                            context, "Chưa thể cập nhật bây giờ!", CustomToast.ToastType.INFO
                        )
                    }
                }
                Log.i("AddRole", response.body().toString())
            }

            override fun onFailure(call: Call<AccountsFixRes>, t: Throwable) {
                CustomToast.showToast(
                    context, "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("AddRole", "onFailure: " + t.message)
            }

        })
    }

    private fun callAddAccount() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.adminAddAccount(
            AccountAddReq(
                mbinding.fullName.text.toString().trim(),
                mbinding.Email.text.toString().trim(),
                mbinding.Password.text.toString().trim(),
                mbinding.phone.text.toString().trim(),
                mbinding.role.tag.toString().trim()
            ), "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<AccountsFixRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<AccountsFixRes>, response: Response<AccountsFixRes>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "Tạo tài khoản thành công") {
                        CustomToast.showToast(
                            context, "Tạo tài khoản thành công", CustomToast.ToastType.SUCCESS
                        )
                        dismiss()
                        onSucess()
                    } else {
                        CustomToast.showToast(
                            context, "Chưa thể cập nhật bây giờ!", CustomToast.ToastType.INFO
                        )
                    }
                }
                Log.i("AddRole", response.body().toString())
            }

            override fun onFailure(call: Call<AccountsFixRes>, t: Throwable) {
                CustomToast.showToast(
                    context, "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("AddRole", "onFailure: " + t.message)
            }

        })
    }
}
