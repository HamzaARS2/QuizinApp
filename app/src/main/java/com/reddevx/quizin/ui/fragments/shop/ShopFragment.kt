package com.reddevx.quizin.ui.fragments.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.reddevx.quizin.databinding.FragmentShopBinding
import com.reddevx.quizin.ui.fragments.shop.gem_packs.GemsFragment
import com.reddevx.quizin.ui.fragments.shop.gold_packs.GoldFragment
import com.reddevx.quizin.ui.fragments.shop.items.ItemsFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.models.UserShopData
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.utils.LoadingDialog

class ShopFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private val args:ShopFragmentArgs by navArgs()
    private val binding by lazy {FragmentShopBinding.inflate(layoutInflater)}

    private lateinit var currentUser: User
    private lateinit var loading:LoadingDialog
    private lateinit var userShopData: UserShopData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = args.userData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
        loading = LoadingDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shopToolbar.setNavigationOnClickListener {
            Navigation
                .findNavController(it)
                .popBackStack()
        }
        binding.shopViewpager.adapter = FragmentAdapter(this,currentUser)
        viewModel.apply {
            postGold(currentUser.gold)
            postGems(currentUser.gems)
        }

        TabLayoutMediator(binding.shopTabLayout,binding.shopViewpager) { tab, position ->
            when(position) {
                0 -> tab.text = "Items"
                1 -> tab.text = "Gold Packs"
                else -> tab.text = "Gems Packs"
            }
        }.attach()
        viewModel.shopGold.observe(viewLifecycleOwner) {
            binding.shopGoldTv.text = it.toString()
        }
        viewModel.shopGems.observe(viewLifecycleOwner) {
            binding.shopGemsTv.text = it.toString()
        }

    }



}

class FragmentAdapter(fragment: Fragment,private val user: User) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ItemsFragment.newInstance(user)
            1 -> GoldFragment.newInstance(user)
            else -> GemsFragment.newInstance(user)
        }
    }

}