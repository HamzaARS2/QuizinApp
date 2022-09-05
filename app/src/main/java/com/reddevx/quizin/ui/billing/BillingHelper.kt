package com.reddevx.quizin.ui.billing

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable

class BillingHelper(
    private val activity: Activity,
    private val products: MutableList<QueryProductDetailsParams.Product>,
    private val mListener: ProductsListener

) : PurchasesUpdatedListener, BillingClientStateListener, ConsumeResponseListener {


    private val mutableProductsDetails: MutableLiveData<List<ProductDetails>> = MutableLiveData()
    val productDetails:LiveData<List<ProductDetails>> get() = mutableProductsDetails

    private val billingClient: BillingClient = BillingClient.newBuilder(activity)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    init {
        billingClient.startConnection(this)
    }


    override fun onBillingSetupFinished(result: BillingResult) {
        // Connected to Play Store successfully!
        val responseCode = result.responseCode
        val debugMessage = result.debugMessage
        Log.d("BillingHelper", "onBillingSetupFinished: $debugMessage")
        if (responseCode == BillingClient.BillingResponseCode.OK) {
            queryProductDetails()
            Log.d("BillingHelper", "Response OK")
        }
    }

    override fun onBillingServiceDisconnected() {
        // Connection Failed!!
        Log.d("BillingHelper", "Disconnected")

    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        // Purchase Complete
        val response = result.responseCode
        if (response == BillingClient.BillingResponseCode.OK && purchases != null) {
            mListener.onPurchaseComplete()
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (response == BillingClient.BillingResponseCode.USER_CANCELED) {

        } else {

        }
    }

    private fun queryProductDetails() {
        Log.d("BillingHelper", "Querying Products")

        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build()
        billingClient.queryProductDetailsAsync(queryProductDetailsParams
        ) { result, productDetailsList ->
            //                    mListener.onProductsDetailsResponse(result, productDetailsList)
            //                    Log.d("BillingHelper", "size = ${productDetailsList.size}")
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    mutableProductsDetails.postValue(productDetailsList)
            }
        }
    }

    fun startPurchase(productDetails: ProductDetails) {
        Log.d("BillingHelper", "Starting Purchase")

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    private fun handlePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.consumeAsync(consumeParams, this)
    }

    interface ProductsListener {
        fun onProductsDetailsResponse(result: BillingResult, products: MutableList<ProductDetails>)
        fun onPurchaseComplete()
        fun onPurchaseConsumed()
    }

    override fun onConsumeResponse(result: BillingResult, purchaseToken: String) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.d("BillingHelper", "Product Consumed")

            mListener.onPurchaseConsumed()
        }
    }


}