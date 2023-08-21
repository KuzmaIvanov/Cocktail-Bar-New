package com.example.cocktailbarnew.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.cocktailbarnew.R
import com.example.cocktailbarnew.databinding.FragmentMyCocktailsBinding
import com.example.cocktailbarnew.di.AppComponent
import com.example.cocktailbarnew.presentation.BaseFragment
import com.example.cocktailbarnew.presentation.adapters.CocktailItemAdapter
import com.example.cocktailbarnew.presentation.viewmodels.MyCocktailsViewModel
import kotlinx.coroutines.launch

class MyCocktailsFragment : BaseFragment<FragmentMyCocktailsBinding, MyCocktailsViewModel>(
    R.layout.fragment_my_cocktails
) {

    private val adapter = CocktailItemAdapter(
        cocktails = emptyList(),
        onItemClickAction = { id ->
            navigateToCocktailDetails(id)
        }
    )

    override fun createBinding(view: View): FragmentMyCocktailsBinding {
        return FragmentMyCocktailsBinding.bind(view)
    }

    override fun getViewModelClass(): Class<MyCocktailsViewModel> {
        return MyCocktailsViewModel::class.java
    }

    override fun initDaggerComponent(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecyclerView()
        loadData()
        observeCocktailsUiState()
    }

    private fun initListeners() {
        binding.createCocktailFab.setOnClickListener {
            navigateToCreateCocktail()
        }
        binding.shareImageView.setOnClickListener {
            showShareCocktailsBottomSheet()
        }
    }

    private fun initRecyclerView() {
        binding.myCocktailsRecyclerView.adapter = adapter
    }

    private fun observeCocktailsUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cocktailsUiState.collect { uiState ->
                    uiState?.let {
                        if (uiState.isSuccess) {
                            val cocktails = uiState.getOrNull()
                            if (cocktails.isNullOrEmpty()) {
                                binding.noCocktailsGroup.visibility = View.VISIBLE
                                binding.shareImageView.visibility = View.GONE
                                adapter.cocktails = emptyList()
                            } else {
                                binding.noCocktailsGroup.visibility = View.GONE
                                binding.shareImageView.visibility = View.VISIBLE
                                adapter.cocktails = cocktails
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.failed_to_load_data),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getAllCocktails()
    }

    private fun showShareCocktailsBottomSheet() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getShareMessage())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun getShareMessage(): String {
        val message = StringBuilder()
            .apply {
                append(getString(R.string.first_cocktails_share_message))
                viewModel.getRecentCocktails().forEach {
                    append(it.name)
                    append(", ")
                }
                append(getString(R.string.end_etc_message))
                append(getString(R.string.last_cocktails_share_message))
            }
        return message.toString()
    }

    private fun navigateToCreateCocktail() {
        val action = MyCocktailsFragmentDirections.actionMyCocktailsFragmentToCreateCocktailFragment()
        findNavController().navigate(action)
    }

    private fun navigateToCocktailDetails(id: Long) {
        val action = MyCocktailsFragmentDirections.actionMyCocktailsFragmentToCocktailDetailsFragment(id)
        findNavController().navigate(action)
    }
}