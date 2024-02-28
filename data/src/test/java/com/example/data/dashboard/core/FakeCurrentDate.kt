package com.example.data.dashboard.core

import com.example.data.dashboard.cache.CurrentDate

internal class FakeCurrentDate(private val date: String) : CurrentDate {

    override fun date() = date
}