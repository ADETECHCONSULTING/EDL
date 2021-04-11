package fr.atraore.edl.ui.edl

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fr.atraore.edl.EdlApplication

abstract class BaseFragment<T> : Fragment() {
    abstract val title: String

    private val baseViewModel: BaseViewModel<T> by viewModels {
        val edlApplication = activity?.application as EdlApplication;
        BaseViewModelFactory(edlApplication.constatRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, baseViewModel.toString(), Toast.LENGTH_SHORT).show()
    }
}