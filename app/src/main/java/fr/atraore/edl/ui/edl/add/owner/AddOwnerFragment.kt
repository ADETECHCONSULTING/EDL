package fr.atraore.edl.ui.edl.add.owner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R

class AddOwnerFragment : Fragment() {

    companion object {
        fun newInstance() = AddOwnerFragment()
    }

    private lateinit var viewModel: AddOwnerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_owner_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddOwnerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}