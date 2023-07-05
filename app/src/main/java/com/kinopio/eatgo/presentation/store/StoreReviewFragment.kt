package com.kinopio.eatgo.presentation.store

import StoreMenuFragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.domain.map.ReviewResponseDto
import com.kinopio.eatgo.domain.store.Menu
import com.kinopio.eatgo.domain.store.ReviewDto
import com.naver.maps.map.e


class StoreReviewFragment : Fragment() {

    private lateinit var reviewAdapter : StoreDetailReviewAdapter
    private val reviewList  = mutableListOf<ReviewDto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 전달 받기
        val reviews = arguments?.getSerializable(ARG_REVIEW_LIST) as? List<ReviewDto>
        if (reviews != null) {
            this.reviewList.addAll(reviews)
        }
        Log.d("reviewed","review fragment data tesst ${reviews}")
        val reveiewRecyclerView: RecyclerView = view.findViewById(R.id.detial_review_rv)
        reveiewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        reviewAdapter = StoreDetailReviewAdapter(reviewList)
        reveiewRecyclerView.adapter = reviewAdapter

        val reviewCountText = view.findViewById<TextView>(R.id.reviewCount)
        var reviewCount = reviews?.size.toString()
        reviewCountText.text = "리뷰 개수 : " + reviewCount+ " 개"

    }
    companion object {
        private const val ARG_REVIEW_LIST = "review_list"

        fun newInstance(reviewList: List<ReviewDto>): StoreReviewFragment {
            val fragment = StoreReviewFragment()
            val args = Bundle()
            args.putSerializable(ARG_REVIEW_LIST, ArrayList(reviewList))
            fragment.arguments = args
            return fragment
        }
    }

}