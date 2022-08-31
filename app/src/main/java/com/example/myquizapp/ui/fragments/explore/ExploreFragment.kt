package com.example.myquizapp.ui.fragments.explore

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myquizapp.R
import com.example.myquizapp.databinding.FragmentExploreBinding
import com.google.android.material.appbar.AppBarLayout
import com.example.myquizapp.data.models.*
import com.example.myquizapp.listeners.CategoriesListener
import com.example.myquizapp.listeners.LatestQuizzesListener
import com.example.myquizapp.ui.fragments.explore.adapters.CategoriesAdapter
import com.example.myquizapp.ui.fragments.explore.adapters.LatestQuizzesAdapter
import com.example.myquizapp.utils.hideNavigationBar
import com.example.myquizapp.utils.setViewInsets
import com.example.myquizapp.utils.showNavigationBar
import kotlin.math.abs


class ExploreFragment : Fragment(), View.OnClickListener, CategoriesListener,
    LatestQuizzesListener, CategoriesAdapter.ExploreCategoryListener,
    LatestQuizzesAdapter.LatestQuizListener {


    private lateinit var latestQuizzesAdapter: LatestQuizzesAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val args: ExploreFragmentArgs by navArgs()
    private lateinit var currentUser: User

    private val viewModel: ExploreViewModel by activityViewModels()

    private val binding by lazy {
        FragmentExploreBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewInsets(binding.appBarMain)
        currentUser = args.userData

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarScrollingSystem()
        viewModel.getCategories(this)
        viewModel.getLatestQuizzes(this)

        initAdapters()
        prepareViews()
        viewModel.setUserData(currentUser)

        viewModel.userLiveData.observe(viewLifecycleOwner) {
            loadData(it)
        }

        binding.exploreGoUpBtn.setOnClickListener(this)

        //binding.signOutIconInclude.signOutIconLayout.setOnClickListener(this)
        binding.apply {
            profileInfoCv.setOnClickListener(this@ExploreFragment)
            exploreToolbarUserInfoCv.setOnClickListener(this@ExploreFragment)
            settingsIconInclude.settingsIconLayout.setOnClickListener(this@ExploreFragment)
            shopIconInclude.shopIconLayout.setOnClickListener(this@ExploreFragment)
            exploreCollapsingTb.setContentScrimColor(Color.parseColor("#241c5c"))
        }



    }

    @SuppressLint("SetTextI18n")
    private fun loadData(user: User) {
        binding.apply {
            exploreUsernameTv.text = user.username
            exploreUserLvlBadgeTv.text = "Lv.${user.level}  ${user.badge}"
            exploreUserCorrectTv.text = user.correct.toString()
            exploreUserTrophies.text = user.trophies.toString()
            exploreUserWrongTv.text = user.wrong.toString()
            Glide.with(requireView()).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(binding.exploreUserImage)

        }
        loadToolbarData(user)
    }

    @SuppressLint("SetTextI18n")
    private fun loadToolbarData(user: User) {
        binding.apply {
            exploreToolbarUserName.text = user.username
            exploreToolbarUserBadge.text = "Lv.${user.level}  ${user.badge}"
//            exploreToolbarUserCorrects.text = getConvertedValue(user.correct)
//            exploreToolbarUserTrophies.text = getConvertedValue(user.trophies)
//            exploreToolbarUserWrongs.text = getConvertedValue(user.wrong)
            Glide.with(requireView()).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(binding.exploreToolbarUserImage)
        }
    }

    private fun getConvertedValue(number: Int): String {
        val strValue = number.toString()
        val length = strValue.length
        var convertedValue = ""
        if (length >= 4) {
            for (i in 4..length) {
                convertedValue += strValue[i - 4].toString()
            }
            convertedValue += "k"
            return convertedValue
        }
        return strValue
    }

    private fun setToolbarScrollingSystem() {
        var isShow = true
        var scrollRange = -1
        binding.appBarMain.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
                hideNavigationBar(requireActivity().window)
            }
            if (scrollRange + verticalOffset == 0) {
                binding.MainToolbar.visibility = View.VISIBLE
                binding.mainCoordinator.setBackgroundColor(Color.parseColor("#241c5c"))
                showNavigationBar(requireActivity().window)
                isShow = true
            } else if (isShow) {
                binding.MainToolbar.visibility = View.INVISIBLE
                binding.mainCoordinator.setBackgroundResource(R.drawable.dark_purple_layout_bg)
                isShow = false
            }
        })
    }

    private fun initAdapters() {
        latestQuizzesAdapter = LatestQuizzesAdapter(mListener = this)
        categoriesAdapter = CategoriesAdapter(mListener = this)
    }

    private fun prepareViews() {
        binding.exploreLatestQuizzesVp.apply {
            adapter = latestQuizzesAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(getPageTransformer())

            currentItem = 1
        }

        binding.exploreCategoriesRv.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                GridLayoutManager.HORIZONTAL, false
            )
            hasFixedSize()
        }
    }

    private fun getPageTransformer() = CompositePageTransformer().apply {
        addTransformer(MarginPageTransformer(40))
        addTransformer { page, position ->
            val radius = 1 - abs(position)
            page.scaleY = 0.85f + (radius * 0.18f)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.profile_info_cv -> {
                Navigation
                    .findNavController(view)
                    .navigate(ExploreFragmentDirections.exploreToProfile(currentUser))
            }
            R.id.explore_go_up_btn -> {
                binding.appBarMain.setExpanded(true)
            }
            R.id.explore_toolbar_user_info_cv -> {
                Navigation
                    .findNavController(view)
                    .navigate(ExploreFragmentDirections.exploreToProfile(currentUser))
            }
            R.id.settings_icon_include -> {
                Navigation
                    .findNavController(view)
                    .navigate(ExploreFragmentDirections.exploreToAppSettings(currentUser))
            }
            R.id.shop_icon_include -> {
                Navigation
                    .findNavController(view)
                    .navigate(ExploreFragmentDirections.exploreToShop())
            }

        }
    }



    // Getting Available Categories
    override fun onRetrievingCategoriesCompleted(categories: ArrayList<Category>) {
        categoriesAdapter.setCategories(categories)

        binding.apply {
            exploreCategoriesProgress.visibility = View.GONE
            exploreCategoriesRv.visibility = View.VISIBLE
        }
    }

    override fun onRetrievingCategoriesFailed(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()

    }

    // Getting Available Latest Quizzes
    override fun onLatestQuizzesRetrievedSuccessfully(latestQuizzes: ArrayList<LatestQuiz>) {
        latestQuizzesAdapter.setLatestQuizzes(latestQuizzes)
        binding.apply {
            exploreLatestQuizzesProgress.visibility = View.GONE
            exploreLatestQuizzesVp.visibility = View.VISIBLE
        }
    }

    override fun onRetrievingLatestQuizzesFailed(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onExploreCategoryClick(category: Category) {
        // Category Explore Clicked!
        Navigation
            .findNavController(requireView())
            .navigate(ExploreFragmentDirections.exploreToCategoryQuizzes(category))
    }

    override fun onLatestQuizPlayClick(latestQuiz: LatestQuiz) {
        // latest Quiz Clicked!
        Navigation
            .findNavController(requireView())
            .navigate(
                ExploreFragmentDirections.exploreToQuizSettings(
                    latestQuiz.id,
                    latestQuiz.category.id
                )
            )
    }


}