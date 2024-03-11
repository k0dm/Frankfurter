package com.example.frankfurter.modules

import com.example.presentation.main.Navigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    abstract fun bindUpdateNavigation(navigation: Navigation.Base): Navigation.Update

    @Binds
    abstract fun bindMutableNavigation(navigation: Navigation.Base): Navigation.Mutable
}