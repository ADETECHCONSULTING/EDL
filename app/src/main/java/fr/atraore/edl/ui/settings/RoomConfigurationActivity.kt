package fr.atraore.edl.ui.settings

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.databinding.ActivityRoomConfigurationBinding
import fr.atraore.edl.ui.ReferenceViewModel
import fr.atraore.edl.ui.adapter.ElementGridAdapter
import fr.atraore.edl.ui.adapter.RoomSimpleAdapter
import fr.atraore.edl.utils.observeOnce
import kotlinx.android.synthetic.main.activity_room_configuration.imv_chauffage
import kotlinx.android.synthetic.main.activity_room_configuration.imv_elec
import kotlinx.android.synthetic.main.activity_room_configuration.imv_electromenager
import kotlinx.android.synthetic.main.activity_room_configuration.imv_lot_revetement
import kotlinx.android.synthetic.main.activity_room_configuration.imv_meulbe
import kotlinx.android.synthetic.main.activity_room_configuration.imv_mobilier
import kotlinx.android.synthetic.main.activity_room_configuration.imv_ouvrants
import kotlinx.android.synthetic.main.activity_room_configuration.imv_plomberie
import kotlinx.android.synthetic.main.activity_room_configuration.rcv_elements
import kotlinx.android.synthetic.main.activity_room_configuration.rcv_rooms
import kotlinx.android.synthetic.main.activity_room_configuration.toolbar
import kotlinx.android.synthetic.main.element_grid_item.view.checkbox_element
import kotlinx.android.synthetic.main.room_simple_list_item.view.rb_room_parent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class RoomConfigurationActivity : AppCompatActivity(), CoroutineScope, SearchView.OnQueryTextListener {
    private val TAG = RoomConfigurationActivity::class.simpleName

    private val viewModel: ReferenceViewModel by viewModels()
    private val elementGridAdapter = ElementGridAdapter()
    private val roomSimpleAdapter = RoomSimpleAdapter()
    private var roomsList = emptyList<RoomReference>()
    private var elementList = emptyList<ElementReference>()
    private lateinit var currentRoomSelected: RoomReference
    private var clickedLot: Int = 1
    private lateinit var binding: ActivityRoomConfigurationBinding

    private val onRoomItemClickListener = View.OnClickListener { view ->
        val roomSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = roomSimpleViewHolder.absoluteAdapterPosition
        val room = roomsList[position]
        currentRoomSelected = room
        resetElementStateCheckbox(position)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomConfigurationBinding.inflate(layoutInflater)
        binding.activity = this
        val view = binding.root
        setContentView(view)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        rcv_rooms.layoutManager = LinearLayoutManager(this)

        val layoutManager = FlexboxLayoutManager(this, ROW)
        rcv_elements.layoutManager = layoutManager
        //rcv_elements.addItemDecoration(ItemOffsetDecoration(this, R.dimen.grid_offset))

        rcv_rooms.postDelayed({
            rcv_rooms.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
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

    override fun onStart() {
        super.onStart()

        onLotTechniqueClick(imv_lot_revetement, 1)

        viewModel.getRooms.observeOnce(this) { roomRes ->
            roomsList = roomRes
            currentRoomSelected = roomRes[0]
            getElementsSavedForRoom(currentRoomSelected.name)
            roomSimpleAdapter.swapData(roomRes)
            rcv_rooms.adapter = roomSimpleAdapter
            roomSimpleAdapter.setOnItemClickListener(onRoomItemClickListener)
        }
    }

    private fun getElementsSavedForRoom(roomName: String) {
        roomName.let {
            viewModel.getRoomWithElements(roomName, clickedLot).observeOnce(this) { roomWithElements ->
                roomWithElements?.apply {
                    elementList.forEachIndexed { index, adapterElementRef ->
                        if (adapterElementRef.elementReferenceId in this.elements.map { el -> el.elementReferenceId }) {
                            val viewHolder = rcv_elements.findViewHolderForAdapterPosition(index)
                            viewHolder?.let { holder ->
                                holder.itemView.checkbox_element.isChecked = true
                            }
                        }
                    }
                } ?: run {
                    elementList.forEachIndexed { index, adapterElementRef ->
                        val viewHolder = rcv_elements.findViewHolderForAdapterPosition(index)
                        viewHolder?.let { holder ->
                            holder.itemView.checkbox_element.isChecked = false
                        }
                    }
                }
            }
        }
    }

    private fun resetElementStateCheckbox(position: Int) {
        elementList.forEachIndexed { index, adapterElementRef ->
            val viewHolder = rcv_elements.findViewHolderForAdapterPosition(index)
            viewHolder?.let { holder ->
                holder.itemView.checkbox_element.isChecked = false
            }
        }

        roomsList.forEachIndexed { index, adapterElementRef ->
            val viewHolder = rcv_rooms.findViewHolderForAdapterPosition(index)
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
        imv_lot_revetement.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_mur,
                theme
            )
        )
        imv_lot_revetement.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        imv_ouvrants.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_ouvrant,
                theme
            )
        )
        imv_ouvrants.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        imv_elec.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_elec, theme))
        imv_elec.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        imv_plomberie.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_plomberie,
                theme
            )
        )
        imv_plomberie.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        imv_chauffage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_chauffage,
                theme
            )
        )
        imv_chauffage.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        imv_electromenager.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_electromenager,
                theme
            )
        )
        imv_electromenager.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        imv_mobilier.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_mobilier,
                theme
            )
        )
        imv_mobilier.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        imv_meulbe.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_meuble,
                theme
            )
        )
        imv_meulbe.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

}