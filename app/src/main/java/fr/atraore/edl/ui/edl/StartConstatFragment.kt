package fr.atraore.edl.ui.edl

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R

class StartConstatFragment : Fragment() {

    companion object {
        fun newInstance() = StartConstatFragment()
    }

    private lateinit var viewModel: StartConstatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_constat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StartConstatViewModel::class.java)
        // TODO: Use the ViewModel
    }

}