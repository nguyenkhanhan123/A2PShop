package com.dragnell.a2p_shop.view.act

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebViewClient
import com.dragnell.a2p_shop.databinding.VnpayBinding
import com.dragnell.a2p_shop.viewmodel.CommonViewModel

class VNPAYActivity : BaseActivity<VnpayBinding, CommonViewModel>() {

    private val paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=10000000&vnp_Command=pay&vnp_CreateDate=20250610125529&vnp_CurrCode=VND&vnp_ExpireDate=20250610131029&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=trencao%C3%B4&vnp_OrderType=other-type&vnp_ReturnUrl=https%3A%2F%2Fwww.facebook.com%2Fapi%2Fv1%2Fvnpay%2Fpayment&vnp_TmnCode=705XTMKT&vnp_TxnRef=1749534929127728&vnp_Version=2.1.0&vnp_SecureHash=c09fe5ee529545fa8be9d81a5c20e46f25c1682652163a16a471d07229c19dc21f3ee8cbcfbc797848bcf5421dcd92974eff1d0aa06a2578ed132f12bd494b9d"

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        mbinding.webview.settings.javaScriptEnabled = true
        mbinding.webview.webViewClient = WebViewClient()
        mbinding.webview.loadUrl(paymentUrl)
    }

    override fun initViewBinding(): VnpayBinding {
        return VnpayBinding.inflate(layoutInflater)
    }
}
