package com.example.cocktailbarnew.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cocktailbarnew.R
import com.example.cocktailbarnew.databinding.FragmentCreateCocktailBinding
import com.example.cocktailbarnew.di.AppComponent
import com.example.cocktailbarnew.domain.model.Cocktail
import com.example.cocktailbarnew.presentation.BaseFragment
import com.example.cocktailbarnew.presentation.dialogs.AddIngredientDialog
import com.example.cocktailbarnew.presentation.dialogs.AddIngredientDialog.Companion.ADD_INGREDIENT_DIALOG_TAG
import com.example.cocktailbarnew.presentation.dialogs.PickImageOptionsDialog
import com.example.cocktailbarnew.presentation.dialogs.PickImageOptionsDialog.Companion.PICK_PHOTO_DIALOG_TAG
import com.example.cocktailbarnew.presentation.viewmodels.CreateCocktailViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.launch

class CreateCocktailFragment : BaseFragment<FragmentCreateCocktailBinding, CreateCocktailViewModel>(
    R.layout.fragment_create_cocktail
) {

    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>

    override fun createBinding(view: View): FragmentCreateCocktailBinding {
        return FragmentCreateCocktailBinding.bind(view)
    }

    override fun getViewModelClass(): Class<CreateCocktailViewModel> {
        return CreateCocktailViewModel::class.java
    }

    override fun initDaggerComponent(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.setPickedImageUri(uri)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observePickedImageUri()
        observeIsSuccessfullyInserted()
    }

    private fun initListeners() {
        binding.cocktailNameEditText.doAfterTextChanged {
            binding.cocktailNameTextInputLayout.error = null
        }
        binding.saveCocktailButton.setOnClickListener {
            val enteredName = binding.cocktailNameEditText.text.toString()
            if (enteredName.isEmpty()) {
                binding.cocktailNameTextInputLayout.error =
                    getString(R.string.input_error_add_title)
            }
            val ingredientsChipGroup = binding.ingredientsChipGroup
            if (ingredientsChipGroup.isEmpty()) {
                showNoIngredientsToast()
            }
            if (enteredName.isNotEmpty() && ingredientsChipGroup.isNotEmpty()) {
                insertCocktail(getCocktail())
            }
        }
        binding.cancelCocktailButton.setOnClickListener {
            navigateToMyCocktails()
        }
        binding.addIngredientFab.setOnClickListener {
            showAddIngredientDialog()
        }
        binding.addCocktailImageCardView.setOnClickListener {
            showPickImageOptionsDialog()
        }
    }

    private fun insertCocktail(cocktail: Cocktail) {
        viewModel.insert(cocktail)
    }

    private fun getListOfStringFromIngredientChipGroup(): List<String> {
        val ingredients = mutableListOf<String>()
        binding.ingredientsChipGroup.forEach {
            val chipText = (it as Chip).text.toString()
            ingredients.add(chipText)
        }
        return ingredients
    }

    private fun getCocktail(): Cocktail {
        val image =
            if (viewModel.getPickedImageValue() != null) viewModel.getPickedImageValue()
                .toString() else null
        return Cocktail(
            id = 0L,
            name = binding.cocktailNameEditText.text.toString(),
            description = binding.cocktailDescriptionEditText.text.toString(),
            recipe = binding.cocktailRecipeEditText.text.toString(),
            ingredients = getListOfStringFromIngredientChipGroup(),
            image = image
        )
    }

    private fun addIngredientChip(enteredName: String) {
        val ingredientChip = Chip(requireContext())
            .apply {
                text = enteredName
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    binding.ingredientsChipGroup.removeView(this)
                }
                shapeAppearanceModel = ShapeAppearanceModel()
                    .withCornerSize(resources.getDimension(R.dimen.default_card_view_corner_radius))
            }
        binding.ingredientsChipGroup.addView(ingredientChip)
    }

    private fun showAddIngredientDialog() {
        val dialogFragment = AddIngredientDialog {
            addIngredientChip(it)
        }
        dialogFragment.show(parentFragmentManager, ADD_INGREDIENT_DIALOG_TAG)
    }

    private fun showPickImageOptionsDialog() {
        val dialogFragment = PickImageOptionsDialog(
            onPickImageOption = {
                pickCocktailImageFromGallery()
            },
            onDeletePickedImageOption = {
                viewModel.resetPickedImageUri()
            }
        )
        dialogFragment.show(parentFragmentManager, PICK_PHOTO_DIALOG_TAG)
    }

    private fun observeIsSuccessfullyInserted() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSuccessfullyInserted.collect {
                    it?.let {
                        val message = when (it) {
                            true -> {
                                getString(R.string.inserted_successfully_message)
                            }

                            false -> {
                                getString(R.string.inserted_failed_message)
                            }
                        }
                        showIsInsertedSuccessfullyToast(message)
                        navigateToMyCocktails()
                    }
                }
            }
        }
    }

    private fun observePickedImageUri() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pickedImageUri.collect { uri ->
                    if (uri == null) {
                        binding.photoCameraImageView.visibility = View.VISIBLE
                    } else {
                        binding.photoCameraImageView.visibility = View.GONE
                    }
                    binding.pickedImageImageView.load(uri)
                }
            }
        }
    }

    private fun showNoIngredientsToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.empty_ingredients_toast_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showIsInsertedSuccessfullyToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun pickCocktailImageFromGallery() {
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun navigateToMyCocktails() {
        findNavController().navigateUp()
    }
}