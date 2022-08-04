package com.example.myquizapp.ui.fragments.app_settings

import android.app.AlertDialog
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
import com.example.myquizapp.R
import com.example.myquizapp.data.models.User
import com.example.myquizapp.databinding.SettingsFragmentBinding
import com.example.myquizapp.listeners.SuccessListener
import com.example.myquizapp.listeners.UpdateUserListener
import com.example.myquizapp.utils.LoadingDialog

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
            loading.createLoadingDialog()
        }

        binding.appSettingsDeleteAccountBtn.setOnClickListener {
            // Deleting User Account!
            showDeleteAccountDialog()
        }

        binding.appSettingsEditAvatarCv.setOnClickListener {
            Navigation
                .findNavController(requireView())
                .navigate(SettingsFragmentDirections.appSettingsToAvatars(user))
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

    private fun showDeleteAccountDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setMessage("Are you sure you want to delete your account ?")
            .setPositiveButton(R.string.yes) { dialog, _ ->
                viewModel.deleteUserAccount(user.id, this)
                loading.createLoadingDialog()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
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