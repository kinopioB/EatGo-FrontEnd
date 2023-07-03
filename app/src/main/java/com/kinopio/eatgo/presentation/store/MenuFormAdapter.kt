package com.kinopio.eatgo.presentation.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemMenuFormBinding
import com.kinopio.eatgo.domain.store.ui_model.MenuForm

class MenuFormAdapter(private val menuList: List<MenuForm>) :
    RecyclerView.Adapter<MenuFormAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMenuFormBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val menu = menuList[position]
        holder.bind(menu)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class Holder(private val binding: ItemMenuFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: MenuForm) {
            binding.apply {
                menuNameTv.text = menu.name
                menuCountTv.text = menu.amount.toString()
                menuPriceTv.text = menu.price.toString()
                menuImg.setImageURI(menu.imageUri)
            }
        }
    }
}