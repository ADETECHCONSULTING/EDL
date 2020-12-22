package fr.atraore.edl.ui.edl.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.search.agency.AgencySearchFragment
import fr.atraore.edl.ui.edl.search.biens.PropertySearchFragment
import fr.atraore.edl.ui.edl.search.contractor.ContractorSearchFragment
import fr.atraore.edl.ui.edl.search.owner.OwnerSearchFragment
import fr.atraore.edl.ui.edl.search.tenant.TenantSearchFragment
import fr.atraore.edl.ui.edl.search.user.UserSearchFragment
import fr.atraore.edl.utils.ARGS_TAB_POSITION
import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            PropertySearchFragment.newInstance(),
            OwnerSearchFragment.newInstance(),
            TenantSearchFragment.newInstance(),
            ContractorSearchFragment.newInstance(),
            AgencySearchFragment.newInstance()
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