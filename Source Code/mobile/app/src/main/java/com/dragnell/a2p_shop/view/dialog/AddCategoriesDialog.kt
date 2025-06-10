package com.dragnell.a2p_shop.view.dialog

import CustomToast
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.DialogCategoriesBinding
import com.dragnell.a2p_shop.model.Category
import com.dragnell.a2p_shop.model.req_api.CategoryAddReq
import com.dragnell.a2p_shop.model.req_api.CategoryFixReq
import com.dragnell.a2p_shop.model.res_api.CategoryAddRes
import com.dragnell.a2p_shop.model.res_api.CategoryFixRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCategoriesDialog(
    context: Context,
    val parentId: String?,
    val category: Category?,
    private val onSucess: () -> Unit,
    private val onClick: (String) -> Unit
) : BottomSheetDialog(context, R.style.CustomDialogStyle) {
    private val mbinding: DialogCategoriesBinding = DialogCategoriesBinding.inflate(layoutInflater)

    init {
        setContentView(mbinding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mbinding.icWed.setOnClickListener {
            onClick(mbinding.title.text.toString().trim())
        }
        if (category == null) {
            mbinding.btnApply.setOnClickListener {
                callAddCategory()
            }
        } else {
            mbinding.nameDialog.text = "Chỉnh sửa Category"
            mbinding.title.setText(category.title)
            mbinding.describe.setText(category.description)
            mbinding.thumbnail.setText(category.thumbnail)
            mbinding.btnApply.setOnClickListener {
                callFixCategory()
            }
        }
    }

    private fun callFixCategory() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.fixCategories(
            CategoryFixReq(
                category!!.id,
                mbinding.title.text.toString().trim(),
                mbinding.describe.text.toString().trim(),
                mbinding.thumbnail.text.toString().trim(),
                category.position
            ), "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<CategoryFixRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CategoryFixRes>, response: Response<CategoryFixRes>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "Cập nhật thành công!") {
                        CustomToast.showToast(
                            context, "Cập nhật thành công!", CustomToast.ToastType.SUCCESS
                        )
                        dismiss()
                        onSucess()
                    } else {
                        CustomToast.showToast(
                            context, "Chưa thể cập nhật bây giờ!", CustomToast.ToastType.INFO
                        )
                    }
                }
                Log.i("AddCategory", response.body().toString())
            }

            override fun onFailure(call: Call<CategoryFixRes>, t: Throwable) {
                CustomToast.showToast(
                    context, "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("AddCategory", "onFailure: " + t.message)
            }

        })
    }

    private fun callAddCategory() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.addCategories(
            CategoryAddReq(
                mbinding.title.text.toString().trim(),
                parentId.toString().trim(),
                mbinding.describe.text.toString().trim(),
                mbinding.thumbnail.text.toString().trim(),
                "active"
            ), "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<CategoryAddRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CategoryAddRes>, response: Response<CategoryAddRes>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "ahihihihihihi") {
                        CustomToast.showToast(
                            context, "Tạo Category thành công", CustomToast.ToastType.SUCCESS
                        )
                        dismiss()
                        onSucess()
                    } else {
                        CustomToast.showToast(
                            context, "Chưa thể cập nhật bây giờ!", CustomToast.ToastType.INFO
                        )
                    }
                }
                Log.i("AddCategory", response.body().toString())
            }

            override fun onFailure(call: Call<CategoryAddRes>, t: Throwable) {
                CustomToast.showToast(
                    context, "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("AddCategory", "onFailure: " + t.message)
            }

        })
    }
}
