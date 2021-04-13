package fr.atraore.edl.ui.edl.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.ViewPagerAdapter
import fr.atraore.edl.ui.edl.search.agency.AgencySearchFragment
import fr.atraore.edl.ui.edl.search.biens.PropertySearchFragment
import fr.atraore.edl.ui.edl.search.contractor.ContractorSearchFragment
import fr.atraore.edl.ui.edl.search.owner.OwnerSearchFragment
import fr.atraore.edl.ui.edl.search.tenant.TenantSearchFragment
import fr.atraore.edl.ui.edl.start.StartConstatViewModel
import fr.atraore.edl.ui.edl.start.StartConstatViewModelFactory
import fr.atraore.edl.utils.ARGS_CONSTAT
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.ARGS_TAB_POSITION
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        val constat = arguments?.getSerializable(ARGS_CONSTAT) as ConstatWithDetails

        //#TODO Gérer le cas où le constat id n'est pas été sauvegardé (lenteur de la tablette)
        val fragmentList = arrayListOf<Fragment>(
            PropertySearchFragment.newInstance(constat),
            OwnerSearchFragment.newInstance(constat),
            TenantSearchFragment.newInstance(constat),
            ContractorSearchFragment.newInstance(constat),
            AgencySearchFragment.newInstance(constat)
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.viewPager.adapter = adapter

        //<*> pas besoin d'arguments typés vu qu'on récupère que le title
        TabLayoutMediator(view.tab_layout, view.viewPager) { tab, position ->
            tab.text = (fragmentList[position] as BaseFragment<*>).title
        }.attach()

        view.viewPager.setCurrentItem(arguments?.getInt(ARGS_TAB_POSITION) ?: 0, true)

        return view
    }

}