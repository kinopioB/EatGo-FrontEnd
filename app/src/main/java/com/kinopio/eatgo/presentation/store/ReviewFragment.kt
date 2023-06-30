package com.kinopio.eatgo.presentation.store

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.map.ReviewService
import com.kinopio.eatgo.databinding.FragmentReviewBinding
import com.kinopio.eatgo.domain.map.ReviewResponseDto
import com.kinopio.eatgo.domain.map.StoreLocationListDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = false
    }

    private lateinit var binding: FragmentReviewBinding

    // Fragment의 뷰를 생성하고 초기화
    // Fragment의 뷰 계층 구조를 생성
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }


    // 뷰에 대한 이벤트 리스너를 설정 뷰와 관련된 데이터를 로드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnRegister.setOnClickListener {
            // 별 값 가져오기
            val ratingStar = binding.ratingStar.rating
            // editText 값 가져오기
            val inputText = binding.reviewText.text.toString()
            Log.d("review", "editText : $inputText")
            Log.d("review", "ratingStar : $ratingStar")

            // storeId, userId 넘겨서 주기
            val storeId: Int = 1
            val userId: Int = 2

            Log.d("retrofit",  "retrofit1")

            val retrofit = RetrofitClient.getRetrofit2()
            val reviewService = retrofit.create(ReviewService::class.java)

            Log.d("retrofit",  "retrofit2")

            var reviewRequestDto : ReviewResponseDto = ReviewResponseDto(
                userId = userId,
                storeId = storeId,
                content = inputText,
                rating = ratingStar.toInt()
            )
            Log.d("retrofit", reviewService.toString())

            // URL
            reviewService.createReviews(storeId, userId, reviewRequestDto).enqueue(object : Callback<ReviewResponseDto> {
                    override fun onFailure(call: Call<ReviewResponseDto>, t: Throwable) {
                        Log.d("fail", "실패")
                        Log.d("fail", "$t")
                    }
                    override fun onResponse(
                        call: Call<ReviewResponseDto>,
                        response: Response<ReviewResponseDto>
                    ) {
                        Log.d("retrofit",  "retrofit3")

                        if (response.isSuccessful.not()) {
                            Log.d("retrofit",  "retrofit4")
                            return
                        }
                        Log.d("retrofit",  "통신 + ${response?.body()}")
                        // 1. 리뷰 데이터 DB에 넣어주기
                        // 2. activity에 그려주기
                    }
                })
        }
    }
}