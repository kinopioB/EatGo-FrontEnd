package com.kinopio.eatgo.presentation.store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.User
import com.kinopio.eatgo.data.map.StoreLocationService
import com.kinopio.eatgo.databinding.FragmentSummaryInfomationBinding
import com.kinopio.eatgo.domain.store.StoreResponseDto
import com.kinopio.eatgo.presentation.map.DistanceResponseDto
import com.kinopio.eatgo.util.calDist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SummaryInfomationFragment : Fragment() {
    private var storeId :Int = 0
    private var posX : Double = 0.0
    private var posY : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt("storeId")
            posX = it.getDouble("posX")
            posY = it.getDouble("posY")
        }
        Log.d("summary", "$storeId")
        Log.d("summary", "$posX")
        Log.d("summary", "$posY")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSummaryInfomationBinding.inflate(inflater, container, false)

        val retrofit = RetrofitClient.getRetrofit2()

        val storeLocationService = retrofit.create(StoreLocationService::class.java)

        binding.cancelSummaryBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        storeLocationService.getDistance("${User.getPositionX()}, ${User.getPositionY()}", "${posX}, ${posY}", getString(com.kinopio.eatgo.R.string.google_key))
            .enqueue(object : Callback<DistanceResponseDto> {
                override fun onFailure(call: Call<DistanceResponseDto>, t: Throwable) {
                    Log.d("faildis", "실패")
                    Log.d("faildis", "$t")
                }

                override fun onResponse(
                    call: Call<DistanceResponseDto>,
                    response: Response<DistanceResponseDto>
                ) {
                    response.body()?.let {
                        Log.d("dis", "${it.rows[0].elements[0].distance.text}, ${it.rows[0].elements[0].duration.text}")
                        binding.storeDistanceTv.text = "${it.rows[0].elements[0].distance.text}, ${it.rows[0].elements[0].duration.text}"
                    }
                }
            })

        storeLocationService.getSummaryStore(storeId).enqueue(object : Callback<StoreResponseDto> {
            override fun onFailure(call: Call<StoreResponseDto>, t: Throwable) {
                Log.d("fail", "실패")
                Log.d("fail", "$t")
            }

            override fun onResponse(
                call: Call<StoreResponseDto>,
                response: Response<StoreResponseDto>
            ) {
                response.body()?.let {
                    Log.d("summary", "summary setting start")
                    Log.d("summary", "$it")
                    binding.storeNameTv.text = it.storeName
                    binding.openDayTv.text = "#" + it.openInfos.map { it.day }.joinToString(separator = " ").toString()
                    Log.d("summary", "${"#" + it.openInfos.map { it.day }.joinToString(separator = " ").toString()}")
                    binding.storeRatingTv.text = it.ratingAverage.toString()
                }
            }
        })


        return binding.root
    }

}