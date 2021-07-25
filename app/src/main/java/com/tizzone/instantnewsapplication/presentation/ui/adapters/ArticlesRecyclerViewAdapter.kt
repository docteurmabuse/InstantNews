package com.tizzone.instantnewsapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tizzone.instantnewsapplication.databinding.FragmentArticleItemBinding
import com.tizzone.instantnewsapplication.domain.model.Article

/**
 * [RecyclerView.Adapter] that can display an [Article].
 * TODO: Replace the implementation with code for your data type.
 */
class ArticlesRecyclerViewAdapter(

) : PagingDataAdapter<Article, ArticlesRecyclerViewAdapter.ArticleViewHolder>(ARTICLES_COMPARATOR) {

    companion object {
        private val ARTICLES_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            FragmentArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val articles = getItem(position)
        holder.itemView.apply {
            if (articles != null) {
                holder.idView.text = articles.title
                holder.contentView.text = articles.description
            }
        }
    }

    inner class ArticleViewHolder(binding: FragmentArticleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}