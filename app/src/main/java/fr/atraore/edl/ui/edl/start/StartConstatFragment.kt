package fr.atraore.edl.ui.edl.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import kotlinx.android.synthetic.main.start_constat_fragment.*

class StartConstatFragment : Fragment() {

    companion object {
        fun newInstance() = StartConstatFragment()
    }

    private val startViewModel: StartConstatViewModel by viewModels() {
        StartConstatViewModelFactory((activity?.application as EdlApplication).repository, arguments?.getString("constatId")!!)
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
            Toast.makeText(context, it.constat.constatId, Toast.LENGTH_SHORT).show()
        })

        initListener()
    }

    fun initListener() {
        imv_search_bien.setOnClickListener {
            findNavController().navigate(R.id.go_to_search)
        }
    }

}