package com.reddevx.quizin.ui.fragments.shop.items

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.reddevx.quizin.R
import com.reddevx.quizin.databinding.FragmentItemsBinding
import com.reddevx.quizin.ui.fragments.shop.ShopViewModel
import com.reddevx.quizin.utils.showDialog

class ItemsFragment : Fragment(), ItemListener {

    companion object {
        fun newInstance() = ItemsFragment()
    }

    private lateinit var viewModel: ShopViewModel
    private val binding by lazy {FragmentItemsBinding.inflate(layoutInflater)}
    private lateinit var shopItemsAdapter: ShopItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
        shopItemsAdapter = ShopItemsAdapter(arrayListOf("1","2","3","4"),this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shopItemsRv.apply {
            adapter = shopItemsAdapter
            hasFixedSize()
        }
    }

    override fun onQuestionMarkClick(item: String) {
        showItemInfo()
    }

    private fun showItemInfo() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.shop_item_explanation_dialog,binding.root,false)
        val closeBtn: Button = view.findViewById(R.id.shop_item_explanation_close_btn)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

    }


}