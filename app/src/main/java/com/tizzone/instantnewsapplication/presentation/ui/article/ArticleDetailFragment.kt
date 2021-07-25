package com.tizzone.instantnewsapplication.presentation.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.load
import com.tizzone.instantnewsapplication.R
import com.tizzone.instantnewsapplication.databinding.FragmentArticleDetailBinding
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.utils.ARG_ARTICLE_ID


/**
 * A simple [Fragment] subclass.
 * Use the [ArticleDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleDetailFragment : Fragment() {
    private var _binding: FragmentArticleDetailBinding? = null

    //This property is only available between onCreateView & onDestroyView.
    private val binding get() = _binding!!

    //Views
    lateinit var webViewClient: WebViewClient

    //Properties
    private var article: Article? = null
    private var isWebView: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            if (bundle.containsKey(ARG_ARTICLE_ID)) {
                bundle.getParcelable<Article>(ARG_ARTICLE_ID)?.let {
                    this.article = it
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.run {
            //Set back button and action bar
            (activity as AppCompatActivity).setSupportActionBar(detailToolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbarLayout.title = context?.resources?.getString(R.string.app_name)
            articleDetailTitleText.text = article?.title
            articleDetailDescriptionText.text= article?.description
            articleDetailImage.load(article?.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_baseline_image_24)
                fallback(R.drawable.ic_baseline_image_24)
            }
            seeMoreFab.setOnClickListener {
                setSeeMoreContent()
            }
        }
    }

    private fun setSeeMoreContent() {
        binding.run {
            if (!isWebView) {
                isWebView = true
                seeMoreFab.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                webView.visibility = View.VISIBLE
                articleDetailConstraint.visibility = View.GONE
                webView.apply {
                    webViewClient = WebViewClient()
                    article?.url?.let {
                        loadUrl(it)
                    }
                }
            } else {
                isWebView = false
                webView.visibility = View.GONE
                articleDetailConstraint.visibility = View.VISIBLE
                seeMoreFab.setImageResource(R.drawable.ic_baseline_arrow_forward_24)
            }
        }
    }
}