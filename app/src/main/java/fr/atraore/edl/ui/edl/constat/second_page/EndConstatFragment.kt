package fr.atraore.edl.ui.edl.constat.second_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.second_page.groupie.ChildItem
import fr.atraore.edl.ui.edl.constat.second_page.groupie.ParentItem
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.android.synthetic.main.end_constat_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class EndConstatFragment() : BaseFragment("EndConstat"), LifecycleObserver,
MainActivity.OnNavigationFragment, CoroutineScope {

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

    companion object {
        fun newInstance() = EndConstatFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).mNavigationFragment = this
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

        groupLayoutManager = LinearLayoutManager(activity)

        rcv_rooms.apply {
            layoutManager = groupLayoutManager
            adapter = groupAdapter
        }

        viewModel.constatDetail.observe(viewLifecycleOwner, { constatWithDetails ->
            constatWithDetails?.let {
                parentList = constatWithDetails.rooms.map { ParentItem(it) }

                parentList.forEach { parentIt ->
                    ExpandableGroup(parentIt, false).apply {
                        constatWithDetails.elements.let { childIt ->
                            add(Section(childIt.map { ChildItem(it) }))
                        }
                        groupAdapter.add(this)
                    }
                }
            }
        })

    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }

}