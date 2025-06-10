package com.dragnell.a2p_shop.view.act

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.MainAdminActBinding
import com.dragnell.a2p_shop.model.res_api.AccountInfoRes
import com.dragnell.a2p_shop.model.res_api.ClientLogOutRes
import com.dragnell.a2p_shop.model.res_api.UserInfoRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.view.fragment.AccountsFragment
import com.dragnell.a2p_shop.view.fragment.CategoryFragment
import com.dragnell.a2p_shop.view.fragment.MyCartFragment
import com.dragnell.a2p_shop.view.fragment.OrderAdminFragment
import com.dragnell.a2p_shop.view.fragment.RevenueFragment
import com.dragnell.a2p_shop.view.fragment.RoleFragment
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainAdminActivity : BaseActivity<MainAdminActBinding, CommonViewModel>() {
    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        clickView(mbinding.menu.role)
        mbinding.menu.role.setOnClickListener(this)
        mbinding.menu.account.setOnClickListener(this)
        mbinding.menu.categories.setOnClickListener(this)
        mbinding.menu.order.setOnClickListener(this)
        mbinding.menu.revenue.setOnClickListener(this)

        callGetUserInfoAPI()

        mbinding.btLogOut.setOnClickListener {
            callLogOut()
            finish()
        }
    }

    private fun callLogOut() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.adminLogOut("Bearer ${CommonUtils.getInstance().getPref("Token")}")
            .enqueue(object : Callback<ClientLogOutRes> {
                override fun onResponse(
                    call: Call<ClientLogOutRes>, response: Response<ClientLogOutRes>
                ) {
                    Log.e("KQ", response.body().toString())
                    if (response.body()!!.message == "Đăng xuất thành công") {
                        CommonUtils.getInstance().clearPref("Token")
                        CustomToast.showToast(
                            this@MainAdminActivity,
                            "Đăng xuất thành công!",
                            CustomToast.ToastType.SUCCESS
                        )
                        val intent = Intent(this@MainAdminActivity, Login::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<ClientLogOutRes>, t: Throwable) {
                    CustomToast.showToast(
                        this@MainAdminActivity,
                        "Lỗi kết nối!",
                        CustomToast.ToastType.WARNING
                    )
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }

    override fun initViewBinding(): MainAdminActBinding {
        return MainAdminActBinding.inflate(layoutInflater)
    }

    override fun clickView(v: View) {
        super.clickView(v)
        if (v == mbinding.menu.role){
            showFragment(RoleFragment.TAG, null, false, R.id.fr)
        }
        if (v == mbinding.menu.account) {
            showFragment(AccountsFragment.TAG, null, false, R.id.fr)
        }
        if (v == mbinding.menu.categories) {
            showFragment(CategoryFragment.TAG, null, false, R.id.fr)
        }
        if (v == mbinding.menu.order) {
            showFragment(OrderAdminFragment.TAG, null, false, R.id.fr)
        }
        if (v == mbinding.menu.revenue) {
            showFragment(RevenueFragment.TAG, null, false, R.id.fr)
        }
    }

    private fun callGetUserInfoAPI() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getMyAccount("Bearer ${CommonUtils.getInstance().getPref("Token")}")
            .enqueue(object : Callback<AccountInfoRes> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<AccountInfoRes>, response: Response<AccountInfoRes>
                ) {
                        if (response.body()!!.account!=null){
                            mbinding.fullName.text= response.body()!!.account!!.fullName
                            mbinding.Email.text= response.body()!!.account!!.email
                            mbinding.phone.text= response.body()!!.account!!.phone.toString()
                        }
                        else{
                            CustomToast.showToast(
                                this@MainAdminActivity,
                                "Thông tin chưa thể hiển thị!",
                                CustomToast.ToastType.INFO
                            )
                            mbinding.fullName.text= ""
                            mbinding.Email.text= ""
                            mbinding.phone.text= ""
                        }
                }

                override fun onFailure(call: Call<AccountInfoRes>, t: Throwable) {
                    CustomToast.showToast(
                        this@MainAdminActivity,
                        "Lỗi kết nối!",
                        CustomToast.ToastType.WARNING
                    )
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }


}