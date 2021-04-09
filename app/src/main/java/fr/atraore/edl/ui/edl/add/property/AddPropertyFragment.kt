package fr.atraore.edl.ui.edl.add.property

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.BaseFragment

class AddPropertyFragment : BaseFragment() {

    override val title: String
        get() = "Biens"

    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    private lateinit var viewModel: AddPropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_property_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddPropertyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}