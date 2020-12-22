package fr.atraore.edl.ui.edl.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.utils.*
import kotlinx.android.synthetic.main.start_constat_fragment.*

class StartConstatFragment : Fragment() {

    companion object {
        fun newInstance() = StartConstatFragment()
    }

    private val startViewModel: StartConstatViewModel by viewModels() {
        StartConstatViewModelFactory((activity?.application as EdlApplication).constatRepository, arguments?.getString(ARGS_CONSTAT_ID)!!)
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
            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_BIENS)
            findNavController().navigate(R.id.go_to_search, bundle)
        }

        imv_search_owner.setOnClickListener {
            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_PROPRIETAIRE)
            findNavController().navigate(R.id.go_to_search, bundle)
        }

        imv_search_locataire.setOnClickListener {
            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_LOCATAIRE)
            findNavController().navigate(R.id.go_to_search, bundle)
        }

        imv_search_mandataire.setOnClickListener {
            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_MANDATAIRE)
            findNavController().navigate(R.id.go_to_search, bundle)
        }

        imv_search_agence.setOnClickListener {
            val bundle = bundleOf(ARGS_TAB_POSITION to POSITION_FRAGMENT_AGENCES)
            findNavController().navigate(R.id.go_to_search, bundle)
        }
    }

}