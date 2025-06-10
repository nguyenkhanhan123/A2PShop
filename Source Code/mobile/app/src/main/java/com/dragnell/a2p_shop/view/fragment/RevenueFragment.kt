package com.dragnell.a2p_shop.view.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.RevenueBinding
import com.dragnell.a2p_shop.model.res_api.GetOrderAdminRes
import com.dragnell.a2p_shop.model.res_api.RevenueRes
import com.dragnell.a2p_shop.view.adapter.OrderAdapter
import com.dragnell.a2p_shop.view.adapter.RevenueAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RevenueFragment : BaseFragment<RevenueBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        callGetRevenue()
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): RevenueBinding {
        return RevenueBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = RevenueFragment::class.java.name
    }

    private fun callGetRevenue() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getRevenueAdmin(
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<RevenueRes> {
            override fun onResponse(
                call: Call<RevenueRes>, response: Response<RevenueRes>
            ) {
                Log.e("KQ", response.body().toString())
                if (response.body() != null) {
                    mbinding.rv.adapter =
                        RevenueAdapter(response.body()!!.order, requireContext())
                }
                Log.e("KQ", response.body().toString())

            }

            override fun onFailure(call: Call<RevenueRes>, t: Throwable) {
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }
}