package com.dragnell.a2p_shop.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.PlaceBinding
import com.dragnell.a2p_shop.model.Order
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class PlaceAdminFragment : BaseFragment<PlaceBinding, CommonViewModel>(), OnMapReadyCallback {

    private lateinit var order: Order
    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 111

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        order = requireActivity().intent.getSerializableExtra("order") as Order
        Log.e("order", order.toString())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        mbinding.road.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                getCurrentLocationAndDrawRoute()
            }
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PlaceBinding {
        return PlaceBinding.inflate(inflater, container, false)
    }

    companion object {
        val TAG: String = PlaceAdminFragment::class.java.name
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        try {
            val customerLatLng = LatLng(
                order.userInfo?.toadoa?.coordinates?.get(0) ?: 0.0,
                order.userInfo?.toadoa?.coordinates?.get(1) ?: 0.0
            )
            mMap?.addMarker(MarkerOptions().position(customerLatLng).title("Khách Hàng"))
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(customerLatLng, 15f))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Không thể hiển thị vị trí khách hàng", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationAndDrawRoute() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                val customerLatLng = LatLng(
                    order.userInfo?.toadoa?.coordinates?.get(0) ?: 0.0,
                    order.userInfo?.toadoa?.coordinates?.get(1) ?: 0.0
                )
                drawRoute(currentLatLng, customerLatLng)
            } ?: Toast.makeText(
                requireContext(),
                "Không lấy được vị trí hiện tại",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocationAndDrawRoute()
        } else {
            Toast.makeText(
                requireContext(),
                "Bạn cần cấp quyền để hiển thị lộ trình",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun drawRoute(start: LatLng, end: LatLng) {
        Thread {
            try {
                val apiKey = "40eff71d-275a-496a-a83f-116383a7767d"
                val url = "https://graphhopper.com/api/1/route?" +
                        "point=${start.latitude},${start.longitude}" +
                        "&point=${end.latitude},${end.longitude}" +
                        "&vehicle=car&locale=vi&points_encoded=false&key=$apiKey"

                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body()?.string() ?: return@Thread

                val json = JSONObject(body)
                val pathObj = json.getJSONArray("paths").getJSONObject(0)

                val distanceMeters = pathObj.getDouble("distance")
                val timeMillis = pathObj.getLong("time")

                val distanceKm = distanceMeters / 1000
                val timeMinutes = timeMillis / 1000 / 60
                val timeHours = timeMinutes / 60
                val remainingMinutes = timeMinutes % 60

                val pointsArray = pathObj.getJSONObject("points").getJSONArray("coordinates")

                val path = ArrayList<LatLng>()
                for (i in 0 until pointsArray.length()) {
                    val coord = pointsArray.getJSONArray(i)
                    val lon = coord.getDouble(0)
                    val lat = coord.getDouble(1)
                    path.add(LatLng(lat, lon))
                }

                val bearing = getBearing(path[0], path[1])

                requireActivity().runOnUiThread {
                    mMap?.addPolyline(
                        PolylineOptions()
                            .addAll(path)
                            .color(Color.BLUE)
                            .width(10f)
                    )

                    // Thêm marker xe tải tại vị trí bắt đầu, có hướng (rotation)
                    val truckMarker = mMap?.addMarker(
                        MarkerOptions()
                            .position(path.first())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.space))
                            .anchor(0.5f, 0.5f)
                            .flat(true)
                            .rotation(bearing)
                    )

                    mMap?.addMarker(MarkerOptions().position(end).title("Khách hàng"))
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(path.first(), 14f))

                    mbinding.s.text = "Quãng đường dự kiến: %.1f km".format(distanceKm)
                    mbinding.t.text = "Thời gian dự kiến: %d giờ %d phút".format(timeHours, remainingMinutes)

                }
            } catch (e: Exception) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Không thể vẽ tuyến đường",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }


    private fun getBearing(from: LatLng, to: LatLng): Float {
        val lat1 = Math.toRadians(from.latitude)
        val lon1 = Math.toRadians(from.longitude)
        val lat2 = Math.toRadians(to.latitude)
        val lon2 = Math.toRadians(to.longitude)

        val dLon = lon2 - lon1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)
        val brng = Math.toDegrees(Math.atan2(y, x))
        return ((brng + 360) % 360).toFloat()
    }
}
