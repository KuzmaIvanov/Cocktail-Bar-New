package com.example.cocktailbarnew.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.example.cocktailbarnew.R
import com.example.cocktailbarnew.databinding.DialogFragmentAddIngredientBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddIngredientDialog(
    private val onAddIngredient: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentAddIngredientBinding.inflate(layoutInflater)
        with(binding) {
            ingredientNameTextInputLayout.editText?.doAfterTextChanged {
                ingredientNameTextInputLayout.error = null
            }
            addIngredientButton.setOnClickListener {
                val editText = ingredientNameTextInputLayout.editText
                if (editText != null && editText.text.isNotEmpty()) {
                    onAddIngredient(editText.text.toString())
                    dismiss()
                } else {
                    ingredientNameTextInputLayout.error = getString(R.string.input_error_add_title)
                }
            }
            cancelIngredientButton.setOnClickListener {
                dismiss()
            }
        }
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
        return builder.create()
    }

    companion object {
        const val ADD_INGREDIENT_DIALOG_TAG = "ADD_INGREDIENT_DIALOG"
    }
}