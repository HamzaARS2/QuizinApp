package com.reddevx.quizin.ui.fragments.shop.gem_packs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.QuizProduct
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.FragmentGemsBinding
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.ui.billing.BillingHelper
import com.reddevx.quizin.ui.fragments.shop.ShopViewModel
import com.reddevx.quizin.utils.LoadingDialog
import com.reddevx.quizin.utils.getProducts

class GemsFragment : Fragment(), BillingHelper.ProductsListener, GemPackListener,
    UpdateUserListener {

    companion object {
        fun newInstance(user: User) =
            GemsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("currentUser", user)
                }
            }
    }

    private lateinit var viewModel: ShopViewModel
    private val binding by lazy { FragmentGemsBinding.inflate(layoutInflater) }
    private lateinit var gemsAdapter: GemPackAdapter

    private lateinit var billingHelper: BillingHelper

    private lateinit var loading: LoadingDialog

    private lateinit var currentUser: User
    private lateinit var product: QuizProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentUser = it.getSerializable("currentUser") as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
        billingHelper = BillingHelper(requireActivity(), getProducts(), this)
        gemsAdapter = GemPackAdapter(mListener = this)

        loading = LoadingDialog(requireContext())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gemsRv.apply {
            adapter = gemsAdapter
            hasFixedSize()
        }

        billingHelper.productDetails.observe(viewLifecycleOwner) { productsDetails ->
            val gemPacks: ArrayList<QuizProduct> = arrayListOf()
            for (product in productsDetails) {
                when (product.productId) {
                    "gems_pack_lvl1" -> {
                        gemPacks.add(QuizProduct(product, 500))
                    }
                    "gems_pack_lvl2" -> {
                        gemPacks.add(QuizProduct(product, 1000, R.drawable.shop_gems_pack_lvl2))
                    }
                    else -> {
                        // gems_pack_lvl3
                        gemPacks.add(QuizProduct(product, 1500, R.drawable.shop_gems_pack_lvl3))
                    }
                }
            }
            gemsAdapter.setProducts(gemPacks)
            // Hide Progress
            binding.shopGemsProgress.visibility = View.GONE
        }
    }

    override fun onGemPackClick(product: QuizProduct) {
        this.product = product
        billingHelper.startPurchase(product.productDetails)
    }


    override fun onPurchaseComplete() {
        loading.startLoading()
    }

    override fun onPurchaseConsumed() {
        currentUser.gems = currentUser.gems + product.gemsAmount
        viewModel.saveUserData(currentUser,this)
        Log.d("GemsFragment", "onPurchaseConsumed: Product Consumed")

    }

    override fun onUserUpdatedSuccessfully(user: User) {
        if (context != null)
        Toast.makeText(context, "You have bought ${product.gemsAmount} Gems", Toast.LENGTH_SHORT).show()
        loading.close()
        viewModel.postGems(user.gems)

    }

    override fun onUpdatingUserFailed(e: Exception) {
        if (context != null)
        Toast.makeText(context, "Error : ${e.message}", Toast.LENGTH_SHORT).show()
        loading.close()
    }


}