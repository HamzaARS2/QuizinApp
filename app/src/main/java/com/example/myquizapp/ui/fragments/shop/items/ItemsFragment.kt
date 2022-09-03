package com.example.myquizapp.ui.fragments.shop.items

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myquizapp.R
import com.example.myquizapp.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {

    companion object {
        fun newInstance() = ItemsFragment()
    }

    private lateinit var viewModel: ItemsViewModel
    private val binding by lazy {FragmentItemsBinding.inflate(layoutInflater)}
    private lateinit var shopItemsAdapter: ShopItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ItemsViewModel::class.java]
        shopItemsAdapter = ShopItemsAdapter(arrayListOf())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shopItemsRv.apply {
            adapter = shopItemsAdapter
            hasFixedSize()
        }
    }



}