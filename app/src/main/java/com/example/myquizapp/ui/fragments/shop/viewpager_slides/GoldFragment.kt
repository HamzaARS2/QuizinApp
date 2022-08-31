package com.example.myquizapp.ui.fragments.shop.viewpager_slides

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myquizapp.R
import com.example.myquizapp.ui.fragments.shop.viewpager_slides.viewmodels.GoldViewModel

class GoldFragment : Fragment() {

    companion object {
        fun newInstance() = GoldFragment()
    }

    private lateinit var viewModel: GoldViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[GoldViewModel::class.java]

        return inflater.inflate(R.layout.fragment_gold, container, false)
    }


}