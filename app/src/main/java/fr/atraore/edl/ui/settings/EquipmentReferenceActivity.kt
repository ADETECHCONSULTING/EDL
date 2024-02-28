package fr.atraore.edl.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.utils.observeOnce

@AndroidEntryPoint
class EquipmentReferenceActivity : AppCompatActivity() {
    private val viewModel: ListViewModel by viewModels()
    private lateinit var adapter: EquipmentReferenceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_reference)

        val recyclerView = findViewById<RecyclerView>(R.id.rcvEquipmentReference)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EquipmentReferenceAdapter(mutableListOf(), viewModel)
        recyclerView.adapter = adapter

        viewModel.getAllEquipmentReferences().observe(this) { list ->
            adapter.updateList(list)
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText.orEmpty())
                return false
            }
        })
    }

    private fun filterList(query: String) {
        viewModel.getAllEquipmentReferences().observeOnce(this@EquipmentReferenceActivity) {
            val filteredList = it.filter { equipmentReference ->
                equipmentReference.level1.contains(query, ignoreCase = true) ||
                        equipmentReference.level2.contains(query, ignoreCase = true) ||
                        equipmentReference.level3?.contains(query, ignoreCase = true) == true
            }

            adapter.updateList(filteredList)
        }
    }
}