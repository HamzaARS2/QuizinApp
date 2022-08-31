package com.example.myquizapp.ui.fragments.shop

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.myquizapp.R
import com.example.myquizapp.databinding.FragmentShopBinding

class ShopFragment : Fragment() {

    private val viewModel: ShopViewModel by activityViewModels()
    private val binding by lazy {FragmentShopBinding.inflate(layoutInflater)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


}