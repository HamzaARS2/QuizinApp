package com.reddevx.quizin.ui.fragments.shop.gem_packs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.QuizProduct
import com.reddevx.quizin.databinding.FragmentGemsBinding
import com.reddevx.quizin.ui.billing.BillingHelper
import com.reddevx.quizin.utils.LoadingDialog
import com.reddevx.quizin.utils.getProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GemsFragment : Fragment(), BillingHelper.ProductsListener, GemPackListener {

    companion object {
        fun newInstance() = GemsFragment()
    }

    private lateinit var viewModel: GemsViewModel
    private val binding by lazy { FragmentGemsBinding.inflate(layoutInflater) }
    private lateinit var gemsAdapter: GemPackAdapter

    private lateinit var billingHelper: BillingHelper

    private lateinit var loading: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[GemsViewModel::class.java]
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
            Log.d("GemsFragment", "onProductsDetailsResponse:size is ${productsDetails.size}")
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
                        gemPacks.add(QuizProduct(product, 1500, R.drawable.shop_gems_pack_lvl3))
                    }
                }
            }

            Log.d("GemsFragment", "onProductsDetailsResponse: gems size = ${gemPacks.size}")

            // Toast.makeText(requireContext(), gemPacks.size, Toast.LENGTH_SHORT).show()
            gemsAdapter.setProducts(gemPacks)
        }
    }

    override fun onGemPackClick(product: QuizProduct) {
        billingHelper.startPurchase(product.productDetails)
    }

    override fun onProductsDetailsResponse(
        result: BillingResult,
        products: MutableList<ProductDetails>
    ) {





    }

    override fun onPurchaseComplete() {
        loading.createLoadingDialog()
        Toast.makeText(requireActivity(), "Purchase Complete", Toast.LENGTH_SHORT).show()
    }

    override fun onPurchaseConsumed() {
        loading.close()
        Log.d("GemsFragment", "onPurchaseConsumed: Product Consumed")
    }


}