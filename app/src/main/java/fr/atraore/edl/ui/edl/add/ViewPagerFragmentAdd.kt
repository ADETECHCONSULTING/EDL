package fr.atraore.edl.ui.edl.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.ViewPagerAdapter
import fr.atraore.edl.ui.edl.add.contractor.AddContractorFragment
import fr.atraore.edl.ui.edl.add.owner.AddOwnerFragment
import fr.atraore.edl.ui.edl.add.property.AddPropertyFragment
import fr.atraore.edl.ui.edl.add.tenant.AddTenantFragment
import fr.atraore.edl.utils.ARGS_TAB_POSITION
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragmentAdd : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            AddPropertyFragment.newInstance(),
            AddOwnerFragment.newInstance(),
            AddTenantFragment.newInstance(),
            AddContractorFragment.newInstance()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.viewPager.adapter = adapter

        TabLayoutMediator(view.tab_layout, view.viewPager) { tab, position ->
            tab.text = (fragmentList[position] as BaseFragment<*>).title
        }.attach()

        view.viewPager.setCurrentItem(arguments?.getInt(ARGS_TAB_POSITION) ?: 0, true)

        return view
    }

}