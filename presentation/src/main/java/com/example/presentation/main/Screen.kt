package com.example.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Screen {

    fun show(containerId: Int, supportFragmentManager: FragmentManager)

    abstract class Replace(private val clazz: Class<out Fragment>) : Screen {

        override fun show(containerId: Int, supportFragmentManager: FragmentManager) {
            supportFragmentManager.beginTransaction()
                .replace(containerId, clazz.getDeclaredConstructor().newInstance())
                .commit()
        }
    }

    abstract class Add(private val clazz: Class<out Fragment>): Screen {

        override fun show(containerId: Int, supportFragmentManager: FragmentManager) {
            supportFragmentManager.beginTransaction()
                .add(containerId, clazz.getDeclaredConstructor().newInstance())
                .addToBackStack(clazz.simpleName)
                .commit()
        }
    }

    object Empty : Screen {
        override fun show(containerId: Int, supportFragmentManager: FragmentManager) = Unit
    }

    object Pop : Screen {

        override fun show(containerId: Int, supportFragmentManager: FragmentManager) {
            supportFragmentManager.popBackStack()
        }
    }
}