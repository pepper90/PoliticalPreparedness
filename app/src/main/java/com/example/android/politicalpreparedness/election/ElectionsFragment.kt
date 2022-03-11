package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    //Declared ViewModel
    private lateinit var binding: FragmentElectionBinding
    private val viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(this)[ElectionsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //Added binding values
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        binding.lifecycleOwner = this

        //Initialize ViewModel
        binding.viewModel = viewModel

        // Linked elections to voter info
        viewModel.navigateToDetailElection.observe(viewLifecycleOwner) { election ->
            if (election != null) {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election
                    )
                )
                viewModel.onElectionNavigated()
                Log.d(
                    TAG,
                    "ID: ${election.id}, NAME: ${election.name}, DIVISION: ${election.division}"
                )
            }
        }

        // Initiated recycler adapters
        binding.upcomingElectionsRv.adapter = ElectionListAdapter(
            ElectionListener { election ->
                viewModel.onElectionClicked(election)
            }
        )

        binding.savedElectionsRv.adapter = ElectionListAdapter(
            ElectionListener { election ->
                viewModel.onElectionClicked(election)
            }
        )

        return binding.root
    }
}

private const val TAG = "FragmentElection"