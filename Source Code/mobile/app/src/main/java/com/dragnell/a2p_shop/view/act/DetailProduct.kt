package com.dragnell.a2p_shop.view.act

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.DetailProductBinding
import com.dragnell.a2p_shop.model.Product
import com.dragnell.a2p_shop.model.req_api.CartFixReq
import com.dragnell.a2p_shop.model.res_api.CartAddRes
import com.dragnell.a2p_shop.model.res_api.CartFixRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailProduct : BaseActivity<DetailProductBinding, CommonViewModel>() {
    private var isExpanded = false
    lateinit var product: Product

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        callAPIGetProductBySlug()
        mbinding.btnDecrease.setOnClickListener {
            val i = mbinding.txtQuantity.text.toString().toIntOrNull() ?: 0
            mbinding.txtQuantity.text = (i - 1).coerceAtLeast(0).toString()
        }
        mbinding.btnIncrease.setOnClickListener {
            val i = mbinding.txtQuantity.text.toString().toIntOrNull() ?: 0
            mbinding.txtQuantity.text = (i + 1).coerceAtMost(10).toString()
        }
        mbinding.btAddToCart.setOnClickListener {
            callAPIAddToCart()
        }
    }

    override fun initViewBinding(): DetailProductBinding {
        return DetailProductBinding.inflate(layoutInflater)
    }


    private fun callAPIGetProductBySlug() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getProductBySlug(intent.getStringExtra("slug").toString())
            .enqueue(object : Callback<Product> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<Product>, response: Response<Product>
                ) {
                    if (response.body() != null) {
                        mbinding.clMain.visibility=View.VISIBLE
                        product = response.body()!!
                        Glide.with(this@DetailProduct).load(response.body()!!.thumbnail)
                            .into(mbinding.imgProduct)
                        mbinding.txtProductName.text = response.body()!!.title
                        val fullText = response.body()!!.description
                        val shortText = fullText.take(300) + "..."
                        setTextWithReadMore(mbinding.describe, shortText, fullText)
                        mbinding.txtOriginalPrice.text = "$ ${response.body()!!.price}"
                        val discountedPrice = (product.price * (1 - product.discountPercentage.toDouble() / 100)).toInt()
                        mbinding.txtProductPrice.text =
                            "$ $discountedPrice"
                    }
                    else {
                        mbinding.clMain.visibility=View.GONE
                        CustomToast.showToast(this@DetailProduct,"Không tìm thấy sản phẩm phù hợp!",CustomToast.ToastType.INFO)
                    }
                    Log.e("KQ", response.body().toString())
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    CustomToast.showToast(this@DetailProduct,"Lỗi kết nối!",CustomToast.ToastType.WARNING)
                    Log.e("KQ", "onFailure: " + t.message)
                }

            })
    }

    private fun callAPIAddToCart() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.cartAdd(
            CartFixReq(
                product.id, mbinding.txtQuantity.text.toString().toIntOrNull() ?: 0
            ),
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<CartAddRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CartAddRes>, response: Response<CartAddRes>
            ) {
                if(response.body()!=null && response.body()!!.message=="thanh cong"){
                    CustomToast.showToast(this@DetailProduct,"Thêm vào giỏ hàng thành công!",CustomToast.ToastType.SUCCESS)
                }
                else{
                    CustomToast.showToast(this@DetailProduct,"Lỗi trong quá trình xử lý!",CustomToast.ToastType.ERROR)
                }
                Log.e("KQ", response.body().toString())
            }

            override fun onFailure(call: Call<CartAddRes>, t: Throwable) {
                CustomToast.showToast(this@DetailProduct,"Lỗi kết nối!",CustomToast.ToastType.WARNING)
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

    private fun setTextWithReadMore(textView: TextView, shortText: String, fullText: String) {
        val actionText = if (isExpanded) " Read less" else " Read more"
        val displayText = if (isExpanded) fullText else shortText

        val spannableString = SpannableString(displayText + actionText)

        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                isExpanded = !isExpanded
                setTextWithReadMore(textView, shortText, fullText)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }, displayText.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}
