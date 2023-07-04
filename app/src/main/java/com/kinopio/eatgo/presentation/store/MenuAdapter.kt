package com.kinopio.eatgo.presentation.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kinopio.eatgo.databinding.ItemMenuBinding
import com.kinopio.eatgo.domain.store.Menu

class MenuAdapter(private val menuList: List<Menu>?) :
    RecyclerView.Adapter<MenuAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMenuBinding.inflate(inflater, parent, false)
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

    inner class Holder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu) {
            binding.apply {
                binding.menuName.text = menu.menuName
                Glide.with(itemView)
                    .load(menu.thumbnail)
                    .into(menuImg)
                binding.menuNameInfo.text = menu.info
                binding.menuPriceAmount.text = "${menu.amount}개: ${menu.price}"
            }
        }
    }
}