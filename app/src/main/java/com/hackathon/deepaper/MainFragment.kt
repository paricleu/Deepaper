package com.hackathon.deepaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrScan.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.mainContent)
                .navigate(R.id.scanFragment, arguments)
        }

        textReader.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.mainContent)
                .navigate(R.id.cameraFragment, arguments)
        }
    }
}