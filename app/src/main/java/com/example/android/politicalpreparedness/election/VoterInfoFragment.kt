package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
        binding.lifecycleOwner = this

        // Get argument from Bundle
        val electionArgs = VoterInfoFragmentArgs.fromBundle(requireArguments()).election

        // Bind argument to election
        binding.election = electionArgs

        // Added ViewModel values and create ViewModel
        viewModel = ViewModelProvider(
            this,
            VoterInfoViewModelFactory(requireActivity().application, electionArgs!!)
        )[VoterInfoViewModel::class.java]

        binding.viewModel = viewModel


        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        //TODO: Handle loading of URLs

        return binding.root
    }

    //TODO: Create method to load URL intents

}