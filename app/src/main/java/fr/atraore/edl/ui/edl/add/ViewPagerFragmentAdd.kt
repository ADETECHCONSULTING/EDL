package fr.atraore.edl.ui.edl.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.ViewPagerAdapter
import fr.atraore.edl.ui.edl.add.contractor.AddContractorFragment
import fr.atraore.edl.ui.edl.add.owner.AddOwnerFragment
import fr.atraore.edl.ui.edl.add.property.AddPropertyFragment
import fr.atraore.edl.ui.edl.add.tenant.AddTenantFragment
import fr.atraore.edl.utils.*
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragmentAdd : BaseFragment("ViewPager") {

    override val title: String
        get() = "ViewPager"

    override fun goNext() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_add_room)?.isVisible = false
        menu.findItem(R.id.action_compteur)?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_next -> {
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            AddPropertyFragment.newInstance(arguments?.getString(ARGS_PROPERTY_ID)),
            AddOwnerFragment.newInstance(arguments?.getString(ARGS_OWNER_ID)),
            AddTenantFragment.newInstance(arguments?.getString(ARGS_TENANT_ID)),
            AddContractorFragment.newInstance(arguments?.getString(ARGS_CONTRACTOR_ID))
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.viewPager.adapter = adapter

        TabLayoutMediator(view.tab_layout, view.viewPager) { tab, position ->
            tab.text = (fragmentList[position] as BaseFragment).title
        }.attach()

        view.viewPager.setCurrentItem(arguments?.getInt(ARGS_TAB_POSITION) ?: 0, true)

        return view
    }

}