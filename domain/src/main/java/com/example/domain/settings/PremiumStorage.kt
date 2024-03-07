package com.example.domain.settings

interface PremiumStorage {

    interface Read {

        fun isUserPremium(): Boolean
    }

    interface Save {

        fun savePremium()
    }

    interface Mutable : Read, Save
}