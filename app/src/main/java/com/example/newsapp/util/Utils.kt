package com.example.newsapp.util

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.R

object Utils {

    fun getCustomProgressBar(
        context: Context,
        fragmentActivity: FragmentActivity
    ): CircularProgressDrawable {
        //Custom Progress Bar for glide while loading the image
        val circularProgressDrawable = CircularProgressDrawable(fragmentActivity)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            circularProgressDrawable.setColorSchemeColors(context.getColor(R.color.colorPrimary))
        } else
            circularProgressDrawable.setColorSchemeColors(context.resources.getColor(R.color.colorPrimary))
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    fun getOptionsForGlide(context: Context, fragmentActivity: FragmentActivity): RequestOptions {
        //Options
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(getCustomProgressBar(context, fragmentActivity))
            .error(R.mipmap.ic_launcher_foreground)
        return options
    }
}