package com.example.newsapp.ui.news.adapter

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.util.Constant.URL
import com.example.newsapp.util.Utils.getOptionsForGlide
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_news.view.*

class NewsItem(
    private val newsArticle: Article,
    val fragmentActivity: FragmentActivity,val context: Context
) : Item() {

    override fun bind(holder: GroupieViewHolder, position: Int) {

        holder.itemView.tv_news_title.text = newsArticle.title
        holder.itemView.tv_news_description.text = newsArticle.description

        Glide.with(fragmentActivity)
            .load(newsArticle.urlToImage).apply(
                getOptionsForGlide(
                    context,
                    fragmentActivity
                )
            )
            .into(holder.itemView.iv_news)

        holder.itemView.cv_main_layout.setOnClickListener { view ->

            val bundle = bundleOf(URL to newsArticle.url)
            view.findNavController()
                .navigate(R.id.action_newsCardFragment_to_newsWebViewFragment, bundle)
        }
    }

    override fun getLayout() = R.layout.item_news
}