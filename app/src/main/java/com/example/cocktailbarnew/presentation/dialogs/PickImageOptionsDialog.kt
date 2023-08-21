package com.example.cocktailbarnew.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.cocktailbarnew.databinding.DialogFragmentPickImageOptionsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PickImageOptionsDialog(
    private val onPickImageOption: () -> Unit,
    private val onDeletePickedImageOption: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentPickImageOptionsBinding.inflate(layoutInflater)
        with(binding) {
            pickPhotoButton.setOnClickListener {
                onPickImageOption()
                dismiss()
            }
            deletePhotoButton.setOnClickListener {
                onDeletePickedImageOption()
                dismiss()
            }
        }
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
        return builder.create()
    }

    companion object {
        const val PICK_PHOTO_DIALOG_TAG = "PICK_PHOTO_DIALOG"
    }
}