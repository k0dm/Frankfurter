package com.example.domain.settings

interface SaveResult {

    fun map(mapper: Mapper)

    interface Mapper {

        fun mapSavedSuccessfully()

        fun mapRequireSubscription()
    }

    object Success : SaveResult {

        override fun map(mapper: Mapper) {
            mapper.mapSavedSuccessfully()
        }
    }

    object RequirePremium : SaveResult {

        override fun map(mapper: Mapper) {
            mapper.mapRequireSubscription()
        }
    }
}