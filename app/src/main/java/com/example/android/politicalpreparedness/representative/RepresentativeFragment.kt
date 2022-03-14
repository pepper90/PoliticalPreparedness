package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    //Declared ViewModel
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val TAG = "RepresentativeFragment"
    }

    // Location permission request
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            getLocation()
        } else {
            Snackbar.make(
                requireView(),
                R.string.location_permission_message,
                Snackbar.LENGTH_SHORT).show()
        }
    }

    // Location permission request Launcher
    private fun locationPermissionRequestLauncher() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
        }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)

        // Established bindings
        viewModel = ViewModelProvider(this)[RepresentativeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Bind states to AutoCompleteTextView
        val states = resources.getStringArray(R.array.states)
        val statesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_state, states)
        (binding.state.editText as? AutoCompleteTextView)?.setAdapter(statesAdapter)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Defined and assign Representative adapter
        binding.representativesRv.adapter = RepresentativeListAdapter()

        // Established button listener for field search
        binding.buttonSearch.setOnClickListener {
            // Hide keyboard
            hideKeyboard()

            // Tak user input and convert it to address
            viewModel.getAddress(formatInputToAddress())

            // Search the representatives with the address
            viewModel.loadRepresentativesData()
        }

        // Establish button listener for location search
        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            locationPermissionRequestLauncher()
        }

        return binding.root
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            for (location in p0.locations) {
                // Update UI with location data
                val address = geoCodeLocation(location)
                viewModel.getAddress(address)
                viewModel.loadRepresentativesData()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        // Get location from LocationServices
        try {
            val locationRequest = LocationRequest.create()
            locationRequest.apply {
                interval = 20000
                smallestDisplacement = 5F
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
        } catch (e: SecurityException) {
            Log.e(TAG, e.message!!)
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun formatInputToAddress(): Address {
        return Address(
            line1 = binding.addressLine1.editText?.text.toString(),
            line2 = binding.addressLine2.editText?.text.toString(),
            city = binding.city.editText?.text.toString(),
            state = binding.state.editText?.text.toString(),
            zip = binding.zip.editText?.text.toString()
        )
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}