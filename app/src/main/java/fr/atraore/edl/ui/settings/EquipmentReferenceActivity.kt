package fr.atraore.edl.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R

@AndroidEntryPoint
class EquipmentReferenceActivity : AppCompatActivity() {
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_reference)

        val recyclerView = findViewById<RecyclerView>(R.id.rcvEquipmentReference)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = EquipmentReferenceAdapter(mutableListOf(), viewModel)
        recyclerView.adapter = adapter

        viewModel.getAllEquipmentReferences().observe(this) { list ->
            adapter.updateList(list)
        }
    }
}