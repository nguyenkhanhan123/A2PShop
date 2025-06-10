package com.dragnell.a2p_shop.view.act

import CustomToast
import android.content.Intent
import android.graphics.Typeface
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.SignupBinding
import com.dragnell.a2p_shop.model.req_api.ClientLoginReq
import com.dragnell.a2p_shop.model.req_api.ClientSignUpReq
import com.dragnell.a2p_shop.model.res_api.ClientLoginRes
import com.dragnell.a2p_shop.model.res_api.ClientSignUpRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUp : BaseActivity<SignupBinding, CommonViewModel>() {
    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
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
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        mbinding.createAccount.setOnClickListener {
            callCreateAccount()
        }
    }

    private fun callCreateAccount() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.clientSignUp(
            ClientSignUpReq(
                mbinding.Email.text.toString().trim(),
                mbinding.Password.text.toString().trim(),
                mbinding.fullName.text.toString().trim(),
                mbinding.phone.text.toString().trim()
            )
        ).enqueue(object : Callback<ClientSignUpRes> {
            override fun onResponse(
                call: Call<ClientSignUpRes>, response: Response<ClientSignUpRes>
            ) {
                Log.i("KQ", response.body().toString())
                if(response.body()!!.code == 200){
                    val cookies = response.headers().values("Set-Cookie")

                    val cartIdLast = cookies.filter { it.startsWith("cartId=") }
                        .map { it.split(";")[0].substringAfter("cartId=") }
                        .lastOrNull()

                    CommonUtils.getInstance().savePref("Cookie", cartIdLast.toString())

                   callLogin()

                    CustomToast.showToast(this@SignUp,"Đăng ký thành công!",CustomToast.ToastType.SUCCESS)

                    finish()
                }
                else{
                    CustomToast.showToast(this@SignUp,"Tài khoản đã tồn tại!",CustomToast.ToastType.ERROR)
                }
            }

            override fun onFailure(call: Call<ClientSignUpRes>, t: Throwable) {
                CustomToast.showToast(this@SignUp,"Lỗi kết nối!",CustomToast.ToastType.WARNING)
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

    private fun callLogin() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.clientLogin2(ClientLoginReq(mbinding.Email.text.toString().trim(), mbinding.Password.text.toString().trim()),"cartId=${CommonUtils.getInstance().getPref("Cookie")}")
            .enqueue(object : Callback<ClientLoginRes> {
                override fun onResponse(
                    call: Call<ClientLoginRes>,
                    response: Response<ClientLoginRes>
                ) {
                    if((response.body()!!.code == 200) && (response.body()!!.massage.trim() =="khi ban thay tin nhan nay, ban da dang nhap thanh cong")){
                        CommonUtils.getInstance().savePref("Token", response.body()!!.token)
                        startActivity(Intent(this@SignUp, MainClientActivity::class.java))
                        finish()
                    }
                    Log.e("KQ", response.body().toString())
                }

                override fun onFailure(call: Call<ClientLoginRes>, t: Throwable) {
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }

    override fun initViewBinding(): SignupBinding {
        return SignupBinding.inflate(layoutInflater)
    }
}
