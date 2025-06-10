package com.dragnell.a2p_shop.view.act

import CustomToast
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.SearchBinding
import com.dragnell.a2p_shop.model.Product
import com.dragnell.a2p_shop.model.res_api.SearchRes
import com.dragnell.a2p_shop.view.adapter.ProductAdapter
import com.dragnell.a2p_shop.view.adapter.RecentSearchAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.view.dialog.FilterDialog
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search : BaseActivity<SearchBinding, CommonViewModel>() {
    private var productList: List<Product> = emptyList()

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        mbinding.filter.setOnClickListener {
            if (productList.isNotEmpty()) {
                val dialog = FilterDialog(this, onClick = { sortType ->
                    val sortedList = when (sortType) {
                        "PRICE_ASC" -> productList.sortedBy { it.price }
                        "PRICE_DESC" -> productList.sortedByDescending { it.price }
                        "AZ" -> productList.sortedBy { it.title.lowercase() }
                        "ZA" -> productList.sortedByDescending { it.title.lowercase() }
                        else -> productList
                    }

                    mbinding.rvProduct.adapter = ProductAdapter(
                        sortedList, this@Search, onClickFolder = { i ->
                            val intent = Intent(this@Search, DetailProduct::class.java)
                            intent.putExtra("slug", i)
                            startActivity(intent)
                        })
                })
                dialog.behavior.isHideable = false
                dialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                dialog.show()
            }

        }

        mbinding.buttonX.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        initRecentSearch()
        mbinding.tvSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (mbinding.tvSearch.text.toString() != "") {
                    CommonUtils.getInstance()
                        .saveUniqueStringToList("RecentSearch", mbinding.tvSearch.text.toString())
                    callAPI()
                    hideKeyboard()
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        mbinding.icSearch.setOnClickListener {
            if (mbinding.tvSearch.text.toString() != "") {
                CommonUtils.getInstance()
                    .saveUniqueStringToList("RecentSearch", mbinding.tvSearch.text.toString())
                callAPI()
                hideKeyboard()
            }
        }
    }

    private fun initRecentSearch() {
        if (CommonUtils.getInstance().getListPref("RecentSearch").isEmpty()) {
            mbinding.buy.visibility = View.VISIBLE
            mbinding.recentSearch.visibility = View.GONE
            mbinding.answerSearch.visibility = View.GONE
        } else {
            mbinding.buy.visibility = View.GONE
            mbinding.recentSearch.visibility = View.VISIBLE
            mbinding.answerSearch.visibility = View.GONE
            mbinding.rvRecentSearch.adapter = RecentSearchAdapter(
                CommonUtils.getInstance().getListPref("RecentSearch"),
                this,
                onClick = {
                    mbinding.tvSearch.setText(it)
                })
        }
    }

    private fun callAPI() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.searchProducts(mbinding.tvSearch.text.toString())
            .enqueue(object : Callback<SearchRes> {
                override fun onResponse(
                    call: Call<SearchRes>, response: Response<SearchRes>
                ) {
                    mbinding.tvSearch.setText("")
                    if (response.body()?.products?.isNotEmpty() == true) {
                        productList = response.body()!!.products

                        mbinding.buy.visibility = View.GONE
                        mbinding.recentSearch.visibility = View.GONE
                        mbinding.answerSearch.visibility = View.VISIBLE
                        mbinding.rvProduct.adapter = ProductAdapter(
                            response.body()!!.products, this@Search, onClickFolder = { i ->
                                val intent: Intent = Intent(
                                    this@Search, DetailProduct::class.java
                                )
                                intent.putExtra("slug", i)
                                startActivity(intent)
                            })
                    } else {
                        initRecentSearch()
                        CustomToast.showToast(
                            this@Search,
                            "Không tìm thấy sản phẩm phù hợp!",
                            CustomToast.ToastType.INFO
                        )
                    }
                    Log.e("KQ", response.body().toString())
                }

                override fun onFailure(call: Call<SearchRes>, t: Throwable) {
                    CustomToast.showToast(
                        this@Search,
                        "Lỗi kết nối!",
                        CustomToast.ToastType.WARNING
                    )
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }

    override fun initViewBinding(): SearchBinding {
        return SearchBinding.inflate(layoutInflater)
    }

    private fun Activity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}