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
import com.kinopio.eatgo.presentation.map.HistoryNaverMapFragment
import com.kinopio.eatgo.presentation.map.StoreMangeNaverMapFragment

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
        Log.d("history", "11111 ${histories}")
        val info = arguments?.getSerializable(ARG_INFO) as? String

        val categoryId = arguments?.getSerializable(ARG_CATEGORY_ID) as? Int

        Log.d("Info", "${info}")

        val infoTxt = view.findViewById<TextView>(R.id.store_info_txt)
        infoTxt.text = info

     //   HistoryNaverMapFragment.newInstance(info, histories)

        val fm = parentFragmentManager
        val transaction = fm.beginTransaction()

        var mapFragment :HistoryNaverMapFragment = HistoryNaverMapFragment.newInstance(categoryId, histories)


        transaction.add(R.id.historyNaverMapContainer, mapFragment)
        transaction.commit()
    }

    companion object {
        private const val ARG_HISTORY_LIST = "history_list"
        private const val ARG_INFO ="store_info"
        private const val ARG_CATEGORY_ID = "category_id"
        fun newInstance(categoryId:Int?, info:String?, historyList: List<StoreHistory>?): StoreInfoFragment {
            val fragment = StoreInfoFragment()
            val args = Bundle()
            args.putSerializable(ARG_HISTORY_LIST, ArrayList(historyList))
            args.putSerializable(ARG_INFO, info)
            args.putSerializable(ARG_CATEGORY_ID, categoryId)
            fragment.arguments = args
            return fragment
        }
    }
}