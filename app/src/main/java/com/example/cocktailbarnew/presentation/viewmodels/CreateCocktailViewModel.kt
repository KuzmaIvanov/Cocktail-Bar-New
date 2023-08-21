package com.example.cocktailbarnew.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailbarnew.di.IoDispatcher
import com.example.cocktailbarnew.domain.model.Cocktail
import com.example.cocktailbarnew.domain.repository.CocktailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateCocktailViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _isSuccessfullyInserted = MutableStateFlow<Boolean?>(null)
    val isSuccessfullyInserted get() = _isSuccessfullyInserted

    private val _pickedImageUri = MutableStateFlow<Uri?>(null)
    val pickedImageUri get() = _pickedImageUri

    fun insert(cocktail: Cocktail) {
        viewModelScope.launch(ioDispatcher) {
            kotlin.runCatching {
                cocktailRepository.insert(cocktail)
            }.onFailure {
                _isSuccessfullyInserted.value = false
            }.onSuccess {
                _isSuccessfullyInserted.value = true
            }
        }
    }

    fun getPickedImageValue(): Uri? {
        return _pickedImageUri.value
    }

    fun setPickedImageUri(uri: Uri?) {
        _pickedImageUri.value = uri
    }

    fun resetPickedImageUri() {
        _pickedImageUri.value = null
    }
}