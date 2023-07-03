package com.kinopio.eatgo.presentation.store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.databinding.FragmentSummaryInfomationBinding


class SummaryInfomationFragment : Fragment() {
    private var storeId :Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt("storeId")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSummaryInfomationBinding.inflate(inflater, container, false)
        binding.textView4.text = storeId.toString()
        val retrofit = RetrofitClient.getRetrofit2()


        return binding.root
    }

}