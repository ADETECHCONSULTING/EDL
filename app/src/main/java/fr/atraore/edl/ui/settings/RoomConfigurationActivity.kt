package fr.atraore.edl.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.ui.ReferenceViewModel
import fr.atraore.edl.ui.adapter.ElementGridAdapter
import fr.atraore.edl.ui.adapter.RoomSimpleAdapter
import kotlinx.android.synthetic.main.activity_room_configuration.*

@AndroidEntryPoint
class RoomConfigurationActivity : AppCompatActivity() {
    private val TAG = RoomConfigurationActivity::class.simpleName
    lateinit var mNavigationFragment: MainActivity.OnNavigationFragment

    private val viewModel: ReferenceViewModel by viewModels()
    private val elementGridAdapter = ElementGridAdapter()
    private val roomSimpleAdapter = RoomSimpleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_configuration)

        rcv_rooms.layoutManager = LinearLayoutManager(this)
        rcv_elements.layoutManager = GridLayoutManager(this, 4)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getElements.observe(this) { elementRes ->
            elementGridAdapter.swapData(elementRes)
            rcv_elements.adapter = elementGridAdapter
        }

        viewModel.getRooms.observe(this) { roomRes ->
            roomSimpleAdapter.swapData(roomRes)
            rcv_rooms.adapter = roomSimpleAdapter
        }
    }
}