package com.kinopio.eatgo.presentation.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.FragmentSummaryInfomationBinding


class SummaryInfomationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSummaryInfomationBinding.inflate(inflater, container, false)
        return binding.root
    }
}