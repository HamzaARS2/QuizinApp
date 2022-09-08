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
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.FragmentItemsBinding
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.ui.fragments.shop.ShopViewModel
import com.reddevx.quizin.utils.LoadingDialog
import com.reddevx.quizin.utils.showDialog
import com.reddevx.quizin.utils.showPopup

class ItemsFragment : Fragment(), ItemListener, UpdateUserListener {

    companion object {
        fun newInstance(user: User) = ItemsFragment().apply {
            arguments = Bundle().apply {
                putSerializable("currentUser",user)
            }
        }
    }

    private lateinit var viewModel: ShopViewModel
    private val binding by lazy {FragmentItemsBinding.inflate(layoutInflater)}
    private lateinit var shopItemsAdapter: ShopItemsAdapter
    private lateinit var currentUser: User

    private lateinit var loading:LoadingDialog
    private var boughtItem: ShopItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentUser = it.getSerializable("currentUser") as User
        }
        loading = LoadingDialog(requireContext())
    }
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
            if (viewModel.hasEnoughGold(currentUser,item.price)) {
                loading.startLoading()
                boughtItem = item
                buyItem(item)
                it.dismiss()
            }else {
                showPopup("Sorry, you don't have enough gold to buy this item",true,requireContext())
            }
        }
    }

    private fun buyItem(item: ShopItem) {
        val isFullInventory = viewModel.buyShopItem(currentUser,item,this)
        if (isFullInventory) {
            showPopup("Your inventory is full", true, requireContext())
            loading.close()
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

    override fun onUserUpdatedSuccessfully(user: User) {
        loading.close()
        viewModel.postGold(user.gold)
        if (boughtItem != null) {
            Toast.makeText(
                requireContext(),
                "You have bought ${boughtItem!!.name} item for ${boughtItem!!.price} Gold",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onUpdatingUserFailed(e: Exception) {
        loading.close()
        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }


}