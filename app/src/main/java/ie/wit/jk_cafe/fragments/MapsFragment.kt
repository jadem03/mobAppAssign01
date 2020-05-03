package ie.wit.jk_cafe.fragments

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.helpers.setMapMarker
import ie.wit.jk_cafe.helpers.trackLocation
import ie.wit.jk_cafe.main.MainActivity

class MapsFragment : SupportMapFragment(), OnMapReadyCallback {

    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainActivity
        getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.cafe_location)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        app.mMap = googleMap
        app.mMap.isMyLocationEnabled = true
        app.mMap.uiSettings.isZoomControlsEnabled = true
        app.mMap.uiSettings.setAllGesturesEnabled(true)
        app.mMap.clear()
        trackLocation(app)
        setMapMarker(app)

        val builder = LatLngBounds.builder()

        val dublin = LatLng(53.340888, -6.245676)
        app.mMap.addMarker(MarkerOptions().position(dublin).title("Dublin Café"))
        builder.include(dublin)

        val maynooth = LatLng(53.379031, -6.596471)
        app.mMap.addMarker(MarkerOptions().position(maynooth).title("Maynooth Café"))
        builder.include(maynooth)

        val waterford = LatLng(52.246254, -7.138080)
        app.mMap.addMarker(MarkerOptions().position(waterford).title("WIT Café"))
        builder.include(waterford)

        val bounds = builder.build()
        val padding = 50
        val camera = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        app.mMap.moveCamera(camera)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MapsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}