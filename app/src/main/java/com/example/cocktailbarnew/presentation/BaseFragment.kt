package com.example.cocktailbarnew.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.cocktailbarnew.App
import com.example.cocktailbarnew.R
import com.example.cocktailbarnew.di.AppComponent
import com.example.cocktailbarnew.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM: ViewModel>(
    @LayoutRes private val layoutRes: Int
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = requireNotNull(_binding) { getString(R.string.binding_is_not_init) }

    private var _viewModel: VM? = null
    protected val viewModel get() = requireNotNull(_viewModel) { getString(R.string.view_model_is_not_init) }

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDaggerComponent((context.applicationContext as App).appComponent)
        _viewModel = ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutRes, container, false)
        _binding = createBinding(view)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun createBinding(view: View): VB

    abstract fun getViewModelClass(): Class<VM>

    abstract fun initDaggerComponent(appComponent: AppComponent)
}