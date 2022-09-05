package com.reddevx.quizin.ui.fragments.shop.gold_packs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reddevx.quizin.databinding.FragmentGoldBinding
import com.reddevx.quizin.ui.fragments.shop.ShopViewModel
import com.reddevx.quizin.utils.GoldPack
import com.reddevx.quizin.utils.getGoldPacks

class GoldFragment : Fragment(), GoldPackAdapter.GoldClickListener {

    companion object {
        fun newInstance() = GoldFragment()
    }

    private lateinit var viewModel: ShopViewModel
    private val binding by lazy{FragmentGoldBinding.inflate(layoutInflater)}
    private lateinit var goldAdapter: GoldPackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
        goldAdapter = GoldPackAdapter(getGoldPacks(),this)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goldRv.apply {
            adapter = goldAdapter
            hasFixedSize()
        }
    }

    override fun onGoldPackClick(goldPack: GoldPack) {
        viewModel.postGold(goldPack.goldAmount)
    }


}