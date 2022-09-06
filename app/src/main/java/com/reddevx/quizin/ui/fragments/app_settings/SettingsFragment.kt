package com.reddevx.quizin.ui.fragments.app_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.SettingsFragmentBinding
import com.reddevx.quizin.listeners.SuccessListener
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.utils.LoadingDialog
import com.reddevx.quizin.utils.showDialog

class SettingsFragment : Fragment(), SuccessListener, UpdateUserListener {

    private val binding by lazy { SettingsFragmentBinding.inflate(layoutInflater) }
    private val viewModel: SettingsViewModel by activityViewModels()
    private val args: SettingsFragmentArgs by navArgs()
    private lateinit var loading: LoadingDialog

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = args.userData
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
        binding.appSettingsToolbar.setNavigationOnClickListener {
            Navigation
                .findNavController(requireView())
                .popBackStack()

        }

        loadUserData(user)

        binding.appSettingsSaveAppSettingsBtn.setOnClickListener {
            // Updating User Info!
            val updatedUsername = binding.appSettingsUsernameEdt.text.toString().trim()
            val updatedEmail = binding.appSettingsEmailEdt.text.toString().trim()
            val response = viewModel.validateUpdatedUserInfo(updatedUsername, updatedEmail)
            

            if (!response.isValidEmail or !response.isValidUsername) {
                binding.appSettingsEmailInputLayout.error = response.emailMessage
                binding.appSettingsUsernameInputLayout.error = response.usernameMessage
                return@setOnClickListener
            }

            // Valid User Info .. Updating User Data
            saveUserInfo(updatedUsername, updatedEmail)
            viewModel.updateUser(user, this)
            loading.startLoading()
        }

        binding.appSettingsDeleteAccountBtn.setOnClickListener {
            // Deleting User Account!
            showDialog(true,"Are you sure you want to delete your account ?",requireContext()) {dialog ->
                viewModel.deleteUserAccount(user.id, this)
                loading.startLoading()
                dialog.dismiss()
            }
        }

        binding.appSettingsEditAvatarCv.setOnClickListener {
            Navigation
                .findNavController(requireView())
                .navigate(SettingsFragmentDirections.appSettingsToAvatars(user))
        }

        binding.appSettingsLogOutBtn.setOnClickListener {
            showDialog(true, "Are you sure you want to sign out ?",requireContext()) { dialog ->
                viewModel.signOutCurrentUser()
                Navigation
                    .findNavController(requireView())
                    .navigate(SettingsFragmentDirections.appSettingsToLogin())
                dialog.dismiss()
            }
        }
    }

    private fun loadUserData(user: User) {
        binding.apply {
            Glide.with(requireView()).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(appSettingsAvatar)
            appSettingsUsernameEdt.setText(user.username)
            appSettingsEmailEdt.setText(user.email)
        }
    }

    private fun saveUserInfo(updatedUsername: String, updatedEmail: String) {
        user.apply {
            username = updatedUsername
            email = updatedEmail
        }
    }



    override fun onSuccess(any: Any?) {
        // Account Deleted Successfully!
        loading.close()
        Navigation
            .findNavController(requireView())
            .navigate(SettingsFragmentDirections.appSettingsToRegister())
        Toast.makeText(requireContext(), "Account Deleted Successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(e: Exception) {
        loading.close()
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onUserUpdatedSuccessfully(user: User) {
        loading.close()
        Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onUpdatingUserFailed(e: Exception) {
        loading.close()
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
    }


}