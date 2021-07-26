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
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tizzone.instantnewsapplication.R
import com.tizzone.instantnewsapplication.databinding.FragmentArticlesListBinding
import com.tizzone.instantnewsapplication.domain.data.DataState
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.presentation.ui.adapters.ArticlesRecyclerViewAdapter
import com.tizzone.instantnewsapplication.utils.ARG_ARTICLE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber

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
        //Fetch data from ViewModel
        newJobs = lifecycleScope.launch {
            val value = viewModel.stateArticles
            value.collectLatest {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { articles ->
                            articlesAdapter.submitData(articles)
                        }
                    }
                    DataState.Status.LOADING -> {
                        //Do Something
                    }

                    DataState.Status.ERROR -> {
                        displayError(it.message)
                    }
                }
            }
        }
        lifecycleScope.launch {
            articlesAdapter.loadStateFlow
                //Only emit when refresh loadState for change
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Loading }
                .collect { binding.articlesList.scrollToPosition(0) }
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
        setupSwipeListener()
    }

    private fun setupSwipeListener() {
        swipeRefreshLayout.setOnRefreshListener {
            articlesAdapter.refresh()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        articlesAdapter = ArticlesRecyclerViewAdapter(this@ArticlesListFragment)
        recyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = articlesAdapter.apply {
                addLoadStateListener { loadState ->
                    if (loadState.refresh is LoadState.Loading) {
                        //Show progressBar or swipe on load
                        progressBar.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = isVisible
                    } else {
                        //Hide progressBar or swipe on load
                        progressBar.visibility = View.GONE
                        swipeRefreshLayout.isRefreshing = isHidden
                        binding.noDataText?.visibility = View.GONE

                        //Getting the error
                        val errorState = when {
                            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                            else -> null
                        }
                        errorState?.let {
                            Toast.makeText(
                                requireContext(),
                                "Error; ${it.error.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.noDataText?.visibility = View.VISIBLE
                        }
                    }
                }
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Click listener on recyclerview item
    override fun onItemSelected(position: Int, article: Article) {
        Timber.d("Click Article position: $position & ${article.title}")
        val bundle = Bundle()
        bundle.putParcelable(ARG_ARTICLE_ID, article)
        view?.findNavController()?.navigate(R.id.articleDetailFragment, bundle)
    }
}