package fr.atraore.edl.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.Constat
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.adapter.ConstatAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.settings.KeysConfigurationActivity
import fr.atraore.edl.ui.settings.OutDoorConfigurationActivity
import fr.atraore.edl.ui.settings.RoomConfigurationActivity
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.ARGS_OWNER_ID
import fr.atraore.edl.utils.ARGS_USER_ID
import kotlinx.android.synthetic.main.fragment_main.*
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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_next)?.isVisible = false
        menu.findItem(R.id.action_previous)?.isVisible = false
        menu.findItem(R.id.action_compteur)?.isVisible = false
        menu.findItem(R.id.action_keys)?.isVisible = true
        menu.findItem(R.id.action_add_user)?.isVisible = true
        menu.findItem(R.id.action_add_agency)?.isVisible = true
        menu.findItem(R.id.action_outdoor)?.isVisible = true

        menu.findItem(R.id.action_keys)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        menu.findItem(R.id.action_add_room)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        menu.findItem(R.id.action_outdoor)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

        menu.findItem(R.id.action_add_room)?.title = getString(R.string.manage_rooms)
        menu.findItem(R.id.action_keys)?.title = getString(R.string.manage_keys)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_room -> {
                val intent = Intent(requireContext(), RoomConfigurationActivity::class.java)
                startActivity(intent)
            }
            R.id.action_keys -> {
                val intent = Intent(requireContext(), KeysConfigurationActivity::class.java)
                startActivity(intent)
            }
            R.id.action_add_user -> {
                goToAddUser()
            }
            R.id.action_outdoor -> {
                val intent = Intent(requireContext(), OutDoorConfigurationActivity::class.java)
                startActivity(intent)
            }
            R.id.action_add_agency -> {
                goToAddAgency()
            }
        }
        return super.onOptionsItemSelected(item)
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

    fun goToAddUser() {
        findNavController().navigate(R.id.go_to_add_user)
    }

    fun goToAddAgency() {
        findNavController().navigate(R.id.go_to_add_agency)
    }

}