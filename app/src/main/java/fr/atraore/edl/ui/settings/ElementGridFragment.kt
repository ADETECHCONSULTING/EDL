package fr.atraore.edl.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.ui.ReferenceViewModel
import fr.atraore.edl.ui.adapter.ElementGridAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.ELEMENT_CONFIG_LABEL
import fr.atraore.edl.utils.SUITE_CONSTAT_LABEL

@AndroidEntryPoint
class ElementGridFragment : BaseFragment(SUITE_CONSTAT_LABEL) {
    private val TAG = ElementGridFragment::class.simpleName

    override val title: String
        get() = ELEMENT_CONFIG_LABEL

    private val viewModel: ReferenceViewModel by viewModels()
    private val adapter = ElementGridAdapter()

    override fun goNext() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_element_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getElements.observe(viewLifecycleOwner) { elementRes ->
            adapter.swapData(elementRes)
        }
    }
}