package com.example.myquizapp.ui.fragments.shop.gem_packs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myquizapp.R
import com.example.myquizapp.databinding.FragmentGemsBinding
import com.example.myquizapp.ui.fragments.shop.items.ShopItemsAdapter
import com.example.myquizapp.utils.getGemPacks

class GemsFragment : Fragment() {

    companion object {
        fun newInstance() = GemsFragment()
    }

    private lateinit var viewModel: GemsViewModel
    private val binding by lazy {FragmentGemsBinding.inflate(layoutInflater)}
    private lateinit var gemsAdapter: GemPackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[GemsViewModel::class.java]
        gemsAdapter = GemPackAdapter(getGemPacks())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gemsRv.apply {
            adapter = gemsAdapter
            hasFixedSize()
        }
    }



}