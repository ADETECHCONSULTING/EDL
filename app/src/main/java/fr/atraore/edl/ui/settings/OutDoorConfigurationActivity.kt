package fr.atraore.edl.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.OutdoorEquipementReference
import fr.atraore.edl.ui.ReferenceViewModel
import fr.atraore.edl.ui.adapter.OutdoorGridAdapter
import kotlinx.android.synthetic.main.activity_keys_configuration.*
import kotlinx.android.synthetic.main.activity_room_configuration.*
import kotlinx.android.synthetic.main.activity_room_configuration.rcv_elements
import kotlinx.android.synthetic.main.activity_room_configuration.toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class OutDoorConfigurationActivity : AppCompatActivity(), CoroutineScope, SearchView.OnQueryTextListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val viewModel: ReferenceViewModel by viewModels()
    private val outdoorGridAdapter = OutdoorGridAdapter()
    private var outdoorEqpList = emptyList<OutdoorEquipementReference>()

    private val onOutdoorEqptsItemClickListener = View.OnClickListener { view ->
        val viewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.absoluteAdapterPosition
        val element = outdoorEqpList[position]
        launch {
            element.actif = !element.actif
            viewModel.saveOutdoorEquipementRepository(element)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keys_configuration)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        rcv_elements.layoutManager = FlexboxLayoutManager(this, ROW)
        txv_explication.text = getString(R.string.explication_eqpts_config)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        menu?.findItem(R.id.action_next)?.isVisible = false
        menu?.findItem(R.id.action_compteur)?.isVisible = false
        menu?.findItem(R.id.action_add_room)?.isVisible = true

        val search = menu?.findItem(R.id.action_search)
        search?.isVisible = true
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return true
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_previous -> {
                finish()
            }
            R.id.action_add_room -> {
                MaterialDialog(this).show {
                    title(R.string.add_element)
                    input(allowEmpty = false) { _, text ->
                        launch {
                            viewModel.saveOutdoorEquipementRepository(OutdoorEquipementReference(text.toString(), true))
                        }
                    }
                    positiveButton(R.string.rename)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getOutdoorEqpts.observe(this) { res ->
            outdoorEqpList = res
            outdoorGridAdapter.swapData(res)
            rcv_elements.adapter = outdoorGridAdapter
            outdoorGridAdapter.setOnItemClickListener(onOutdoorEqptsItemClickListener)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return  true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchOutdoorEqptsQuery(searchQuery).observe(this) { list ->
            list.let {
                outdoorGridAdapter.swapData(it)
            }
        }
    }
}