package com.example.presentation.core

interface UpdateAdapter<T : Any> {

    fun update(newList: List<T>)
}