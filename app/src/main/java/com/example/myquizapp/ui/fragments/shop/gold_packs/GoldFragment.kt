package com.example.myquizapp.ui.fragments.shop.gold_packs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myquizapp.R
import com.example.myquizapp.databinding.FragmentGoldBinding
import com.example.myquizapp.ui.fragments.shop.items.ShopItemsAdapter
import com.example.myquizapp.utils.getGoldPacks

class GoldFragment : Fragment() {

    companion object {
        fun newInstance() = GoldFragment()
    }

    private lateinit var viewModel: GoldViewModel
    private val binding by lazy{FragmentGoldBinding.inflate(layoutInflater)}
    private lateinit var goldAdapter: GoldPackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[GoldViewModel::class.java]
        goldAdapter = GoldPackAdapter(getGoldPacks())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goldRv.apply {
            adapter = goldAdapter
            hasFixedSize()
        }
    }


}