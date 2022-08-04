package com.example.myquizapp.ui.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.myquizapp.R
import com.example.myquizapp.data.models.User
import com.example.myquizapp.databinding.LoginFragmentBinding
import com.example.myquizapp.listeners.SignInListener
import com.example.myquizapp.listeners.SuccessListener
import com.example.myquizapp.utils.LoadingDialog

class LoginFragment : Fragment(), View.OnClickListener, SignInListener, SuccessListener {

    private val binding by lazy {
        LoginFragmentBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: LoginViewModel
    private lateinit var loading: LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loading = LoadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginSignInBtn.setOnClickListener(this)
        binding.loginDoNotHaveAccountTv.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        autoSignInUser()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.login_sign_in_btn -> {
                val email = binding.loginEmailEdt.text.toString().trim()
                val password = binding.loginPasswordEdt.text.toString().trim()
                val response = viewModel.validateLogin(email, password)

                if (!response.isValidEmail or !response.isValidPassword) {
                    // Login validation failed
                    binding.loginEmailInputLayout.error = response.emailMessage
                    binding.loginPasswordInputLayout.error = response.passwordMessage
                    return
                }

                // login validation succeeded
                viewModel.signInUser(email, password, this)
                loading.createLoadingDialog()

            }

            R.id.login_do_not_have_account_tv -> {
                Navigation
                    .findNavController(view)
                    .navigate(LoginFragmentDirections.loginToRegister())
            }

        }
    }

    override fun onUserSignedInSuccessfully(user: User) {
        // User signed in successfully
        loading.close()
        Navigation
            .findNavController(requireView())
            .navigate(LoginFragmentDirections.loginToExplore(user))
    }

    override fun onUserSigningFailed(e: Exception?) {
        // User signing failed
        loading.close()
        Toast.makeText(requireContext(), "Error : ${e?.message}", Toast.LENGTH_SHORT).show()
    }

    private fun autoSignInUser() {
        val currentUser = viewModel.getCurrentUser()
        if (currentUser != null) {
            viewModel.getUserData(currentUser.uid, this)
            loading.createLoadingDialog()
        }
    }

    override fun onSuccess(any: Any?) {
        val user = any as User
        Navigation
            .findNavController(requireView())
            .navigate(LoginFragmentDirections.loginToExplore(user))
        loading.close()
    }

    override fun onFailure(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
        loading.close()
    }


}