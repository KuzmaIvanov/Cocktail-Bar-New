package com.example.cocktailbarnew.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cocktailbarnew.presentation.viewmodels.CocktailDetailsViewModel
import com.example.cocktailbarnew.presentation.viewmodels.CreateCocktailViewModel
import com.example.cocktailbarnew.presentation.viewmodels.MyCocktailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MyCocktailsViewModel::class)
    abstract fun splashMyCocktailsViewModel(viewModel: MyCocktailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateCocktailViewModel::class)
    abstract fun splashCreateCocktailViewModel(viewModel: CreateCocktailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CocktailDetailsViewModel::class)
    abstract fun splashCocktailDetailsViewModel(viewModel: CocktailDetailsViewModel): ViewModel
}