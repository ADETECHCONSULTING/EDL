package fr.atraore.edl.ui.settings

import android.content.Intent
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
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.utils.LOTS_LABELS
import fr.atraore.edl.utils.ROOMS_LABELS
import fr.atraore.edl.utils.observeOnce
import kotlinx.android.synthetic.main.activity_equipment_configuration.btnAddFirstLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.btnAddSecondLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.btnAddThridLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.goToEditEquipment
import kotlinx.android.synthetic.main.activity_equipment_configuration.imv_lot_revetement
import kotlinx.android.synthetic.main.activity_equipment_configuration.rcvFirstLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.rcvRooms
import kotlinx.android.synthetic.main.activity_equipment_configuration.rcvSecondLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.rcvThirdLevel
import kotlinx.android.synthetic.main.activity_equipment_configuration.searchView

@AndroidEntryPoint
class EquipmentConfigurationActivity : AppCompatActivity(), OnLevelsItemClickListener {
    private val viewModel: ListViewModel by viewModels()
    var selectedLotImageView: ImageView? = null
    var selectedLotIndex: Int = 1
    var selectedRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_configuration)

        rcvRooms.layoutManager = LinearLayoutManager(this)
        val roomAdapter = ListAdapter(mutableListOf(), 0, this)
        rcvRooms.adapter = roomAdapter

        viewModel.getAllRoomReferences.observe(this) { list ->
            roomAdapter.updateList(list.map { it.name })
        }
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

        goToEditEquipment.setOnClickListener {
            val intent = Intent(this, EquipmentReferenceActivity::class.java)
            startActivity(intent)
        }

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
        val adapter = ListAdapter(mutableListOf(), level, this)
        recyclerView.adapter = adapter

        when(level) {
            1 -> {
                viewModel.listDataFirstLevel(selectedLotIndex).observe(this) { list ->
                    adapter.updateList(list)
                }
                addButton.setOnClickListener {
                    showAddElement(1)
                }
            }
            2 -> {
                viewModel.listDataSecondLevel(selectedLotIndex).observe(this) { list ->
                    adapter.updateList(list)
                }
                addButton.setOnClickListener {
                    showAddElement(2)
                }
            }
            3 -> {
                viewModel.listDataThirdLevel(selectedLotIndex).observe(this) { list ->
                    adapter.updateList(list)
                }
                addButton.setOnClickListener {
                    showAddElement(3)
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
        if (levelOne != null && levelTwo != null && levelThree != null && selectedRoom != null) {
            val roomId = ROOMS_LABELS.indexOf(selectedRoom) + 1
            viewModel.equipmentExists(levelOne, levelTwo, levelThree, selectedLotIndex, roomId).observeOnce(this) { exists ->
                if (!exists) {
                    showConfirmationDialog(levelOne, levelTwo, levelThree)
                } else {
                    Toast.makeText(this, "Un équipement avec ces niveaux dans le lot ${LOTS_LABELS[selectedLotIndex-1]} existe déjà", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRoomItemClick() {
        selectedRoom = (rcvRooms.adapter as ListAdapter).getSelectedItem()
    }

    private fun showConfirmationDialog(levelOne: String, levelTwo: String, levelThree: String) {
        MaterialDialog(this).show {
            title(text = "Voulez vous sauvegarder l'équipement au niveau du lot : ${LOTS_LABELS[selectedLotIndex-1]} ?")
            message(text = "Pièce: ${selectedRoom}\nNiveau 1: $levelOne\nNiveau 2: $levelTwo\nNiveau 3: $levelThree")
            positiveButton(text = "Valider") {
                val roomId = ROOMS_LABELS.indexOf(selectedRoom) + 1
                viewModel.saveEquipements(levelOne, levelTwo, levelThree, selectedLotIndex, roomId)
                Toast.makeText(this@EquipmentConfigurationActivity, "Equipement sauvegardé", Toast.LENGTH_SHORT).show()
            }
            negativeButton(text = "Annuler")
            lifecycleOwner(this@EquipmentConfigurationActivity)
        }
    }

    private fun showAddElement(level: Int) {
        MaterialDialog(this).show {
            title(text = "Ajouter un nouvel élément")
            input { _, text ->
                when(level) {
                    1 -> {
                        val adapter = this@EquipmentConfigurationActivity.rcvFirstLevel.adapter as ListAdapter

                        val newList = mutableListOf(text.toString())
                        newList.addAll(adapter.getList())
                        adapter.updateList(newList)
                    }
                    2 -> {
                        val adapter = this@EquipmentConfigurationActivity.rcvSecondLevel.adapter as ListAdapter
                        val newList = mutableListOf(text.toString())
                        newList.addAll(adapter.getList())
                        adapter.updateList(newList)
                    }
                    3 -> {
                        val adapter = this@EquipmentConfigurationActivity.rcvThirdLevel.adapter as ListAdapter
                        val newList = mutableListOf(text.toString())
                        newList.addAll(adapter.getList())
                        adapter.updateList(newList)
                    }
                }
            }
            positiveButton(text = "Ajouter")
            negativeButton(text = "Annuler")
            lifecycleOwner(this@EquipmentConfigurationActivity)
        }
    }
}


