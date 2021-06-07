package com.example.newsapp.di

import com.example.newsapp.ui.news.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule by lazy{
    module {
        viewModel { NewsViewModel(get()) }
    }
}