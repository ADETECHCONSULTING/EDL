package fr.atraore.edl.ui.edl.add.owner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.ui.edl.BaseFragment

class AddOwnerFragment : BaseFragment<Owner>() {

    override val title: String
        get() = "Propriétaires"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddOwnerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}