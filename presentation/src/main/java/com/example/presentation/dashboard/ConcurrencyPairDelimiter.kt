package com.example.presentation.dashboard

interface ConcurrencyPairDelimiter {

    interface MakeDeletePairScreen {

        fun makeDeletePairScreen(concurrencyPair: String): DeletePairScreen
    }

    interface AddDelimiter {

        fun addDelimiter(from: String, to: String): String
    }

    interface Mutable : MakeDeletePairScreen, AddDelimiter

    class Base(private val concurrencyPairDelimiter: String = " / ") : Mutable {

        override fun makeDeletePairScreen(concurrencyPair: String) =
            concurrencyPair.split(concurrencyPairDelimiter).let { DeletePairScreen(it[0], it[1]) }

        override fun addDelimiter(from: String, to: String) = "$from$concurrencyPairDelimiter$to"
    }
}