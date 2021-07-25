package com.tizzone.instantnewsapplication.presentation.ui.articles_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tizzone.instantnewsapplication.databinding.FragmentArticlesListBinding
import com.tizzone.instantnewsapplication.domain.data.DataState
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.presentation.ui.adapters.ArticlesRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class ArticlesListFragment : Fragment(), ArticlesRecyclerViewAdapter.Interaction {

    private var _binding: FragmentArticlesListBinding? = null

    //This property is only available between onCreateView & onDestroyView.
    private val binding get() = _binding!!

    //ViewModels
    private val viewModel: ArticlesListViewModel by viewModels()

    //Views
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var articlesAdapter: ArticlesRecyclerViewAdapter

    private var newJobs: Job? = null

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
        setObserver()
    }

    private fun setObserver() {
        newJobs?.cancel()
        newJobs = lifecycleScope.launch {
            val value = viewModel.stateArticles
            value.collectLatest {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { articles ->
                            articlesAdapter.submitData(articles)
                        }
                    }
                    DataState.Status.ERROR -> {
                        displayError(it.message)
                    }
                }
            }
        }
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        swipeRefreshLayout = binding.swipeList
        progressBar = binding.progressBarList
        val recyclerView: RecyclerView = binding.articlesList
        setupRecyclerView(recyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        articlesAdapter = ArticlesRecyclerViewAdapter(this@ArticlesListFragment)
        recyclerView.apply {
            adapter = articlesAdapter.apply {

            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemSelected(position: Int, article: Article) {
        TODO("Not yet implemented")
    }
}