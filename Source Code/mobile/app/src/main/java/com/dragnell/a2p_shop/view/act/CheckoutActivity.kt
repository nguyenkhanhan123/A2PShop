package com.dragnell.a2p_shop.view.act

import CustomToast
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.CheckoutBinding
import com.dragnell.a2p_shop.model.Coordinates
import com.dragnell.a2p_shop.model.ProductOrder
import com.dragnell.a2p_shop.model.UserInfo
import com.dragnell.a2p_shop.model.req_api.PostOrderReq
import com.dragnell.a2p_shop.model.res_api.PostOrderRes
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder


class CheckoutActivity : BaseActivity<CheckoutBinding, CommonViewModel>(), OnMapReadyCallback {
    private var listProductOrder: ArrayList<ProductOrder>? = null
    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var selectedPaymentMethod: String? = null

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mbinding.search.setOnClickListener {
            if (mbinding.address.text.toString() == "") {
                CustomToast.showToast(
                    this@CheckoutActivity, "Vui lòng điền địa chỉ!", CustomToast.ToastType.WARNING
                )
            } else {
                getLatLonFromAddress(mbinding.address.text.toString()) { destination ->
                    if (destination != null) {
                        runOnUiThread {
                            mMap?.addMarker(
                                MarkerOptions().position(destination).title("Khách hàng")
                            )
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 16f))
                        }
                        mbinding.addressXY.text =
                            "(${destination.latitude}, ${destination.longitude})"
                    } else {
                        CustomToast.showToast(
                            this@CheckoutActivity,
                            "Không tìm thấy tọa độ của địa chỉ, vui lòng chọn tọa độ hiện tại!",
                            CustomToast.ToastType.WARNING
                        )
                    }
                }
            }
        }

        mbinding.eye.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), 111
                )
            } else {
                enableMyLocation()
            }
        }

        listProductOrder =
            intent.getSerializableExtra("listProductOrder") as? ArrayList<ProductOrder>
        val numberProduct = listProductOrder?.size
        val subToTalCost = listProductOrder?.sumOf { product ->
            val discountedPrice = product.price!! - product.discountPercentage!!
            discountedPrice * product.quantity!!
        } ?: 0
        val shippingCost = 0
        val total = subToTalCost + shippingCost
        mbinding.numberProduct.text = numberProduct.toString()
        mbinding.subTotalCost.text = "$ $subToTalCost"
        mbinding.shippingCost.text = "$ $shippingCost"
        mbinding.totalCost.text = "$ $total"
        mbinding.cartItemsLayout.visibility = View.VISIBLE

        mbinding.checkout.setOnClickListener {
            callPostOrder()
        }

        mbinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedPaymentMethod = when (checkedId) {
                R.id.cash -> "CASH"
                R.id.online1 -> "VNPAY"
                R.id.online2 -> "MOMO"
                else -> null
            }
        }
    }

    private fun callPostOrder() {
        val postOrderReq: PostOrderReq = PostOrderReq(
            "pending", UserInfo(
                mbinding.fullName.text.toString(),
                mbinding.phone.text.toString(),
                mbinding.address.text.toString(),
                Coordinates(
                    "Point", parseCoordinateString(mbinding.addressXY.text.toString())
                )
            ), listProductOrder!!
        )
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.orderPost(
            postOrderReq, "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<PostOrderRes> {
            override fun onResponse(
                call: Call<PostOrderRes>, response: Response<PostOrderRes>
            ) {
                if (response.body()!!.message == "Đặt hàng thành công") {
                    CustomToast.showToast(
                        this@CheckoutActivity, "Đặt hàng thành công!", CustomToast.ToastType.SUCCESS
                    )
                    if (selectedPaymentMethod == "CASH") {
                        finish()
                    }
                    if (selectedPaymentMethod == "VNPAY") {
                        val text = mbinding.totalCost.text.toString()  // "$ 100"
                        val numberStr = text.replace("$", "").trim()  // "100"
                        val total = numberStr.toInt()
                        val intent = Intent(this@CheckoutActivity, VNPAYActivity::class.java)
                        intent.putExtra("totalCost", total)
                        startActivity(intent)

                    }

                } else {
                    CustomToast.showToast(
                        this@CheckoutActivity,
                        "Chưa thể cập nhật bây giờ!",
                        CustomToast.ToastType.INFO
                    )
                }
                Log.e("KQ", response.body().toString())
            }

            override fun onFailure(call: Call<PostOrderRes>, t: Throwable) {
                CustomToast.showToast(
                    this@CheckoutActivity, "Lỗi kết nối!", CustomToast.ToastType.WARNING
                )
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

    private fun getLatLonFromAddress(address: String, callback: (LatLng?) -> Unit) {
        Thread {
            try {
                val query = URLEncoder.encode(address, "UTF-8")
                val url = "https://nominatim.openstreetmap.org/search?q=$query&format=json"
                val client = OkHttpClient()
                val request =
                    Request.Builder().url(url).header("User-Agent", "AndroidApp/1.0").build()
                val response = client.newCall(request).execute()
                val responseBody = response.body()?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    callback(null)
                    return@Thread
                }

                val jsonArray = JSONArray(responseBody)
                if (jsonArray.length() == 0) {
                    callback(null)
                    return@Thread
                }

                val location = jsonArray.getJSONObject(0)
                val lat = location.getDouble("lat")
                val lon = location.getDouble("lon")
                callback(LatLng(lat, lon))
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }.start()
    }

    override fun initViewBinding(): CheckoutBinding {
        return CheckoutBinding.inflate(layoutInflater)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.apply {
            isScrollGesturesEnabled = false    // Không cho kéo bản đồ
            isZoomGesturesEnabled = false      // Không cho zoom bằng chạm
            isTiltGesturesEnabled = false      // Không cho nghiêng bản đồ
            isRotateGesturesEnabled = false    // Không cho xoay bản đồ
            isZoomControlsEnabled = false      // Ẩn nút zoom mặc định của GoogleMap
            isMapToolbarEnabled = false        // Ẩn toolbar GoogleMap khi bấm marker
        }
        val hanoi = LatLng(21.0278, 105.8342)
        mMap!!.addMarker(MarkerOptions().position(hanoi).title("Hanoi"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoi, 16f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap!!.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mbinding.addressXY.text = "(${it.latitude}, ${it.longitude})"
                    mbinding.address.setText("Tọa độ thực tế")
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                    mMap!!.addMarker(
                        MarkerOptions().position(currentLatLng).title("Vị trí của bạn")
                    )
                }
            }
        }
    }

    fun parseCoordinateString(coordString: String): List<Double> {
        return coordString.removePrefix("(").removeSuffix(")").split(",")
            .map { it.trim().toDouble() }.let { listOf(it[0], it[1]) }
    }

}
