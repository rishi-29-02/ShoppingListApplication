package com.androidnerds.shoppinglist.other

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidnerds.shoppinglist.R
import com.androidnerds.shoppinglist.data.db.entities.ShoppingItem
import com.androidnerds.shoppinglist.databinding.ItemShoppingBinding
import com.androidnerds.shoppinglist.ui.ShoppingViewModel

class ShoppingItemAdapter(
    var items: List<ShoppingItem>,
    private val viewModel : ShoppingViewModel
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_shopping,
                parent,
                false
                )
            )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val currShoppingItem = items[position]

        holder.itemShoppingBinding.tvName.text = currShoppingItem.name
        holder.itemShoppingBinding.tvAmount.text = "${currShoppingItem.qty}"

        holder.itemShoppingBinding.ivDelete.setOnClickListener {
            viewModel.delete(currShoppingItem)
        }

        holder.itemShoppingBinding.ivAdd.setOnClickListener {
            currShoppingItem.qty++
            viewModel.upsert(currShoppingItem)
        }

        holder.itemShoppingBinding.ivMinus.setOnClickListener {
            if(currShoppingItem.qty > 0 ){
                currShoppingItem.qty--
                viewModel.upsert(currShoppingItem)
            }
        }
    }

    inner class ShoppingViewHolder(val itemShoppingBinding: ItemShoppingBinding)
        : RecyclerView.ViewHolder(itemShoppingBinding.root)
}