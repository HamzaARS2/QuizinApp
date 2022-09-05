package com.reddevx.quizin.ui.fragments.category_quizzes

import android.graphics.Color
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.reddevx.quizin.R
import com.reddevx.quizin.databinding.FragmentCategoryQuizzesBinding
import com.google.android.material.appbar.AppBarLayout
import com.reddevx.quizin.data.models.*
import com.reddevx.quizin.listeners.QuizzesListener
import com.reddevx.quizin.utils.setViewInsets


class CategoryQuizzesFragment : Fragment(), QuizzesAdapter.QuizPlayListener, QuizzesListener,
    RequestListener<Drawable> {

    private val binding by lazy {
        FragmentCategoryQuizzesBinding.inflate(layoutInflater)
    }

    private val args: CategoryQuizzesFragmentArgs by navArgs()
    private val viewModel: CategoryQuizzesViewModel by activityViewModels()
    private lateinit var category: Category
    private lateinit var quizzesAdapter: QuizzesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewInsets(binding.categoryQuizzesAppBar)
        category = args.categoryData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quizzesAdapter = QuizzesAdapter(category = category, mListener = this)
        viewModel.getQuizzes(category.id, this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Loading category data
        loadCategoryData(category)
        binding.categoryQuizzesRv.apply {
            adapter = quizzesAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            hasFixedSize()
        }
        binding.categoryQuizzesToolbar.apply {
            setNavigationIcon(R.drawable.ic_back_arrow)
            setNavigationIconTint(Color.WHITE)
            setNavigationOnClickListener {
                Navigation
                    .findNavController(requireView())
                    .popBackStack()
            }
        }
        binding.categoryQuizzesTbTv.text = category.name
        setViewsVisibility()

    }

    private fun loadCategoryData(category: Category) {
        binding.apply {
            categoryNameTv.text = category.name
            categoryQuizzesDescriptionTv.text = category.description
            Glide
                .with(this@CategoryQuizzesFragment)
                .load(category.categoryLogo)
                .into(categoryQuizzesImageType)
            Glide
                .with(this@CategoryQuizzesFragment)
                .load(category.layoutBg)
                .listener(this@CategoryQuizzesFragment)
                .into(categoryQuizzesBackgroundImage)

        }
    }

    private fun setViewsVisibility() {
        var isVisible = true
        var scrollRange = -1
        binding.categoryQuizzesAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange
            if (scrollRange + verticalOffset == 0) {
                // scrolled up
                binding.apply {
                    categoryQuizzesCollapsingCl.visibility = View.INVISIBLE
                    categoryQuizzesTbTv.visibility = View.VISIBLE
                }
                isVisible = true
            } else if (isVisible) {
                // scrolled down
                binding.apply {
                    categoryQuizzesCollapsingCl.visibility = View.VISIBLE
                    categoryQuizzesTbTv.visibility = View.GONE
                }
                isVisible = false
            }
        })
    }


    override fun onQuizPlayClick(quiz: Quiz) {
        Navigation
            .findNavController(requireView())
            .navigate(
                CategoryQuizzesFragmentDirections.categoryToQuizSettings(
                    quiz.id,
                    category.id
                )
            )
    }

    override fun onRetrievingQuizzesCompleted(quizzes: ArrayList<Quiz>) {
        binding.apply {
            categoryQuizzesQuizzesProgress.visibility = View.INVISIBLE
            categoryQuizzesRv.visibility = View.VISIBLE
        }
        quizzesAdapter.setQuizzes(quizzes)
    }

    override fun onRetrievingQuizzesFailed(e: Exception) {
        // Retrieving Quizzes Failed!
        Toast.makeText(requireContext(), "Error ${e.message}", Toast.LENGTH_SHORT).show()
        binding.apply {
            categoryQuizzesQuizzesProgress.visibility = View.INVISIBLE
            categoryQuizzesRv.visibility = View.VISIBLE
        }
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        binding.categoryQuizzesBgProgress.visibility = View.INVISIBLE
        return false
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        binding.categoryQuizzesBgProgress.visibility = View.INVISIBLE
        return false
    }


}