package com.kinopio.eatgo.presentation.store

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
import com.kinopio.eatgo.domain.store.ReviewDto
import com.kinopio.eatgo.domain.store.StoreHistory
import com.kinopio.eatgo.domain.store.ui_model.Store

class StoreInfoFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 전달 받기
        val histories = arguments?.getSerializable(ARG_HISTORY_LIST) as? List<StoreHistory>

        val info = arguments?.getSerializable(ARG_INFO) as? String

        Log.d("Info", "${info}")

        val infoTxt = view.findViewById<TextView>(R.id.store_info_txt)
        infoTxt.text = info
//        var reviewCount = reviews?.size.toString()
//        reviewCountText.text = "리뷰 개수 : " + reviewCount+ " 개"
//        if (histories != null) {
//            this.reviewList.addAll(histories)
//        }
//        val reveiewRecyclerView: RecyclerView = view.findViewById(R.id.detial_review_rv)
//        reveiewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        reviewAdapter = StoreDetailReviewAdapter(reviewList)
//        reveiewRecyclerView.adapter = reviewAdapter

    }

    companion object {
        private const val ARG_HISTORY_LIST = "history_list"
        private const val ARG_INFO ="store_info"
        fun newInstance(info:String?, historyList: List<StoreHistory>?): StoreInfoFragment {
            val fragment = StoreInfoFragment()
            val args = Bundle()
            args.putSerializable(ARG_HISTORY_LIST, ArrayList(historyList))
            args.putSerializable(ARG_INFO, info)
            fragment.arguments = args
            return fragment
        }
    }
}