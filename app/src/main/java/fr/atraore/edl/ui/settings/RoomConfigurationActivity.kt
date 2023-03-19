package fr.atraore.edl.ui.settings

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.databinding.ActivityRoomConfigurationBinding
import fr.atraore.edl.databinding.ElementGridItemBinding
import fr.atraore.edl.databinding.ElementListItemBinding
import fr.atraore.edl.ui.ReferenceViewModel
import fr.atraore.edl.ui.adapter.ElementGridAdapter
import fr.atraore.edl.ui.adapter.RoomSimpleAdapter
import fr.atraore.edl.utils.observeOnce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class RoomConfigurationActivity : AppCompatActivity(), CoroutineScope, SearchView.OnQueryTextListener {
    private val TAG = RoomConfigurationActivity::class.simpleName
    lateinit var mNavigationFragment: MainActivity.OnNavigationFragment
    private lateinit var binding: ActivityRoomConfigurationBinding

    private val viewModel: ReferenceViewModel by viewModels()
    private val elementGridAdapter = ElementGridAdapter()
    private val roomSimpleAdapter = RoomSimpleAdapter()
    private var roomsList = emptyList<RoomReference>()
    private var elementList = emptyList<ElementReference>()
    private lateinit var currentRoomSelected: RoomReference
    private var clickedLot: Int = 1

    private val onRoomItemClickListener = View.OnClickListener { view ->
        val roomSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = roomSimpleViewHolder.absoluteAdapterPosition
        val room = roomsList[position]
        currentRoomSelected = room
        resetElementStateCheckbox(position)

        viewModel.getElementsForRoom(room.roomReferenceId).observe(this) { elementRes ->
            elementList = elementRes
            elementGridAdapter.swapData(elementRes)
            binding.rcvElements.adapter = elementGridAdapter

            getElementsSavedForRoom(room.name)
        }
    }

    private val onElementItemClickListener = View.OnClickListener { view ->
        val elementSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = elementSimpleViewHolder.absoluteAdapterPosition
        val element = elementList.get(position)
        viewModel.getRoomWithNameAndIdLot(currentRoomSelected.name, clickedLot).observeOnce(this@RoomConfigurationActivity) { roomRefIfExist ->
            launch {
                if (roomRefIfExist == null) {
                    val roomReference = RoomReference(UUID.randomUUID().toString(), currentRoomSelected.name, clickedLot)
                    if (roomReference.name == "ACCES / ENTREE") {
                        roomReference.mandatory = true
                    }
                    currentRoomSelected = roomReference
                    viewModel.saveRoom(roomReference)
                } else {
                    currentRoomSelected = roomRefIfExist
                }

                if ((view as MaterialCheckBox).isChecked) {
                    viewModel.saveRoomWithElements(currentRoomSelected.roomReferenceId, element.elementReferenceId)
                } else {
                    viewModel.deleteRoomWithElements(currentRoomSelected.roomReferenceId, element.elementReferenceId)
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomConfigurationBinding.inflate(layoutInflater)
        binding.activity = this
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.rcvRooms.layoutManager = LinearLayoutManager(this)

        val layoutManager = FlexboxLayoutManager(this, ROW)
        binding.rcvElements.layoutManager = layoutManager
        //rcv_elements.addItemDecoration(ItemOffsetDecoration(this, R.dimen.grid_offset))

        binding.rcvRooms.postDelayed({
            binding.rcvRooms.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
        }, 400)
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
                    title(R.string.add_room)
                    input(allowEmpty = false) { _, text ->
                        launch {
                            viewModel.saveRoom(RoomReference(UUID.randomUUID().toString(), text.toString(), clickedLot, false))
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

        onLotTechniqueClick(binding.imvLotBatis, 1)

        viewModel.getRooms.observeOnce(this) { roomRes ->
            roomsList = roomRes
            currentRoomSelected = roomRes[0]
            getElementsSavedForRoom(currentRoomSelected.name)
            roomSimpleAdapter.swapData(roomRes)
            binding.rcvRooms.adapter = roomSimpleAdapter
            roomSimpleAdapter.setOnItemClickListener(onRoomItemClickListener)
        }

        elementGridAdapter.setOnItemClickListener(onElementItemClickListener)
    }

    private fun getElementsSavedForRoom(roomName: String) {
        roomName.let {
            viewModel.getRoomWithElements(roomName, clickedLot).observeOnce(this) { roomWithElements ->
                roomWithElements?.apply {
                    elementList.forEachIndexed { index, adapterElementRef ->
                        if (adapterElementRef.elementReferenceId in this.elements.map { el -> el.elementReferenceId }) {
                            val viewHolder = binding.rcvElements.findViewHolderForAdapterPosition(index)
                            viewHolder?.let { holder ->
                                holder.itemView.checkbox_element.isChecked = true
                            }
                        }
                    }
                } ?: run {
                    elementList.forEachIndexed { index, adapterElementRef ->
                        val viewHolder = binding.rcvElements.findViewHolderForAdapterPosition(index)
                        viewHolder?.let { holder ->
                            ElementGridItemBinding.bind(holder.itemView).checkboxElement.isChecked = false
                        }
                    }
                }
            }
        }
    }

    private fun resetElementStateCheckbox(position: Int) {
        elementList.forEachIndexed { index, adapterElementRef ->
            val viewHolder = binding.rcvElements.findViewHolderForAdapterPosition(index)
            viewHolder?.let { holder ->
                holder.itemView.checkbox_element.isChecked = false
            }
        }

        roomsList.forEachIndexed { index, adapterElementRef ->
            val viewHolder = binding.rcvRooms.findViewHolderForAdapterPosition(index)
            viewHolder?.let { holder ->
                if (position != index) {
                    holder.itemView.rb_room_parent.isChecked = false
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //onQueryTextChange a la priorité
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchElementQuery(searchQuery).observe(this) { list ->
            list.let {
                elementGridAdapter.swapData(it)
            }
        }
    }

    //click on lot technique
    fun onLotTechniqueClick(view: View, idLot: Int) {
        unClickAllLotTechnique(theme)
        val themeClick = resources.newTheme()
        themeClick.applyStyle(R.style.ClickedLot, false)
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        changeTheme(themeClick, view as ImageView, idLot)
        if (clickedLot != idLot) {
            clickedLot = idLot
            getElementsSavedForRoom(currentRoomSelected.name)
        }
    }


    private fun changeTheme(theme: Resources.Theme, view: ImageView, idLot: Int) {
        val drawable = drawableLotTechnique(idLot, theme)
        view.setImageDrawable(drawable)
    }

    private fun drawableLotTechnique(idLot: Int, theme: Resources.Theme): Drawable? {
        return when (idLot) {
            1 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_mur, theme)
            2 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_ouvrant, theme)
            3 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_elec, theme)
            4 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_plomberie, theme)
            5 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_chauffage, theme)
            6 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_electromenager, theme)
            7 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_mobilier, theme)
            8 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_meuble, theme)
            else -> null
        }
    }

    private fun unClickAllLotTechnique(theme: Resources.Theme) {
        theme.applyStyle(R.style.DefaultLot, false)
        binding.imvLotBatis.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_mur,
                theme
            )
        )
        binding.imvLotBatis.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.imvOuvrants.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_ouvrant,
                theme
            )
        )
        binding.imvOuvrants.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.imvElec.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_elec, theme))
        binding.imvElec.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.imvPlomberie.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_plomberie,
                theme
            )
        )
        binding.imvPlomberie.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.imvChauffage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_chauffage,
                theme
            )
        )
        binding.imvChauffage.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.imvElectromenager.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_electromenager,
                theme
            )
        )
        binding.imvElectromenager.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.imvMobilier.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_mobilier,
                theme
            )
        )
        binding.imvMobilier.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.imvMeulbe.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_meuble,
                theme
            )
        )
        binding.imvMeulbe.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

}