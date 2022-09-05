package com.reddevx.quizin.ui.fragments.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.RegisterFragmentBinding
import com.reddevx.quizin.listeners.UserListener
import com.reddevx.quizin.utils.LoadingDialog
import java.lang.Exception


class RegisterFragment : Fragment(), View.OnClickListener, UserListener {

    private val viewModel: RegisterViewModel by activityViewModels()
    private lateinit var loading: LoadingDialog


    private val binding by lazy {
        RegisterFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loading = LoadingDialog(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerSignUpBtn.setOnClickListener(this)
        binding.registerAlreadySignedInTv.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.register_sign_up_btn -> {
                val username = binding.registerUsernameEdt.text.toString().trim()
                val email = binding.registerEmailEdt.text.toString().trim()
                val password = binding.registerPasswordEdt.text.toString().trim()
                val response = viewModel.validateRegister(username, email, password)

                if (!response.isValidEmail or !response.isValidPassword or !response.isValidUsername) {
                    // Registering failed
                    binding.registerUsernameInputLayout.error = response.usernameMessage
                    binding.registerEmailInputLayout.error = response.emailMessage
                    binding.registerPasswordInputLayout.error = response.passwordMessage
                    //loading.close()
                    return
                }

                // Registering user
                viewModel.registerUser(username, email, password, this)
                loading.createLoadingDialog()

            }

            R.id.register_already_signed_in_tv -> {
                Navigation
                    .findNavController(view)
                    .navigate(RegisterFragmentDirections.registerToLogin())
            }
        }
    }

    override fun onUserCreatedSuccessfully(user: User) {
        // User successfully Registered
        Navigation
            .findNavController(requireView())
            .navigate(RegisterFragmentDirections.registerToAvatars(user))
        loading.close()
    }

    override fun onUserCreationFailed(e: Exception?) {
        // Error occurred while registering
        Toast.makeText(requireContext(), "Error : ${e?.message}", Toast.LENGTH_SHORT).show()
    }


}