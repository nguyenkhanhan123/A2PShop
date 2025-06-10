package com.dragnell.a2p_shop.view.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.HomeBinding
import com.dragnell.a2p_shop.model.res_api.CategorySlugRes
import com.dragnell.a2p_shop.model.res_api.ProductRes
import com.dragnell.a2p_shop.view.act.DetailProduct
import com.dragnell.a2p_shop.view.act.Search
import com.dragnell.a2p_shop.view.adapter.ProductAdapter
import com.dragnell.a2p_shop.view.adapter.SaleProductAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BaseFragment<HomeBinding, CommonViewModel>() {
    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        callGetProduct()
        mbinding.close.setOnClickListener {
            mbinding.fr.visibility= View.GONE
        }
        MobileAds.initialize(requireContext()) {}

        val adRequest = AdRequest.Builder().build()

        mbinding.adView.loadAd(adRequest)
        mbinding.search.setOnClickListener {
            val intent = Intent(context, Search::class.java)
            requireContext().startActivity(intent)
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): HomeBinding {
        return HomeBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = HomeFragment::class.java.name
    }

    private fun callGetProduct() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getProduct("Bearer ${CommonUtils.getInstance().getPref("Token")}")
            .enqueue(object : Callback<ProductRes> {
                override fun onResponse(
                    call: Call<ProductRes>, response: Response<ProductRes>
                ) {
                    if (response.body()?.products?.isNotEmpty() == true) {
                        val productList = response.body()!!.products
                        mbinding.rvProduct.adapter = ProductAdapter(
                            response.body()!!.products, requireContext(), onClickFolder = { i ->
                                val intent: Intent = Intent(
                                    requireContext(), DetailProduct::class.java
                                )
                                intent.putExtra("slug", i)
                                startActivity(intent)
                            })
                        mbinding.viewPager.adapter = SaleProductAdapter(
                            response.body()!!.products, requireContext(), onClickFolder = { i ->
                                val intent: Intent = Intent(
                                    requireContext(), DetailProduct::class.java
                                )
                                intent.putExtra("slug", i)
                                startActivity(intent)
                            })
                    } else {
                        CustomToast.showToast(
                            requireContext(),
                            "Không tìm thấy sản phẩm phù hợp!",
                            CustomToast.ToastType.INFO
                        )
                    }
                    Log.e("KQ", response.body().toString())
                }

                override fun onFailure(call: Call<ProductRes>, t: Throwable) {
                    CustomToast.showToast(
                        requireContext(),
                        "Lỗi kết nối!",
                        CustomToast.ToastType.WARNING
                    )
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }
}