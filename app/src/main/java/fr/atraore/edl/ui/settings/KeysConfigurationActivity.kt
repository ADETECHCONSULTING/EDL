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
import fr.atraore.edl.data.models.entity.KeyReference
import fr.atraore.edl.databinding.ActivityKeysConfigurationBinding
import fr.atraore.edl.ui.ReferenceViewModel
import fr.atraore.edl.ui.adapter.KeysGridAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class KeysConfigurationActivity : AppCompatActivity(), CoroutineScope, SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityKeysConfigurationBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private val viewModel: ReferenceViewModel by viewModels()
    private val keysGridAdapter = KeysGridAdapter()
    private var keysList = emptyList<KeyReference>()

    private val onKeyItemClickListener = View.OnClickListener { view ->
        val viewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.absoluteAdapterPosition
        val element = keysList[position]
        launch {
            element.actif = !element.actif
            viewModel.saveKey(element)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keys_configuration)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.rcvElements.layoutManager = FlexboxLayoutManager(this, ROW)
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
                    title(R.string.add_key)
                    input(allowEmpty = false) { _, text ->
                        launch {
                            viewModel.saveKey(KeyReference(text.toString(), true))
                        }
                    }
                    positiveButton(R.string.done)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getKeys.observe(this) { res ->
            keysList = res
            keysGridAdapter.swapData(res)
            binding.rcvElements.adapter = keysGridAdapter
            keysGridAdapter.setOnItemClickListener(onKeyItemClickListener)
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
        viewModel.searchKeysQuery(searchQuery).observe(this) { list ->
            list.let {
                keysGridAdapter.swapData(it)
            }
        }
    }
}