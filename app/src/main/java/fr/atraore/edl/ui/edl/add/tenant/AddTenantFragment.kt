package fr.atraore.edl.ui.edl.add.tenant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.BaseFragment

class AddTenantFragment : BaseFragment() {

    override val title: String
        get() = "Locataires"

    companion object {
        fun newInstance() = AddTenantFragment()
    }

    private lateinit var viewModel: AddTenantViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_tenant_fragment, container, false)
    }

}