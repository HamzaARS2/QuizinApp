package com.example.myquizapp.ui.fragments.shop

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myquizapp.R
import com.example.myquizapp.databinding.FragmentShopBinding
import com.example.myquizapp.ui.fragments.shop.viewpager_slides.GemsFragment
import com.example.myquizapp.ui.fragments.shop.viewpager_slides.GoldFragment
import com.example.myquizapp.ui.fragments.shop.viewpager_slides.ItemsFragment
import com.example.myquizapp.utils.setViewInsets
import com.google.android.material.tabs.TabLayoutMediator

class ShopFragment : Fragment() {

    private val viewModel: ShopViewModel by activityViewModels()
    private val binding by lazy {FragmentShopBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shopViewpager.adapter = FragmentAdapter(this)

        TabLayoutMediator(binding.shopTabLayout,binding.shopViewpager) { tab, position ->
            when(position) {
                0 -> tab.text = "Items"
                1 -> tab.text = "Gold Packs"
                else -> tab.text = "Gems Packs"
            }
        }.attach()
    }


}

class FragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ItemsFragment.newInstance()
            1 -> GoldFragment.newInstance()
            else -> GemsFragment.newInstance()
        }
    }

}