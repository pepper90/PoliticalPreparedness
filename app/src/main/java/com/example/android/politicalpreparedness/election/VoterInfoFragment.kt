package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
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

        // Added binding values
        // Bind argument to election in xml
        binding.election = electionArgs

        // Added and created ViewModel
        viewModel = ViewModelProvider(
            this,
            VoterInfoViewModelFactory(requireActivity().application, electionArgs!!)
        )[VoterInfoViewModel::class.java]

        // Bind VoterInfoViewModel to viewModel in xml
        binding.viewModel = viewModel


        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        // Handled loading of URLs
        viewModel.redirect.observe(viewLifecycleOwner) {
            loadURLIntent(it)
        }
        return binding.root
    }

    // Created method to load URL intents
    private fun loadURLIntent(strUri: String?) {
        if (!strUri.isNullOrBlank()) {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            startActivity(i)
        }
    }
}