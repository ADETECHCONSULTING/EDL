package fr.atraore.edl.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.ui.adapter.ConstatAdapter
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((activity?.application as EdlApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = ConstatAdapter()
        rcv_constat.adapter = adapter
        rcv_constat.layoutManager = LinearLayoutManager(context)

        mainViewModel.allConstats.observe(viewLifecycleOwner, Observer { constats ->
            constats?.let { adapter.submitList(it) }
        })

        initListeners()
    }



    fun initListeners() {
        btn_entrant.setOnClickListener {
            findNavController().navigate(R.id.go_to_start)
        }
        btn_pre_etat.setOnClickListener {
            findNavController().navigate(R.id.go_to_start)
        }
        btn_sortant.setOnClickListener {
            findNavController().navigate(R.id.go_to_start)
        }
    }

}