package fr.atraore.edl.ui.edl.second_page

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import fr.atraore.edl.R
import kotlinx.android.synthetic.main.end_constat_fragment.*

class EndConstatFragment : Fragment() {

    companion object {
        fun newInstance() = EndConstatFragment()
    }

    private lateinit var viewModel: EndConstatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.end_constat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EndConstatViewModel::class.java)
        // TODO: Use the ViewModel
    }


}