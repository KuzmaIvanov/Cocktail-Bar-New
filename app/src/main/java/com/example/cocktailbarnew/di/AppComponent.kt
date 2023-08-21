package com.example.cocktailbarnew.di

import android.content.Context
import com.example.cocktailbarnew.presentation.screens.CocktailDetailsFragment
import com.example.cocktailbarnew.presentation.screens.CreateCocktailFragment
import com.example.cocktailbarnew.presentation.screens.MyCocktailsFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment: MyCocktailsFragment)
    fun inject(fragment: CreateCocktailFragment)
    fun inject(fragment: CocktailDetailsFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}

@Module(
    includes = [
        AppBindsModule::class,
        DatabaseModule::class,
        ViewModelModule::class,
        DispatcherModule::class
    ]
)
class AppModule