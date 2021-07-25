package com.tizzone.instantnewsapplication.presentation.ui.articles_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tizzone.instantnewsapplication.databinding.FragmentArticlesListBinding
import com.tizzone.instantnewsapplication.presentation.ui.adapters.ArticlesRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class ArticlesListFragment : Fragment() {

    private var columnCount = 1
    private var _binding: FragmentArticlesListBinding? = null

    //This property is only available between onCreateView & onDestroyView.
    private val binding get() = _binding!!

    //ViewModels
    private val viewModel: ArticlesListViewModel by viewModels()

    //Views
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var progressBar: ProgressBar
    lateinit var articlesAdapter: ArticlesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()


    }

    private fun initViews() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        swipeRefreshLayout = binding.swipeList
        progressBar = binding.progressBarList
        val recyclerView: RecyclerView = binding.articlesList
        setupRecyclerView(recyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        articlesAdapter = ArticlesRecyclerViewAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ArticlesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}