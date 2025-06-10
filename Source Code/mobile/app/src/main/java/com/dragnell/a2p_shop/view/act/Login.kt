package com.dragnell.a2p_shop.view.act

import android.content.Intent
import android.graphics.Typeface
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.LoginBinding
import com.dragnell.a2p_shop.model.req_api.AdminLoginReq
import com.dragnell.a2p_shop.model.req_api.ClientLoginReq
import com.dragnell.a2p_shop.model.res_api.AdminLoginRes
import com.dragnell.a2p_shop.model.res_api.ClientLoginRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : BaseActivity<LoginBinding, CommonViewModel>() {
    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        mbinding.SignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        var isPasswordVisible = false

        mbinding.eye.setOnClickListener {
            if (isPasswordVisible) {
                mbinding.Password.transformationMethod = PasswordTransformationMethod.getInstance()
                mbinding.Password.typeface = Typeface.DEFAULT
            } else {
                mbinding.Password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                mbinding.Password.typeface = Typeface.DEFAULT
            }

            mbinding.Password.setSelection(mbinding.Password.text?.length ?: 0)

            isPasswordVisible = !isPasswordVisible
        }


        mbinding.Login.setOnClickListener {
            callAdminLogin { isAdmin ->
                if (isAdmin) {
                    CustomToast.showToast(this,"Chào mừng Admin!",CustomToast.ToastType.SUCCESS)
                    startActivity(Intent(this, MainAdminActivity::class.java))
                    finish()
                    Log.e("KQ", "You are admin")
                } else {
                    callClientLogin()
                    Log.e("KQ", "You are not admin")
                }
            }
        }
    }


    private fun callAdminLogin(callback: (Boolean) -> Unit) {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.adminLogin(AdminLoginReq(mbinding.Email.text.toString().trim(), mbinding.Password.text.toString().trim()))
            .enqueue(object : Callback<AdminLoginRes> {
                override fun onResponse(call: Call<AdminLoginRes>, response: Response<AdminLoginRes>) {
                    val body = response.body()
                    if (body != null && body.code == 200 && body.message.trim() == "dang nhap thanh cong") {
                        CommonUtils.getInstance().savePref("Token", body.token)
                        callback(true)
                    } else {
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<AdminLoginRes>, t: Throwable) {
                    CustomToast.showToast(this@Login,"Lỗi kết nối!",CustomToast.ToastType.WARNING)
                    Log.e("KQ", "onFailure: " + t.message)
                    callback(false)
                }
            })
    }

    private fun callClientLogin() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.clientLogin1(ClientLoginReq(mbinding.Email.text.toString().trim(), mbinding.Password.text.toString().trim()))
            .enqueue(object : Callback<ClientLoginRes> {
                override fun onResponse(
                    call: Call<ClientLoginRes>,
                    response: Response<ClientLoginRes>
                ) {
                    val cookies = response.headers().values("Set-Cookie")

                    val cartIdLast = cookies.filter { it.startsWith("cartId=") }
                        .map { it.split(";")[0].substringAfter("cartId=") }
                        .lastOrNull()

                    CommonUtils.getInstance().savePref("Cookie", cartIdLast.toString())

                    Log.i("Cookie", cartIdLast.toString())

                    if((response.body()!!.code == 200) && (response.body()!!.massage.trim() =="khi ban thay tin nhan nay, ban da dang nhap thanh cong")){
                        CommonUtils.getInstance().savePref("Token", response.body()!!.token)
                        CustomToast.showToast(this@Login,"Đăng nhập thành công!",CustomToast.ToastType.SUCCESS)
                        startActivity(Intent(this@Login, MainClientActivity::class.java))
                        finish()
                    }
                    else if(response.body()!!.massage.trim() =="email khong chinh xac!")
                    {
                        CustomToast.showToast(this@Login,"Tài khoản chưa tồn tại!",CustomToast.ToastType.ERROR)
                    }
                    else if(response.body()!!.massage.trim() =="Sai mat khau!")
                    {
                        CustomToast.showToast(this@Login,"Sai mật khẩu!",CustomToast.ToastType.ERROR)
                    }
                    else if(response.body()!!.massage.trim() =="tai khoan dang bi khoa!")
                    {
                        CustomToast.showToast(this@Login,"Tài khoản bị khóa!",CustomToast.ToastType.ERROR)
                    }
                    Log.e("KQ", response.body().toString())
                }

                override fun onFailure(call: Call<ClientLoginRes>, t: Throwable) {
                    CustomToast.showToast(this@Login,"Lỗi kết nối!",CustomToast.ToastType.WARNING)
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }

    override fun initViewBinding(): LoginBinding {
        return LoginBinding.inflate(layoutInflater)
    }
}
