package com.example.newsapp.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.ui.news.adapter.NewsItem
import com.example.newsapp.util.ResponseHandler
import com.example.newsapp.util.showToast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_news_card.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class NewsCardFragment : Fragment() {

    private val viewModel by sharedViewModel<NewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelWorks()
    }

    private fun viewModelWorks() {/*
        viewModel.getNewsByTopic("Tesla","publishedAt","2021-06-06")*/
        viewModel.getNewsByTopicResponse.observe(requireActivity(), { response ->
            when (response) {
                is ResponseHandler.Success -> {
                    response.data?.let { newsByTopicResponse ->
                        Timber.d("getNewsByTopicResponse ${newsByTopicResponse}")
                        newsByTopicResponse.articles.let {articleList->
                            if(articleList.isNotEmpty()) {

                                //Groupie
                                val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
                                    addAll(articleList.toRecyclerListItem())
                                }
                                rv_news.apply {
                                    layoutManager =
                                        LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    adapter = groupAdapter
                                }
                            }
                            else{
                                requireActivity().showToast("No articles")
                            }
                        }
                        shimmer_layout_news.visibility = View.GONE
                    }
                }
                is ResponseHandler.Loading -> {
                    Timber.d("Loading ")
                    shimmer_layout_news.visibility = View.VISIBLE
                }
                is ResponseHandler.Failure -> {
                    Timber.d("response ${response}")
                    shimmer_layout_news.visibility = View.GONE
                }
            }
        })
    }

    fun List<Article>.toRecyclerListItem(): List<NewsItem> {
        val list = mutableListOf<NewsItem>()
        this.map { item ->
            list.add(NewsItem(item, requireActivity(), requireContext()))
        }
        return list.toMutableList()
    }
}