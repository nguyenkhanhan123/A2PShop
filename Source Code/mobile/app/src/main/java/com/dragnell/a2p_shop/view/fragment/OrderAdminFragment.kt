package com.dragnell.a2p_shop.view.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.OrderBinding
import com.dragnell.a2p_shop.model.res_api.GetOrderAdminRes
import com.dragnell.a2p_shop.view.act.DetailOrderAdminActivity
import com.dragnell.a2p_shop.view.adapter.OrderAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderAdminFragment : BaseFragment<OrderBinding, CommonViewModel>() {

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        callGetOrder()
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): OrderBinding {
        return OrderBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = OrderAdminFragment::class.java.name
    }

    private fun callGetOrder() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getOrderAdmin(
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<GetOrderAdminRes> {
            override fun onResponse(
                call: Call<GetOrderAdminRes>, response: Response<GetOrderAdminRes>
            ) {
                Log.e("KQ", response.body().toString())
                if (response.body() != null) {
                    mbinding.rv.adapter =
                        OrderAdapter(response.body()!!.order, requireContext(), onClick = {
                            val intent: Intent = Intent(requireContext(), DetailOrderAdminActivity::class.java)
                            intent.putExtra("order", it)
                            startActivity(intent)
                        })
                }
                Log.e("KQ", response.body().toString())

            }

            override fun onFailure(call: Call<GetOrderAdminRes>, t: Throwable) {
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }
}