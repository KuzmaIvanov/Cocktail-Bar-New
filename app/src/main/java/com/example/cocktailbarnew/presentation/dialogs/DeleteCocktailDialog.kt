package com.example.cocktailbarnew.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.cocktailbarnew.databinding.DialogFragmentDeleteCocktailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteCocktailDialog(
    private val onDeleteAction: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentDeleteCocktailBinding.inflate(layoutInflater)
        with(binding) {
            deleteButton.setOnClickListener {
                onDeleteAction()
                dismiss()
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
        }
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
        return builder.create()
    }

    companion object {
        const val DELETE_COCKTAIL_DIALOG_TAG = "DELETE_COCKTAIL_DIALOG"
    }
}