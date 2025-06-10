package com.dragnell.a2p_shop.view.act

import CustomToast
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.MainClientActBinding
import com.dragnell.a2p_shop.model.res_api.ClientLogOutRes
import com.dragnell.a2p_shop.model.res_api.UserInfoRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.view.fragment.HomeFragment
import com.dragnell.a2p_shop.view.fragment.MyCartFragment
import com.dragnell.a2p_shop.view.fragment.OrderClientFragment
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainClientActivity : BaseActivity<MainClientActBinding, CommonViewModel>() {
    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        clickView(mbinding.menu.home)
        mbinding.menu.home.setOnClickListener(this)
        mbinding.menu.mycart.setOnClickListener(this)
        mbinding.menu.order.setOnClickListener(this)

        callGetUserInfoAPI()

        mbinding.btLogOut.setOnClickListener {
            callLogOut()
            finish()
        }
    }

    private fun callLogOut() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.clientLogOut("Bearer ${CommonUtils.getInstance().getPref("Token")}")
            .enqueue(object : Callback<ClientLogOutRes> {
                override fun onResponse(
                    call: Call<ClientLogOutRes>, response: Response<ClientLogOutRes>
                ) {
                    if (response.body()!!.message == "Logout successful") {
                        CommonUtils.getInstance().clearPref("Token")
                        CommonUtils.getInstance().clearPref("Cookie")
                        CommonUtils.getInstance().clearPref("userID")
                        CustomToast.showToast(
                            this@MainClientActivity,
                            "Đăng xuất thành công!",
                            CustomToast.ToastType.SUCCESS
                        )
                        val intent = Intent(this@MainClientActivity, Login::class.java)
                        startActivity(intent)
                    }
                    Log.e("KQ", response.body().toString())
                }

                override fun onFailure(call: Call<ClientLogOutRes>, t: Throwable) {
                    CustomToast.showToast(
                        this@MainClientActivity,
                        "Lỗi kết nối!",
                        CustomToast.ToastType.WARNING
                    )
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }

    override fun initViewBinding(): MainClientActBinding {
        return MainClientActBinding.inflate(layoutInflater)
    }

    override fun clickView(v: View) {
        super.clickView(v)
        if (v == mbinding.menu.home) {
            showFragment(HomeFragment.TAG, null, false, R.id.fr)
        }
        if (v == mbinding.menu.mycart) {
            showFragment(MyCartFragment.TAG, null, false, R.id.fr)
        }
        if (v == mbinding.menu.order) {
            showFragment(OrderClientFragment.TAG, null, false, R.id.fr)
        }
    }

    private fun callGetUserInfoAPI() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getUserInfo("Bearer ${CommonUtils.getInstance().getPref("Token")}")
            .enqueue(object : Callback<UserInfoRes> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<UserInfoRes>, response: Response<UserInfoRes>
                ) {
                    if (response.body()!!.code==200){
                        if (response.body()!!.user!=null){
                            CommonUtils.getInstance().savePref("userID", response.body()!!.user!!.id)
                            mbinding.fullName.text= response.body()!!.user!!.fullName
                            mbinding.Email.text= response.body()!!.user!!.email
                            mbinding.phone.text= response.body()!!.user!!.phone.toString()
                        }
                        else{
                            mbinding.fullName.text= ""
                            mbinding.Email.text= ""
                            mbinding.phone.text= ""
                        }
                    }
                    else{
                        CustomToast.showToast(
                            this@MainClientActivity,
                            "Thông tin chưa thể hiển thị!",
                            CustomToast.ToastType.INFO
                        )
                    }
                    Log.e("KQ", response.body().toString())
                }

                override fun onFailure(call: Call<UserInfoRes>, t: Throwable) {
                    CustomToast.showToast(
                        this@MainClientActivity,
                        "Lỗi kết nối!",
                        CustomToast.ToastType.WARNING
                    )
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }

}