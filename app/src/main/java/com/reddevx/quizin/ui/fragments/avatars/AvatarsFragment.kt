package com.reddevx.quizin.ui.fragments.avatars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.reddevx.quizin.data.models.Avatar
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.AvatarsFragmentBinding
import com.reddevx.quizin.listeners.AvatarsListener
import com.reddevx.quizin.listeners.SuccessListener

class AvatarsFragment : Fragment(), AvatarsListener, AvatarsAdapter.AvatarClickListener,
    SuccessListener {

    private val viewModel: AvatarsViewModel by activityViewModels()
    private val args: AvatarsFragmentArgs by navArgs()

    private lateinit var user: User
    private var selectedAvatar: String = ""
    private val binding by lazy {
        AvatarsFragmentBinding.inflate(layoutInflater)
    }

    private lateinit var avatarsAdapter: AvatarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = args.userData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        avatarsAdapter = AvatarsAdapter(mListener = this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAvatars(this)
        binding.avatarsRv.apply {
            adapter = avatarsAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            hasFixedSize()
        }

        binding.avatarsConfirmBtn.setOnClickListener {
            if (selectedAvatar.isNotEmpty()) {
                user.avatar = selectedAvatar
                viewModel.setUserAvatar(user.id, user.avatar, this)
                binding.apply {
                    avatarsConfirmBtn.visibility = View.INVISIBLE
                    avatarsConfirmProgress.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onAvatarsReceivedSuccessfully(avatar: Avatar) {
        avatarsAdapter.addAvatar(avatar)
    }

    override fun onReceivingAvatarsFailed(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onAvatarSelected(avatar: Avatar) {
        Glide.with(this).load(avatar.avatarUrl).into(binding.avatarsSelectedAvatarImage)
        selectedAvatar = avatar.avatarUrl
        binding.avatarsConfirmBtn.isEnabled = true
    }

    override fun onSuccess(any: Any?) {
        binding.apply {
            avatarsConfirmBtn.visibility = View.VISIBLE
            avatarsConfirmProgress.visibility = View.INVISIBLE
        }
        Navigation
            .findNavController(requireView())
            .navigate(AvatarsFragmentDirections.avatarsToExplore(user))
    }

    override fun onFailure(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
        binding.apply {
            avatarsConfirmBtn.visibility = View.VISIBLE
            avatarsConfirmProgress.visibility = View.INVISIBLE
        }
    }


}