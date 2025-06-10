package com.dragnell.a2p_shop.view.act

import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.GgmapBinding
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class GgMap : BaseActivity<GgmapBinding, CommonViewModel>(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun initViewBinding(): GgmapBinding {
        return GgmapBinding.inflate(layoutInflater)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        val origin = LatLng(21.0285, 105.8542) // Hà Nội
        val destinationAddress = "12 Nguyễn Văn Bảo, Gò Vấp, TP.HCM"

        getLatLonFromAddress(destinationAddress) { destination ->
            if (destination != null) {
                runOnUiThread {
                    mMap?.addMarker(MarkerOptions().position(origin).title("Hà Nội"))
                    mMap?.addMarker(MarkerOptions().position(destination).title("Khách hàng"))
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 6f))
                }

                drawRouteAndAnimate(origin, destination)
            } else {
                Log.e("MAP", "Không tìm thấy tọa độ từ địa chỉ.")
            }
        }
    }

    private fun drawRouteAndAnimate(origin: LatLng, destination: LatLng) {
        val apiKey = "40eff71d-275a-496a-a83f-116383a7767d"
        val url = "https://graphhopper.com/api/1/route?" +
                "point=${origin.latitude},${origin.longitude}" +
                "&point=${destination.latitude},${destination.longitude}" +
                "&vehicle=car&locale=vi&points_encoded=false&key=$apiKey"

        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body()?.string() ?: return@Thread
                Log.d("GRAPH_HOPPER", "Response: $body")

                val json = JSONObject(body)
                val paths = json.getJSONArray("paths")
                if (paths.length() == 0) return@Thread

                val pathObj = paths.getJSONObject(0)
                val distanceMeters = pathObj.getDouble("distance")
                val timeMillis = pathObj.getLong("time")

                val distanceKm = distanceMeters / 1000
                val timeMinutes = timeMillis / 1000 / 60
                val timeHours = timeMinutes / 60
                val remainingMinutes = timeMinutes % 60

                // Show route info as toast
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Quãng đường: %.1f km\nThời gian ước tính: %d giờ %d phút".format(
                            distanceKm,
                            timeHours,
                            remainingMinutes
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }

                val pointsArray = pathObj
                    .getJSONObject("points")
                    .getJSONArray("coordinates")

                val path = ArrayList<LatLng>()
                for (i in 0 until pointsArray.length()) {
                    val coord = pointsArray.getJSONArray(i)
                    val lon = coord.getDouble(0)
                    val lat = coord.getDouble(1)
                    path.add(LatLng(lat, lon))
                }

                runOnUiThread {
                    mMap?.addPolyline(
                        PolylineOptions()
                            .addAll(path)
                            .color(Color.BLUE)
                            .width(10f)
                    )
                }

                runOnUiThread {
                    val truckMarker = mMap?.addMarker(
                        MarkerOptions()
                            .position(path.first())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.truck))
                            .anchor(0.5f, 0.5f)
                            .flat(true)
                    )
                    truckMarker?.let { animateTruckAlongPath(it, path) }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun animateTruckAlongPath(marker: Marker, path: List<LatLng>) {
        Thread {
            for (i in 0 until path.size - 1) {
                val start = path[i]
                val end = path[i + 1]
                val bearing = getBearing(start, end)

                val steps = 30
                for (j in 0..steps) {
                    val lat = start.latitude + (end.latitude - start.latitude) * j / steps
                    val lng = start.longitude + (end.longitude - start.longitude) * j / steps
                    val pos = LatLng(lat, lng)

                    runOnUiThread {
                        marker.position = pos
                        marker.rotation = bearing
                    }

                    Thread.sleep(50)
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

    private fun getLatLonFromAddress(address: String, callback: (LatLng?) -> Unit) {
        Thread {
            try {
                val query = URLEncoder.encode(address, "UTF-8")
                val url = "https://nominatim.openstreetmap.org/search?q=$query&format=json"
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "AndroidApp/1.0")
                    .build()
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
}
