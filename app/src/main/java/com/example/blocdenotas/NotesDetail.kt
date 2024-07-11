package com.example.blocdenotas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.blocdenotas.databinding.FragmentNotesDetailBinding
import com.example.blocdenotas.observer.ConnectivityObserver
import com.example.blocdenotas.viewmodels.NoteShareViewModel
import com.example.blocdenotas.viewmodels.NotesDetailViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.String
import java.util.Locale


class NotesDetail : Fragment() {
    lateinit var binding: FragmentNotesDetailBinding
    lateinit var viewModel: NoteShareViewModel
    lateinit var detailViewModel: NotesDetailViewModel
    lateinit var locationProvider: FusedLocationProviderClient
    var locationRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // callback if permission was done
                tryGetLastLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // callback if permission was done
                tryGetLastLocation()
            } else -> {
            // No permision was provided
            // Ask the user to set permissions on Settings.
        }
        }
    }
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).noteShareViewModel
        detailViewModel = (activity as MainActivity).noteDetailViewModel
        connectivityObserver = (activity as MainActivity).connectivityObserver
        binding.viewModel = viewModel
        binding.detailsViewModel = detailViewModel
        detailViewModel.updateTexts()
        binding.lifecycleOwner = this
        binding.button.setOnClickListener {
            detailViewModel.save()
            binding.root.findNavController().navigate(R.id.action_notesDetail_to_notesListPage)
        }
        locationProvider = LocationServices.getFusedLocationProviderClient((activity as MainActivity))


        tryGetLastLocation()
        setupSafeButton()
        setupDeleteButton()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        if (viewModel.selectedNote == null) {
            viewModel.selectedNote = null
            binding.textView.text = ""
            detailViewModel.notesDescription.value = ""
            detailViewModel.notesTitle.value = ""
        }
    }

    private fun setupSafeButton() {
        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                if (it != ConnectivityObserver.InternetStatus.Available) {
                    withContext(Dispatchers.Main) {
                        binding.button.isEnabled = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.button.isEnabled = true
                    }
                }
            }
        }

        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                if (it != ConnectivityObserver.InternetStatus.Available) {
                    withContext(Dispatchers.Main) {
                        binding.editTextText.isEnabled = false
                        binding.editTextText2.isEnabled = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.deleteButton.isEnabled = true
                        binding.button.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setupDeleteButton() {
        if (viewModel.selectedNote == null) {
            binding.deleteButton.isVisible = false
        }

            binding.deleteButton.setOnClickListener {
                detailViewModel.remove()
                binding.root.findNavController().navigate(R.id.action_notesDetail_to_notesListPage)
            }

    }

    fun tryGetLastLocation() {
        var latitude = 0.0
        var longitude = 0.0
        if (viewModel.selectedNote == null) {
            val hasFineLocation = ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val hasCoarseLocation = ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if ( hasFineLocation && !hasCoarseLocation) {
                locationRequest.launch(arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION ))
            }

            locationProvider.lastLocation.addOnSuccessListener { location ->
                latitude = location.latitude
                longitude = location.longitude
                detailViewModel.latitude.value = latitude
                detailViewModel.longitude.value = longitude
                binding.textView.text = "${latitude} ${longitude}"
                binding.textView.setOnClickListener {
                    val uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    context?.startActivity(intent);
                }
            }
        } else {
            longitude = viewModel.selectedNote!!.longitude
            latitude = viewModel.selectedNote!!.latitude
            detailViewModel.latitude.value = latitude
            detailViewModel.longitude.value = longitude
            binding.textView.text = "${latitude} ${longitude}"

            binding.textView.setOnClickListener {
                val uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context?.startActivity(intent);
            }
        }

    }
}