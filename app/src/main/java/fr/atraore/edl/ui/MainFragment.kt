package fr.atraore.edl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.BuildConfig
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.adapter.ConstatAdapter
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import kotlinx.android.synthetic.main.main_fragment.*
import java.sql.Date
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ConstatAdapter()
        rcv_constat.adapter = adapter
        rcv_constat.layoutManager = LinearLayoutManager(context)

        mainViewModel.allConstatWithDetails.observe(viewLifecycleOwner, Observer { constats ->
            constats?.let { adapter.submitList(it) }
        })

        initListeners()
    }

    fun initListeners() {
        btn_entrant.setOnClickListener {
            val constat = Constat(
                constatId = UUID.randomUUID().toString(),
                typeConstat = "E",
                dateCreation = Date(System.currentTimeMillis()),
                state = 0
            )
            mainViewModel.saveConstat(constat)
            val bundle = bundleOf(ARGS_CONSTAT_ID to constat.constatId)
            findNavController().navigate(R.id.go_to_start, bundle)
        }
        btn_pre_etat.setOnClickListener {
            findNavController().navigate(R.id.go_to_start)
        }
        btn_sortant.setOnClickListener {
            findNavController().navigate(R.id.go_to_start)
        }

        if (BuildConfig.DEBUG) {
            btn_photo.visibility = View.VISIBLE
            btn_photo.setOnClickListener {
                openPicker()
            }
        }
    }

    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 10,
            theme = R.style.ChiliPhotoPicker_Light
        ).show(requireActivity().supportFragmentManager, "picker")
    }

}