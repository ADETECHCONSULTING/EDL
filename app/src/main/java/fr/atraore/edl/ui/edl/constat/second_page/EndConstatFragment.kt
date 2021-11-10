package fr.atraore.edl.ui.edl.constat.second_page

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ElementReference
import fr.atraore.edl.data.models.RoomReference
import fr.atraore.edl.ui.adapter.AgencyAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.second_page.groupie.ChildItem
import fr.atraore.edl.ui.edl.constat.second_page.groupie.ParentItem
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.TwoPaneOnBackPressedCallback
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.android.synthetic.main.end_constat_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class EndConstatFragment() : BaseFragment("EndConstat"), LifecycleObserver,
MainActivity.OnNavigationFragment, CoroutineScope, ChildItem.IActionHandler {
    private val TAG = EndConstatFragment::class.simpleName

    override val title: String
        get() = "Deuxieme partie du constat"

    override fun goNext() {
        TODO("Not yet implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var groupLayoutManager: LinearLayoutManager

    private lateinit var parentList: List<ParentItem>
    var roomRefList: List<RoomReference>? = null

    companion object {
        fun newInstance() = EndConstatFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).mNavigationFragment = this
    }

    @Inject
    lateinit var constatViewModelFactory: ConstatViewModel.AssistedStartFactory
    private val viewModel: ConstatViewModel by assistedViewModel {
        constatViewModelFactory.create(arguments?.getString(ARGS_CONSTAT_ID)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.end_constat_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connect the SlidingPaneLayout to the system back button.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            TwoPaneOnBackPressedCallback(sliding_pane_layout)
        )

        btn_add_room.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(R.string.title_add_rooms)
                listItemsMultiChoice(items = roomRefList?.map { roomReference -> roomReference.name }) { dialog, indices, items ->
                    val roomsToAdd: List<RoomReference>? = roomRefList?.filter { roomRef -> roomRef.name in items }
                    roomsToAdd?.forEach {
                        launch {
                            viewModel.saveConstatRoomCrossRef(arguments?.getString(ARGS_CONSTAT_ID)!!, it.roomReferenceId)
                        }
                    }
                }
                lifecycleOwner(this@EndConstatFragment)
                positiveButton(R.string.done)
            }
        }

        groupLayoutManager = LinearLayoutManager(activity)

        rcv_rooms.apply {
            layoutManager = groupLayoutManager
            adapter = groupAdapter
        }

        viewModel.constatDetail.observe(viewLifecycleOwner, { constatWithDetails ->
            constatWithDetails?.let {
                parentList = constatWithDetails.rooms.map { ParentItem(it) }

                parentList.forEach { parentIt ->

                    viewModel.roomCombinedLiveData(parentIt.roomParent.roomReferenceId).observe(viewLifecycleOwner, { pairInfoRoom ->
                        pairInfoRoom.first?.let { roomWithElements ->
                            ExpandableGroup(parentIt, false).apply {
                                add(Section(roomWithElements.elements.map { ChildItem(it, this@EndConstatFragment) }))
                                groupAdapter.add(this)
                            }
                        }

                        pairInfoRoom.second?.let { listRoomReference ->
                            this.roomRefList = listRoomReference

                            btn_add_room.isEnabled = this.roomRefList?.isEmpty() != true
                        }
                    })

                    viewModel.getRoomWithElements(parentIt.roomParent.roomReferenceId).observe(viewLifecycleOwner, { roomWithElements ->
                        roomWithElements?.let {

                        }
                    })
                }
            }
        })
    }

    // A method on the Fragment that owns the SlidingPaneLayout,
    // called by the adapter when an item is selected.
    fun openDetails(itemId: String) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            val fragment = DetailEndConstatFragment.newInstance()
            fragment.arguments = bundleOf("itemId" to itemId)
            replace(R.id.fragment_detail, fragment)
            // If we're already open and the detail pane is visible,
            // crossfade between the fragments.
            if (sliding_pane_layout.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        sliding_pane_layout.open()
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }

    override fun onLongClick(elementReference: ElementReference) {
        val dialog = MaterialDialog(requireContext()).show {
            input(prefill = elementReference.name, allowEmpty = false) { _, text ->
                elementReference.name = text.toString()
            }
            positiveButton(R.string.rename)
        }

    }

    override fun onSimpleClick(elementReference: ElementReference) {
        openDetails(elementReference.elementReferenceId)
    }


}