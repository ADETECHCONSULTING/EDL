package fr.atraore.edl.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.utils.LOTS_LABELS
import kotlinx.android.synthetic.main.activity_equipment_configuration.btnAddFirstLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.btnAddSecondLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.btnAddThridLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.imv_lot_revetement
import kotlinx.android.synthetic.main.activity_equipment_configuration.rcvFirstLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.rcvSecondLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.rcvThirdLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.searchView

@AndroidEntryPoint
class EquipmentConfigurationActivity : AppCompatActivity(), OnLevelsItemClickListener {
    private val viewModel: ListViewModel by viewModels()
    var selectedLotImageView: ImageView? = null
    var selectedLotIndex: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_configuration)

        setupList(rcvFirstLevel, btnAddFirstLevel, 1)
        setupList(rcvSecondLevel, btnAddSecondLevel, 2)
        setupList(rcvThirdLevel, btnAddThridLevel, 3)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText ?: ""
                viewModel.filterLevelOneItems(query).observe(this@EquipmentConfigurationActivity) { list ->
                    (rcvFirstLevel.adapter as ListAdapter).updateList(list)
                }
                viewModel.filterLevelTwoItems(query).observe(this@EquipmentConfigurationActivity) { list ->
                    (rcvSecondLevel.adapter as ListAdapter).updateList(list)
                }
                viewModel.filterLevelThreeItems(query).observe(this@EquipmentConfigurationActivity) { list ->
                    (rcvThirdLevel.adapter as ListAdapter).updateList(list)
                }
                return false
            }
        })

        val imageViewIds = listOf(
            R.id.imv_lot_revetement,
            R.id.imv_ouvrants,
            R.id.imv_elec,
            R.id.imv_plomberie,
            R.id.imv_chauffage,
            R.id.imv_electromenager,
            R.id.imv_mobilier,
            R.id.imv_meuble
        )

        for ((index, id) in imageViewIds.withIndex()) {
            findViewById<ImageView>(id).setOnClickListener { view ->
                selectedLotImageView?.setBackgroundColor(Color.TRANSPARENT)
                view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                selectedLotImageView = view as ImageView
                selectedLotIndex = index + 1
            }
        }

        imv_lot_revetement.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        selectedLotImageView = imv_lot_revetement

        rcvSecondLevel.isEnabled = false
        rcvSecondLevel.alpha = 0.5f
        btnAddSecondLevel.isEnabled = false

        rcvThirdLevel.isEnabled = false
        rcvThirdLevel.alpha = 0.5f
        btnAddThridLevel.isEnabled = false
    }

    private fun setupList(recyclerView: RecyclerView, addButton: Button, level: Int) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ListAdapter(emptyList(), level, this)
        recyclerView.adapter = adapter

        when(level) {
            1 -> {
                viewModel.listDataFirstLevel(selectedLotIndex).observe(this) { list ->
                    adapter.updateList(list)
                }
                addButton.setOnClickListener {
                    viewModel.addItemFirstLevel("New Item", selectedLotIndex)
                    adapter.notifyDataSetChanged()
                }
            }
            2 -> {
                viewModel.listDataSecondLevel(selectedLotIndex).observe(this) { list ->
                    adapter.updateList(list)
                }
                addButton.setOnClickListener {
                    val selectedFirstLevelItem = (rcvFirstLevel.adapter as ListAdapter).getSelectedItem()
                    viewModel.addItemSecondLevel(selectedFirstLevelItem, "New Item", selectedLotIndex)
                    adapter.notifyDataSetChanged()
                }
            }
            3 -> {
                viewModel.listDataThirdLevel(selectedLotIndex).observe(this) { list ->
                    adapter.updateList(list)
                }
                addButton.setOnClickListener {
                    val selectedFirstLevelItem = (rcvFirstLevel.adapter as ListAdapter).getSelectedItem()
                    viewModel.addItemThirdLevel(selectedFirstLevelItem, "New Item", selectedLotIndex)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onFirstLevelItemClick() {
        rcvSecondLevel.isEnabled = true
        rcvSecondLevel.alpha = 1f
        btnAddSecondLevel.isEnabled = true
    }

    override fun onSecondLevelItemClick() {
        rcvThirdLevel.isEnabled = true
        rcvThirdLevel.alpha = 1f
        btnAddThridLevel.isEnabled = true
    }

    override fun onThirdLevelItemClick() {
        val levelOne = (rcvFirstLevel.adapter as ListAdapter).getSelectedItem()
        val levelTwo = (rcvSecondLevel.adapter as ListAdapter).getSelectedItem()
        val levelThree = (rcvThirdLevel.adapter as ListAdapter).getSelectedItem()
        viewModel.equipmentExists(levelOne, levelTwo, levelThree, selectedLotIndex).observe(this) { exists ->
            if (!exists) {
                showConfirmationDialog(levelOne, levelTwo, levelThree)
            } else {
                Toast.makeText(this, "Un équipement avec ces niveaux dans let lot ${LOTS_LABELS[selectedLotIndex-1]} existe déjà", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog(levelOne: String, levelTwo: String, levelThree: String) {
        MaterialDialog(this).show {
            title(text = "Voulez vous sauvegarder l'équipement au niveau du lot : ${LOTS_LABELS[selectedLotIndex-1]} ?")
            message(text = "Niveau 1: $levelOne\nNiveau 2: $levelTwo\nNiveau 3: $levelThree")
            positiveButton(text = "Valider") {
                // Sauvegarder les valeurs dans le repository
                Toast.makeText(this@EquipmentConfigurationActivity, "Sauvegarde effectuée", Toast.LENGTH_SHORT).show()
            }
            negativeButton(text = "Annuler")
            lifecycleOwner(this@EquipmentConfigurationActivity)
        }
    }
}


