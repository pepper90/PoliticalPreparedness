package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    //Declared ViewModel
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 33
        private const val TAG = "RepresentativeFragment"
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
//            checkLocationPermissions()
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

//    private fun checkLocationPermissions(): Boolean {
//        return if (isPermissionGranted()) {
//            true
//        } else {
//            //TODO: Request Location permissions
//            false
//        }
//    }

//    private fun isPermissionGranted() : Boolean {
//        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
//    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
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