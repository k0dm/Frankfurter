package com.example.presentation.dashboard

interface CurrencyPairDelimiter {

    interface MakeDeletePairScreen {

        fun makeDeletePairScreen(currencyPair: String): DeletePairScreen
    }

    interface AddDelimiter {

        fun addDelimiter(from: String, to: String): String
    }

    interface Mutable : MakeDeletePairScreen, AddDelimiter

    class Base(private val currencyPairDelimiter: String = " / ") : Mutable {

        override fun makeDeletePairScreen(currencyPair: String) =
            currencyPair.split(currencyPairDelimiter).let { DeletePairScreen(it[0], it[1]) }

        override fun addDelimiter(from: String, to: String) = "$from$currencyPairDelimiter$to"
    }
}