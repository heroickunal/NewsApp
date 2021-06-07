package com.example.newsapp.ui.news

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.ui.news.adapter.NewsItem
import com.example.newsapp.util.ResponseHandler
import com.example.newsapp.util.Utils.getCurrentDate
import com.example.newsapp.util.showToast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
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

        clickListener()
        viewModelWorks()
    }

    private fun clickListener() {
        iv_search_by_topic.setOnClickListener {
            searchByTopicDialog()
        }
    }

    private fun viewModelWorks() {
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
                    response.errorMessage?.let{errormsg->
                        requireActivity().showToast(errormsg)
                    }
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

    private fun searchByTopicDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_search)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val topicName = dialog.findViewById(R.id.et_topic_name) as TextInputEditText
        val btnSearch = dialog.findViewById(R.id.btn_search) as MaterialButton

        btnSearch.setOnClickListener {
            if(topicName.text.toString().isNotEmpty()) {
                viewModel.getNewsByTopic(topicName.text.toString(), "publishedAt", getCurrentDate())
                dialog.dismiss()
            }
            else
            {
                requireActivity().showToast(getString(R.string.please_enter_the_topic))
            }
        }

        requireActivity().resources?.let {
            val displayMetrics = it.displayMetrics
            val dialogWidth = (displayMetrics.widthPixels * 0.85).toInt()
            val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(dialogWidth, dialogHeight)
            dialog.show()
        }
    }
}