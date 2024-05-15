package com.androidnerds.shoppinglist.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnerds.shoppinglist.data.db.ShoppingDataBase
import com.androidnerds.shoppinglist.data.db.entities.ShoppingItem
import com.androidnerds.shoppinglist.data.repositories.ShoppingRepository
import com.androidnerds.shoppinglist.databinding.ActivityShoppingBinding
import com.androidnerds.shoppinglist.databinding.DialogAddShoppingItemBinding
import com.androidnerds.shoppinglist.other.ShoppingItemAdapter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ShoppingActivity : AppCompatActivity(), KodeinAware {

    private lateinit var binding : ActivityShoppingBinding

    override val kodein by kodein()
    private val factory: ShoppingViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        val viewModel = ViewModelProviders.of(this, factory)[ShoppingViewModel::class.java]

        setAdapter(viewModel)

        setOnEventListeners(viewModel)
    }
    
    private fun setOnEventListeners(viewModel: ShoppingViewModel) {
        binding.fab.setOnClickListener {
            showAddItemDialog(viewModel)
        }
    }

    private fun setAdapter(viewModel: ShoppingViewModel) {
        val adapter = ShoppingItemAdapter( listOf(), viewModel)
        binding.rvShoppingItem.layoutManager = LinearLayoutManager(this)
        binding.rvShoppingItem.adapter = adapter

        viewModel.getAllShoppingItems().observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    private fun showAddItemDialog(viewModel: ShoppingViewModel) {
        val dialog = Dialog(this)
        val layoutBinding = DialogAddShoppingItemBinding.inflate(LayoutInflater.from(this))

        layoutBinding.tvAdd.setOnClickListener {
            val name = layoutBinding.etName.text.toString()
            val amount = layoutBinding.etAmount.text.toString()

            if(name.isEmpty() || amount.isEmpty()){
                Toast.makeText(this, "Please enter all the information", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val item = ShoppingItem(name, amount.toInt())
            viewModel.upsert(item)
            dialog.dismiss()
        }

        layoutBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(layoutBinding.root)
        dialog.show()
    }
}