package com.example.cocktailbarnew.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailbarnew.di.IoDispatcher
import com.example.cocktailbarnew.domain.model.Cocktail
import com.example.cocktailbarnew.domain.repository.CocktailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyCocktailsViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _cocktailsUiState = MutableStateFlow<Result<List<Cocktail>>?>(null)
    val cocktailsUiState get() = _cocktailsUiState

    fun getAllCocktails() {
        viewModelScope.launch(ioDispatcher) {
            cocktailRepository.getAll()
                .catch {
                    _cocktailsUiState.value = Result.failure(it)
                }
                .collect {
                    _cocktailsUiState.value = Result.success(it)
                }
        }
    }

    fun getRecentCocktails(): List<Cocktail> {
        return cocktailsUiState.value?.getOrThrow()?.takeLast(NUMBER_OF_RECENT_COCKTAILS)!!
    }

    companion object {
        private const val NUMBER_OF_RECENT_COCKTAILS = 4
    }
}