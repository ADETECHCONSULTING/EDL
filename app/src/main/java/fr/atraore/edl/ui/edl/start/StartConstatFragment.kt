package fr.atraore.edl.ui.edl.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R

class StartConstatFragment : Fragment() {

    companion object {
        fun newInstance() = StartConstatFragment()
    }

    private var bundleConstatId: Int = -1
    private val startViewModel: StartConstatViewModel by viewModels() {
        StartConstatViewModelFactory((activity?.application as EdlApplication).repository, arguments?.getInt("constatId") ?: -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_constat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startViewModel.constatDetail.observe(viewLifecycleOwner, {
            Toast.makeText(context, it.constat.constatId.toString(), Toast.LENGTH_SHORT).show()
        })
    }

}