package com.dragnell.a2p_shop.view.dialog

import CustomToast
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.DialogAddOrFixBinding
import com.dragnell.a2p_shop.model.Role
import com.dragnell.a2p_shop.model.req_api.RoleAddReq
import com.dragnell.a2p_shop.model.req_api.RoleFixReq
import com.dragnell.a2p_shop.model.res_api.RoleFixRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOrFixRoleDialog(context: Context, val role: Role?,private val onSucess: () -> Unit) :
    BottomSheetDialog(context, R.style.CustomDialogStyle) {
    private val mbinding: DialogAddOrFixBinding = DialogAddOrFixBinding.inflate(layoutInflater)

    init {
        setContentView(mbinding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (role == null) {
            mbinding.btnApply.setOnClickListener {
                callAddRole()
            }
        } else {
            mbinding.nameDialog.text="Chỉnh sửa Role"
            mbinding.title.setText(role.title)
            mbinding.describe.setText(role.description)
            val selectedList: List<String> = role.permissions
            if (selectedList.contains(mbinding.checkbox1.getText().toString())) {
                mbinding.checkbox1.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox2.getText().toString())) {
                mbinding.checkbox2.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox3.getText().toString())) {
                mbinding.checkbox3.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox4.getText().toString())) {
                mbinding.checkbox4.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox5.getText().toString())) {
                mbinding.checkbox5.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox6.getText().toString())) {
                mbinding.checkbox6.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox7.getText().toString())) {
                mbinding.checkbox7.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox8.getText().toString())) {
                mbinding.checkbox8.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox9.getText().toString())) {
                mbinding.checkbox9.setChecked(true)
            }
            if (selectedList.contains(mbinding.checkbox1o.getText().toString())) {
                mbinding.checkbox1o.setChecked(true)
            }
            mbinding.btnApply.setOnClickListener {
                callEditRole()
            }
        }
    }

    private fun callEditRole() {
        val selectedList: MutableList<String> = ArrayList()

        if (mbinding.checkbox1.isChecked) selectedList.add(
            mbinding.checkbox1.getText().toString()
        )
        if (mbinding.checkbox2.isChecked) selectedList.add(
            mbinding.checkbox2.getText().toString()
        )
        if (mbinding.checkbox3.isChecked) selectedList.add(
            mbinding.checkbox3.getText().toString()
        )
        if (mbinding.checkbox4.isChecked) selectedList.add(
            mbinding.checkbox4.getText().toString()
        )
        if (mbinding.checkbox5.isChecked) selectedList.add(
            mbinding.checkbox5.getText().toString()
        )
        if (mbinding.checkbox6.isChecked) selectedList.add(
            mbinding.checkbox6.getText().toString()
        )
        if (mbinding.checkbox7.isChecked) selectedList.add(
            mbinding.checkbox7.getText().toString()
        )
        if (mbinding.checkbox8.isChecked) selectedList.add(
            mbinding.checkbox8.getText().toString()
        )
        if (mbinding.checkbox9.isChecked) selectedList.add(
            mbinding.checkbox9.getText().toString()
        )
        if (mbinding.checkbox1o.isChecked) selectedList.add(
            mbinding.checkbox1o.getText().toString()
        )

        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.adminFixRole(
            RoleFixReq(
                role!!.id,
                mbinding.title.text.toString().trim(),
                mbinding.describe.text.toString().trim(),
                selectedList
            ), "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<RoleFixRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RoleFixRes>, response: Response<RoleFixRes>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "Role updated successfully") {
                        CustomToast.showToast(
                            context, "Sửa vai trò thành công!", CustomToast.ToastType.SUCCESS
                        )
                        dismiss()
                        onSucess()
                    } else {
                        CustomToast.showToast(
                            context, "Chưa thể cập nhật bây giờ!", CustomToast.ToastType.INFO
                        )
                    }
                }
                Log.i("FixRole", response.body().toString())
            }

            override fun onFailure(call: Call<RoleFixRes>, t: Throwable) {
                dismiss()
                onSucess()
                CustomToast.showToast(
                    context, "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("FixRole", "onFailure: " + t.message)
            }

        })
    }

    private fun callAddRole() {
        val selectedList: MutableList<String> = ArrayList()

        if (mbinding.checkbox1.isChecked) selectedList.add(
            mbinding.checkbox1.getText().toString()
        )
        if (mbinding.checkbox2.isChecked) selectedList.add(
            mbinding.checkbox2.getText().toString()
        )
        if (mbinding.checkbox3.isChecked) selectedList.add(
            mbinding.checkbox3.getText().toString()
        )
        if (mbinding.checkbox4.isChecked) selectedList.add(
            mbinding.checkbox4.getText().toString()
        )
        if (mbinding.checkbox5.isChecked) selectedList.add(
            mbinding.checkbox5.getText().toString()
        )
        if (mbinding.checkbox6.isChecked) selectedList.add(
            mbinding.checkbox6.getText().toString()
        )
        if (mbinding.checkbox7.isChecked) selectedList.add(
            mbinding.checkbox7.getText().toString()
        )
        if (mbinding.checkbox8.isChecked) selectedList.add(
            mbinding.checkbox8.getText().toString()
        )
        if (mbinding.checkbox9.isChecked) selectedList.add(
            mbinding.checkbox9.getText().toString()
        )
        if (mbinding.checkbox1o.isChecked) selectedList.add(
            mbinding.checkbox1o.getText().toString()
        )

        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.adminAddRole(
            RoleAddReq(
                mbinding.title.text.toString().trim(),
                mbinding.describe.text.toString().trim(),
                selectedList,
                false
            ), "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<RoleFixRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RoleFixRes>, response: Response<RoleFixRes>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "Role created successfully!") {
                        CustomToast.showToast(
                            context, "Tạo vai trò thành công!", CustomToast.ToastType.SUCCESS
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

            override fun onFailure(call: Call<RoleFixRes>, t: Throwable) {
                CustomToast.showToast(
                    context, "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("AddRole", "onFailure: " + t.message)
            }

        })
    }
}
