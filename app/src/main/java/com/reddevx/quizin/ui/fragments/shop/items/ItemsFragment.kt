package com.reddevx.quizin.ui.fragments.shop.items

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.reddevx.quizin.R
import com.reddevx.quizin.data.getShopItems
import com.reddevx.quizin.data.models.ShopItem
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
        shopItemsAdapter = ShopItemsAdapter(getShopItems(),this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shopItemsRv.apply {
            adapter = shopItemsAdapter
            hasFixedSize()
        }
    }

    override fun onQuestionMarkClick(item: ShopItem) {
        showItemInfo(item)
    }

    override fun onBuyButtonClick(item: ShopItem) {
        showDialog(true,"Are you sure you want to buy this item ?",requireContext()) {
            Toast.makeText(requireContext(), "You have bought ${item.name} item for ${item.price}", Toast.LENGTH_SHORT).show()
            it.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showItemInfo(item: ShopItem) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.shop_item_explanation_dialog,binding.root,false)
        val closeBtn: Button = view.findViewById(R.id.shop_item_explanation_close_btn)
        val itemInfoTv: TextView = view.findViewById(R.id.shop_item_explanation_body_tv)
        val itemNameTv: TextView = view.findViewById(R.id.shop_item_explanation_name_tv)
        itemNameTv.text = "${item.name} info :"
        itemInfoTv.text = item.description
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