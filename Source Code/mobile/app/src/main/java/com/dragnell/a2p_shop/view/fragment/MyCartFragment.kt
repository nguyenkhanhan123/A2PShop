package com.dragnell.a2p_shop.view.fragment

import CustomToast
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.MyCartBinding
import com.dragnell.a2p_shop.model.ProductOrder
import com.dragnell.a2p_shop.model.req_api.CartDeleteReq
import com.dragnell.a2p_shop.model.req_api.CartFixReq
import com.dragnell.a2p_shop.model.res_api.CartDeleteRes
import com.dragnell.a2p_shop.model.res_api.CartFixRes
import com.dragnell.a2p_shop.model.res_api.CartRes
import com.dragnell.a2p_shop.view.act.CheckoutActivity
import com.dragnell.a2p_shop.view.adapter.ProductCartAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCartFragment : BaseFragment<MyCartBinding, CommonViewModel>() {
    var listProductOrder: ArrayList<ProductOrder> = arrayListOf()

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        callGetCart()
        mbinding.checkout.setOnClickListener {
            if(listProductOrder.isEmpty()){
                CustomToast.showToast(requireContext(),"Vui lòng chọn sản phẩm muốn thanh toán!",CustomToast.ToastType.WARNING)
            }
            else{
                val intent = Intent(requireContext(),CheckoutActivity::class.java)
                intent.putExtra("listProductOrder", listProductOrder)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        listProductOrder=arrayListOf()
        callGetCart()
        super.onResume()
    }
    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): MyCartBinding {
        return MyCartBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = MyCartFragment::class.java.name
    }

    private fun callGetCart() {
        Log.e("KQ", CommonUtils.getInstance().getPref("Token").toString())
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getCart(
            "Bearer ${CommonUtils.getInstance().getPref("Token")}",
            "cartId=${CommonUtils.getInstance().getPref("Cookie")}"
        ).enqueue(object : Callback<CartRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CartRes>, response: Response<CartRes>
            ) {
                Log.e("KQ", response.body().toString())
                if (response.body()!!.code == 200) {
                    if (response.body()!!.cart.products.isEmpty()) {
                        mbinding.emptyCartLayout.visibility = View.VISIBLE
                        mbinding.cartItemsLayout.visibility = View.GONE
                    } else {
                        mbinding.emptyCartLayout.visibility = View.GONE
                        mbinding.cartItemsLayout.visibility = View.VISIBLE
                        mbinding.rvProductCart.adapter =
                            ProductCartAdapter(response.body()!!.cart.products,
                                requireContext(),
                                onClickDelete = { s ->
                                    callDeleteCart(s)
                                },
                                onClickUpdate = { s, i ->
                                    callUpdateCart(s, i)
                                },
                                onTrueCheckBox = {
                                    listProductOrder.add(it)
                                },
                                onFalseCheckBox = {
                                    listProductOrder.remove(it)
                                }
                            )

                    }
                }

            }

            override fun onFailure(call: Call<CartRes>, t: Throwable) {
                CustomToast.showToast(
                    requireContext(), "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

    private fun callDeleteCart(s: String) {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.cartDelete(
            CartDeleteReq(listOf(s)),
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<CartDeleteRes> {
            override fun onResponse(
                call: Call<CartDeleteRes>, response: Response<CartDeleteRes>
            ) {
                if (response.body()!!.code == 200) {
                    if (response.body()!!.message == "Xóa sản phẩm thành công") {
                        CustomToast.showToast(
                            requireContext(),
                            "Xóa thành công!",
                            CustomToast.ToastType.SUCCESS
                        )
                        callGetCart()
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

            override fun onFailure(call: Call<CartDeleteRes>, t: Throwable) {
                CustomToast.showToast(
                    requireContext(), "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

    private fun callUpdateCart(s: String, i: Int) {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.cartUpdateQuantity(
            CartFixReq(s, i),
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<CartFixRes> {
            override fun onResponse(
                call: Call<CartFixRes>, response: Response<CartFixRes>
            ) {
                if (response.body()!!.code == 200) {
                    if (response.body()!!.message == "update thanh cong") {
                        CustomToast.showToast(
                            requireContext(),
                            "Sửa thành công!",
                            CustomToast.ToastType.SUCCESS
                        )
                        callGetCart()
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

            override fun onFailure(call: Call<CartFixRes>, t: Throwable) {
                CustomToast.showToast(
                    requireContext(), "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

}