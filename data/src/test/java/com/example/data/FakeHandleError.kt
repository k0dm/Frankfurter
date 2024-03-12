package com.example.data

import com.example.data.dashboard.HandleError

internal class FakeHandleError : HandleError {

    override fun handle(e: Exception): String = e.javaClass.simpleName
}