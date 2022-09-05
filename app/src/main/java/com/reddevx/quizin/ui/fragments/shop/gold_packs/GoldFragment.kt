package com.reddevx.quizin.ui.fragments.shop.gold_packs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.FragmentGoldBinding
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.ui.fragments.shop.ShopViewModel
import com.reddevx.quizin.utils.GoldPack
import com.reddevx.quizin.utils.LoadingDialog
import com.reddevx.quizin.utils.getGoldPacks
import com.reddevx.quizin.utils.showDialog

class GoldFragment : Fragment(), GoldPackAdapter.GoldClickListener, UpdateUserListener {

    companion object {
        fun newInstance(user: User) =
            GoldFragment().apply {
            arguments = Bundle().apply {
                putSerializable("currentUser", user)
            }
        }
    }

    private lateinit var viewModel: ShopViewModel
    private val binding by lazy{FragmentGoldBinding.inflate(layoutInflater)}
    private lateinit var goldAdapter: GoldPackAdapter
    private lateinit var goldPack: GoldPack

    private lateinit var loading:LoadingDialog

    private lateinit var currentUser:User
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
        showDialog(true,"Are you sure you want to buy this pack ?",requireContext()) {
            this.goldPack = goldPack
            currentUser.gold = currentUser.gold + goldPack.goldAmount
            viewModel.saveUserData(currentUser,this)
            it.dismiss()
            loading.createLoadingDialog()
        }

    }

    override fun onUserUpdatedSuccessfully(user: User) {
        if (context != null)
            Toast.makeText(context, "You have bought ${goldPack.goldAmount} Gold", Toast.LENGTH_SHORT).show()
        loading.close()
        viewModel.postGold(user.gold)
    }

    override fun onUpdatingUserFailed(e: Exception) {
        loading.close()
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
    }


}