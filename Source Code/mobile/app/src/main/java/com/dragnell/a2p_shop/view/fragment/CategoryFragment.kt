package com.dragnell.a2p_shop.view.fragment

import CustomToast
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.CategoryBinding
import com.dragnell.a2p_shop.model.Category
import com.dragnell.a2p_shop.model.req_api.CategoryDeleteReq
import com.dragnell.a2p_shop.model.res_api.CategoryDeleteRes
import com.dragnell.a2p_shop.view.act.ProductActivity
import com.dragnell.a2p_shop.view.adapter.CategoryAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.view.dialog.AddCategoriesDialog
import com.dragnell.a2p_shop.view.dialog.FixOrDeleteDialog
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : BaseFragment<CategoryBinding, CommonViewModel>() {

    private var rootCategoriesName: String = ""

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var list: List<Category>

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        mbinding.add.setOnClickListener {
            val dialog =
                AddCategoriesDialog(requireContext(), rootCategoriesName, null, onSucess = {
                    callGetCategory()
                }, onClick = {
                    val url = "https://www.google.com/search?tbm=isch&q=" + Uri.encode(it)
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    startActivity(intent)
                })
            dialog.behavior.isHideable = false
            dialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            dialog.show()
        }
        callGetCategory()
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): CategoryBinding {
        return CategoryBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = CategoryFragment::class.java.name
    }

    private fun callGetCategory() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getCategories(
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>, response: Response<List<Category>>
            ) {
                if (response.body() != null) {
                    list = response.body()!!
                    val rootCategories = list.filter { it.parentId.isEmpty() }
                    categoryAdapter = CategoryAdapter(
                        rootCategories,
                        requireContext(),
                        onClickFolder = { id, title,slug ->
                            rootCategoriesName = id
                            val categories = list.filter { category -> category.parentId == id }
                            if (categories.isEmpty()) {
                                val intent = Intent(requireContext(), ProductActivity::class.java)
                                intent.putExtra("id", id)
                                intent.putExtra("slug", slug)
                                intent.putExtra("title", title)
                                startActivity(intent)
                            } else {
                                categoryAdapter.updateData(categories)
                            }
                        },
                        onLongClickFolder = {
                            val dialog = FixOrDeleteDialog(requireContext(), onFix = {
                                callFixCategory(it)
                            }, onDelete = {
                                callDeleteCategory(it)
                            })
                            dialog.show()
                        })
                    mbinding.rv.adapter = categoryAdapter
                }
                Log.e("KQ", response.body().toString())

            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

    private fun callFixCategory(category: Category) {
        val dialog =
            AddCategoriesDialog(requireContext(), rootCategoriesName, category, onSucess = {
                callGetCategory()
            }, onClick = {
                val url = "https://www.google.com/search?tbm=isch&q=" + Uri.encode(it)
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                startActivity(intent)
            })
        dialog.behavior.isHideable = false
        dialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        dialog.show()
    }

    private fun callDeleteCategory(category: Category) {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.deleteCategories(
            CategoryDeleteReq(category.id), "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<CategoryDeleteRes> {
            override fun onResponse(
                call: Call<CategoryDeleteRes>, response: Response<CategoryDeleteRes>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "Xóa thành công") {
                        CustomToast.showToast(
                            requireContext(), "Xóa thành công!", CustomToast.ToastType.SUCCESS
                        )
                        callGetCategory()
                    } else {
                        CustomToast.showToast(
                            requireContext(),
                            "Chưa thể cập nhật bây giờ!",
                            CustomToast.ToastType.INFO
                        )
                    }
                }
                Log.e("KQ", response.body().toString())

            }

            override fun onFailure(call: Call<CategoryDeleteRes>, t: Throwable) {
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }
}