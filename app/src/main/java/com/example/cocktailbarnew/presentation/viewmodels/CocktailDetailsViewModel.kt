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

class CocktailDetailsViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _cocktailDetailsUiState = MutableStateFlow<Result<Cocktail?>>(Result.success(null))
    val cocktailDetailsUiState get() = _cocktailDetailsUiState

    private val _isSuccessfullyDeleted = MutableStateFlow<Boolean?>(null)
    val isSuccessfullyDeleted get() = _isSuccessfullyDeleted

    fun getById(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            cocktailRepository.getById(id)
                .catch {
                    _cocktailDetailsUiState.value = Result.failure(it)
                }
                .collect {
                    _cocktailDetailsUiState.value = Result.success(it)
                }
        }
    }

    fun deleteById(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            kotlin.runCatching {
                cocktailRepository.deleteById(id)
            }.onFailure {
                _isSuccessfullyDeleted.value = false
            }.onSuccess {
                _isSuccessfullyDeleted.value = true
            }
        }
    }
}