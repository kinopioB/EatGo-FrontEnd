package com.kinopio.eatgo.presentation.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemBestMenuBinding
import com.kinopio.eatgo.domain.store.Menu

class BestMenuAdapter(private val menuList: List<Menu>?) :
    RecyclerView.Adapter<BestMenuAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBestMenuBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val menu = menuList?.get(position)
        menu?.let {
            // menu 데이터 활용
            // 예: holder.bindData(menu)
            holder.bind(menu)
        }
    }

    override fun getItemCount(): Int {
        return menuList?.size ?: 0
    }

    inner class Holder(private val binding: ItemBestMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu) {
            binding.apply {
                binding.menuName.text = menu.menuName
            }
        }
    }
}