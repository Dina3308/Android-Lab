package com.example.hw.presentation

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

@AddToEndSingle
interface BaseView {

    fun showLoading()

    fun hideLoading()

    @Skip
    fun consumerError(throwable: Throwable)
}