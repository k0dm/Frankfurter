package com.example.presentation.core

import org.junit.Assert.assertEquals

internal class FakeClear : ClearViewModel {

    private val actualClearClassList = mutableListOf<Class<out CustomViewModel>>()

    override fun clear(clazz: Class<out CustomViewModel>) {
        actualClearClassList.add(clazz)
    }

    fun checkClearCalled(expectedClazzList: List<Class<out CustomViewModel>>) {
        assertEquals(expectedClazzList, actualClearClassList)
    }
}
