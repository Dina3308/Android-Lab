package com.example.hw.utils

import android.content.res.Resources
import com.example.hw.R
import retrofit2.HttpException
import java.net.UnknownHostException

fun Throwable.getErrorMessage(resources: Resources): String? = when (this) {
    is UnknownHostException -> resources.getString(R.string.no_internet)
    is HttpException -> resources.getString(R.string.no_city)
    else -> message
}