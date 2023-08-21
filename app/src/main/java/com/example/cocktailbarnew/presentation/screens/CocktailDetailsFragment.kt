package com.example.cocktailbarnew.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.cocktailbarnew.R
import com.example.cocktailbarnew.databinding.FragmentCocktailDetailsBinding
import com.example.cocktailbarnew.di.AppComponent
import com.example.cocktailbarnew.domain.model.Cocktail
import com.example.cocktailbarnew.presentation.BaseFragment
import com.example.cocktailbarnew.presentation.dialogs.DeleteCocktailDialog
import com.example.cocktailbarnew.presentation.dialogs.DeleteCocktailDialog.Companion.DELETE_COCKTAIL_DIALOG_TAG
import com.example.cocktailbarnew.presentation.viewmodels.CocktailDetailsViewModel
import kotlinx.coroutines.launch

class CocktailDetailsFragment :
    BaseFragment<FragmentCocktailDetailsBinding, CocktailDetailsViewModel>(
        R.layout.fragment_cocktail_details
    ) {

    private val args: CocktailDetailsFragmentArgs by navArgs()

    override fun createBinding(view: View): FragmentCocktailDetailsBinding {
        return FragmentCocktailDetailsBinding.bind(view)
    }

    override fun getViewModelClass(): Class<CocktailDetailsViewModel> {
        return CocktailDetailsViewModel::class.java
    }

    override fun initDaggerComponent(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getCocktail()
        observeCocktailDetailsUiState()
        observeIsSuccessfullyDeleted()
    }

    private fun getCocktail() {
        viewModel.getById(args.cocktailId)
    }

    private fun observeCocktailDetailsUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cocktailDetailsUiState.collect {
                    if (it.isSuccess) {
                        val cocktail = it.getOrNull()
                        if (cocktail != null) {
                            initViews(cocktail)
                        }
                    } else {
                        showFailedToGetCocktailToast()
                        navigateToMyCocktails()
                    }
                }
            }
        }
    }

    private fun initViews(cocktail: Cocktail) = with(binding) {
        cocktailNameTextView.text = cocktail.name
        if (cocktail.description.isEmpty()) {
            cocktailDescTextView.visibility = View.GONE
        } else {
            cocktailDescTextView.text = cocktail.description
        }
        cocktailIngredientsTextView.text = getIngredientsString(cocktail.ingredients)
        if (cocktail.recipe.isEmpty()) {
            cocktailRecipeTextView.visibility = View.GONE
            cocktailRecipeTitleTextView.visibility = View.GONE
        } else {
            cocktailRecipeTextView.text = cocktail.recipe
        }
        cocktailDetailsImageView.load(cocktail.image ?: R.drawable.cocktail_placeholder)
    }

    private fun getIngredientsString(ingredients: List<String>): String {
        val stringBuilder = StringBuilder()
        ingredients.forEachIndexed { i, it ->
            stringBuilder.append(it)
            if (i != ingredients.size - 1)
                stringBuilder.append("\n—\n")
        }
        return stringBuilder.toString()
    }

    private fun showFailedToGetCocktailToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.failed_to_get_cocktail),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteSelectedCocktail() {
        viewModel.deleteById(args.cocktailId)
    }

    private fun initListeners() {
        binding.deleteCocktailButton.setOnClickListener {
            showDeleteCocktailDialog()
        }
    }

    private fun observeIsSuccessfullyDeleted() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSuccessfullyDeleted.collect {
                    it?.let {
                        val message = when (it) {
                            true -> {
                                getString(R.string.deleted_successfully_message)
                            }

                            false -> {
                                getString(R.string.deleted_failed_message)
                            }
                        }
                        showIsSuccessfullyDeletedToast(message)
                        navigateToMyCocktails()
                    }
                }
            }
        }
    }

    private fun showDeleteCocktailDialog() {
        val dialogFragment = DeleteCocktailDialog {
            deleteSelectedCocktail()
        }
        dialogFragment.show(parentFragmentManager, DELETE_COCKTAIL_DIALOG_TAG)
    }

    private fun showIsSuccessfullyDeletedToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun navigateToMyCocktails() {
        findNavController().navigateUp()
    }
}