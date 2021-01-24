package fr.atraore.edl.ui.edl.add

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R

class AddContractorFragment : Fragment() {

    companion object {
        fun newInstance() = AddContractorFragment()
    }

    private lateinit var viewModel: AddContractorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_contractor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddContractorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}