package fr.atraore.edl.ui.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.ui.ReferenceViewModel
import fr.atraore.edl.ui.adapter.ElementGridAdapter
import fr.atraore.edl.ui.adapter.RoomSimpleAdapter
import fr.atraore.edl.utils.observeOnce
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_room_configuration.*
import kotlinx.android.synthetic.main.activity_room_configuration.toolbar
import kotlinx.android.synthetic.main.element_grid_item.view.*
import kotlinx.android.synthetic.main.room_simple_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class RoomConfigurationActivity : AppCompatActivity(), CoroutineScope, SearchView.OnQueryTextListener {
    private val TAG = RoomConfigurationActivity::class.simpleName
    lateinit var mNavigationFragment: MainActivity.OnNavigationFragment

    private val viewModel: ReferenceViewModel by viewModels()
    private val elementGridAdapter = ElementGridAdapter()
    private val roomSimpleAdapter = RoomSimpleAdapter()
    private var roomsList = emptyList<RoomReference>()
    private var elementList = emptyList<ElementReference>()
    private lateinit var currentRoomSelected: RoomReference

    private val onRoomItemClickListener = View.OnClickListener { view ->
        val roomSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = roomSimpleViewHolder.absoluteAdapterPosition
        val room = roomsList[position]
        currentRoomSelected = room
        resetElementStateCheckbox(position)
        getElementsSavedForRoom(room.roomReferenceId)
    }

    private val onElementItemClickListener = View.OnClickListener { view ->
        val elementSimpleViewHolder: RecyclerView.ViewHolder = view.tag as RecyclerView.ViewHolder
        val position = elementSimpleViewHolder.absoluteAdapterPosition
        val element = elementList.get(position)
        launch {
            viewModel.saveRoomWithElements(currentRoomSelected.roomReferenceId, element.elementReferenceId)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_configuration)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        rcv_rooms.layoutManager = LinearLayoutManager(this)
        rcv_elements.layoutManager = GridLayoutManager(this, 4)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        menu?.findItem(R.id.action_next)?.isVisible = false
        menu?.findItem(R.id.action_previous)?.isVisible = false
        menu?.findItem(R.id.action_compteur)?.isVisible = false
        menu?.findItem(R.id.action_add_room)?.isVisible = false

        val search = menu?.findItem(R.id.action_search)
        search?.isVisible = true
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getRooms.observe(this) { roomRes ->
            roomsList = roomRes
            currentRoomSelected = roomRes[0]
            getElementsSavedForRoom(currentRoomSelected.roomReferenceId)
            roomSimpleAdapter.swapData(roomRes)
            rcv_rooms.adapter = roomSimpleAdapter
            roomSimpleAdapter.setOnItemClickListener(onRoomItemClickListener)
        }

        viewModel.getElements.observe(this) { elementRes ->
            elementList = elementRes
            elementGridAdapter.swapData(elementRes)
            rcv_elements.adapter = elementGridAdapter
            elementGridAdapter.setOnItemClickListener(onElementItemClickListener)
        }
    }

    private fun getElementsSavedForRoom(idRoom: String) {
        idRoom.let {
            viewModel.getRoomWithElements(idRoom).observeOnce(this) { roomWithElements ->
                roomWithElements?.apply {
                    //elementGridAdapter.selectItemFromList()
                    elementList.forEachIndexed { index, adapterElementRef ->
                        if (adapterElementRef.elementReferenceId in this.elements.map { el -> el.elementReferenceId }) {
                            val viewHolder = rcv_elements.findViewHolderForAdapterPosition(index)
                            viewHolder?.let { holder ->
                                holder.itemView.checkbox_element.isChecked = true
                            }
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
        return  true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchElementQuery(searchQuery).observe(this) { list ->
            list.let {
                elementGridAdapter.swapData(it)
            }
        }
    }
}