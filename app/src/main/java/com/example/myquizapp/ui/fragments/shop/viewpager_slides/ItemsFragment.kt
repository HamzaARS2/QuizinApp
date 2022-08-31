package com.example.myquizapp.ui.fragments.shop.viewpager_slides

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myquizapp.R
import com.example.myquizapp.ui.fragments.shop.viewpager_slides.viewmodels.ItemsViewModel

class ItemsFragment : Fragment() {

    companion object {
        fun newInstance() = ItemsFragment()
    }

    private lateinit var viewModel: ItemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ItemsViewModel::class.java]

        return inflater.inflate(R.layout.fragment_items, container, false)
    }



}