package fr.atraore.edl.ui.edl.constat.second_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.SUITE_CONSTAT_LABEL


class DetailEndConstatFragment : BaseFragment(SUITE_CONSTAT_LABEL), MainActivity.OnNavigationFragment {

    override val title: String
        get() = SUITE_CONSTAT_LABEL

    companion object {
        fun newInstance() = DetailEndConstatFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).mNavigationFragment = this
    }

    override fun goNext() {
        Toast.makeText(activity, "vers signature", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_end_constat, container, false)
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }
}