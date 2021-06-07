package com.example.newsapp.ui.news

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.util.Constant.URL
import kotlinx.android.synthetic.main.fragment_news_webview.*
import timber.log.Timber

class NewsWebViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Loading screen
        constraintLayout_loading_screen.visibility= View.VISIBLE

        var url = "url"
        arguments?.let {
            it.getString(URL)?.let{
                url = it
                Timber.d("url -> $url")
            }
        }


/*
        webView.settings.javaScriptEnabled = true
*/
        webView.loadUrl(url)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, _url: String): Boolean {
                view?.loadUrl(_url)
                Timber.d("shouldOverrideUrlLoading")
                return true
            }

            override fun onPageFinished(view: WebView?, _url: String?) {
                super.onPageFinished(view, _url)
                Timber.d("onPageFinished")
                constraintLayout_loading_screen.visibility= View.GONE
            }

            override fun onPageStarted(view: WebView?, _url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, _url, favicon)
                Timber.d("onPageStarted")
                constraintLayout_loading_screen.visibility= View.VISIBLE
            }
        }
    }
}