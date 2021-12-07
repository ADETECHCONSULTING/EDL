package fr.atraore.edl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Constat
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.adapter.ConstatAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.PROPERTY_LABEL
import kotlinx.android.synthetic.main.main_fragment.*
import java.sql.Date
import java.util.*

@AndroidEntryPoint
class MainFragment : BaseFragment("MainFrag") {
    override val title: String
        get() = "Main"

    override fun goNext() {

    }
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_next)?.isVisible = false
        menu.findItem(R.id.action_previous)?.isVisible = false
        menu.findItem(R.id.action_add_room)?.isVisible = false
        menu.findItem(R.id.action_compteur)?.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

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
            goToConstat("E")
        }
        btn_pre_etat.setOnClickListener {
            goToConstat("P")
        }
        btn_sortant.setOnClickListener {
            goToConstat("S")
        }
    }

    fun goToConstat(typeConstat: String) {
        val constat = Constat(
            constatId = UUID.randomUUID().toString(),
            typeConstat = typeConstat,
            dateCreation = Date(System.currentTimeMillis()),
            state = 0
        )
        mainViewModel.saveConstat(constat)
        val bundle = bundleOf(ARGS_CONSTAT_ID to constat.constatId)
        findNavController().navigate(R.id.go_to_start, bundle)
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