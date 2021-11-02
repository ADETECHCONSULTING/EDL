package fr.atraore.edl.ui.edl.second_page

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import fr.atraore.edl.R
import kotlinx.android.synthetic.main.end_constat_fragment.*

class EndConstatFragment : Fragment() {

    companion object {
        fun newInstance() = EndConstatFragment()
    }

    private lateinit var viewModel: EndConstatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.end_constat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EndConstatViewModel::class.java)
        // TODO: Use the ViewModel
    }

    // A method on the Fragment that owns the SlidingPaneLayout,
// called by the adapter when an item is selected.
    fun openDetails(itemId: Int) {
        // Assume the NavHostFragment is added with the +id/detail_container.
        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.detail_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(
            // Assume the itemId is the android:id of a destination in the graph.
            itemId,
            null,
            NavOptions.Builder()
                // Pop all destinations off the back stack.
                .setPopUpTo(navController.graph.startDestination, true)
                .apply {
                    // If we're already open and the detail pane is visible,
                    // crossfade between the destinations.
                    if (sliding_pane_layout.isOpen) {
                        setEnterAnim(R.animator.nav_default_enter_anim)
                        setExitAnim(R.animator.nav_default_exit_anim)
                    }
                }
                .build()
        )
        sliding_pane_layout.open()
    }

}