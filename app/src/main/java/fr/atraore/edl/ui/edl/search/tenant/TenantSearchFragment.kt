package fr.atraore.edl.ui.edl.search.tenant

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.search.BaseFragment

class TenantSearchFragment : BaseFragment() {

    override val title: String
        get() = "Locataire"

    companion object {
        fun newInstance() = TenantSearchFragment()
    }

    private lateinit var viewModel: TenantSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tenant_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TenantSearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}