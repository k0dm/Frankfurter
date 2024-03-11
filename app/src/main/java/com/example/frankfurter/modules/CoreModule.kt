package com.example.frankfurter.modules

import com.example.data.core.ProvideResources
import com.example.data.dashboard.HandleError
import com.example.frankfurter.BaseProvideResources
import com.example.frankfurter.ProvideInstance
import com.example.presentation.core.RunAsync
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun bindRunAsync(runAsync: RunAsync.Base): RunAsync

    @Binds
    abstract fun bindProvideResources(provideResources: BaseProvideResources): ProvideResources

    @Binds
    abstract fun bindHandleError(handleError: HandleError.Base): HandleError

    @Binds
    abstract fun bindProvideInstances(provideInstance: ProvideInstance.Base): ProvideInstance
}
