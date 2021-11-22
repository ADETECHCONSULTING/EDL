package fr.atraore.edl.ui.edl.constat.second_page.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.*
import fr.atraore.edl.databinding.FragmentDetailEndConstatBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.SUITE_CONSTAT_LABEL
import kotlinx.android.synthetic.main.fragment_detail_end_constat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class DetailEndConstatFragment : BaseFragment(SUITE_CONSTAT_LABEL),
    MainActivity.OnNavigationFragment, CoroutineScope {

    override val title: String
        get() = SUITE_CONSTAT_LABEL

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var binding: FragmentDetailEndConstatBinding

    private val viewModel: DetailEndConstatViewModel by viewModels()
    private lateinit var detail: Detail
    private lateinit var alterations: List<Alteration>
    private lateinit var etats: List<Etat>
    private lateinit var proprete: List<Proprete>
    private lateinit var descriptif: List<Descriptif>

    companion object {
        fun newInstance() = DetailEndConstatFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).mNavigationFragment = this
    }

    override fun goNext() {
        Toast.makeText(activity, "vers signature", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_detail_end_constat,
                container,
                false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("detailId").let { detailId ->
            if (!detailId.isNullOrEmpty()) {
                viewModel.getDetailById(detailId).observe(viewLifecycleOwner, {
                    it?.let {
                        binding.detail = it
                        detail = it
                    }
                })

                viewModel.getAllAlterations.observe(viewLifecycleOwner, {
                    it.let { list ->
                        cg_alterations.removeAllViews()
                        list.forEach { item ->
                            createChipsInCG(item.label, cg_alterations)
                        }
                    }
                })

                viewModel.getAllEtats.observe(viewLifecycleOwner, {
                    it.let { list: List<Etat> ->
                        cg_etat.removeAllViews()
                        list.forEach { item ->
                            createChipsInCG(item.label, cg_etat)
                        }
                    }
                })

                viewModel.getAllDescriptifs.observe(viewLifecycleOwner, {
                    it.let { list: List<Descriptif> ->
                        cg_descriptif.removeAllViews()
                        list.forEach { item ->
                            createChipsInCG(item.label, cg_descriptif)
                        }
                    }
                })

                viewModel.getAllPropretes.observe(viewLifecycleOwner, {
                    it.let { list: List<Proprete> ->
                        cg_proprete.removeAllViews()
                        list.forEach { item ->
                            createChipsInCG(item.label, cg_proprete)
                        }
                    }
                })
            }
        }
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }

    private fun createChipsInCG(text: String, cg: ChipGroup) {
        val chip: Chip = layoutInflater.inflate(R.layout.chip_only, cg) as Chip
        chip.text = text
        chip.setOnClickListener { view ->

        }

        cg.addView(chip)
    }

    @SuppressLint("CheckResult")
    fun onAddClicked(view: View, id: Int) {
        arguments?.getInt("idLot").let { idLot ->
            if (idLot != null) {
                MaterialDialog(requireContext()).show {
                    title(R.string.add_element_label)
                    input(allowEmpty = false) { _, text ->
                        launch {
                            when (id) {
                                1 -> viewModel.saveEtat(
                                    Etat(
                                        UUID.randomUUID().toString(),
                                        text.toString(),
                                        idLot,
                                        detail.idDetail
                                    )
                                )
                                2 -> viewModel.saveProprete(
                                    Proprete(
                                        UUID.randomUUID().toString(),
                                        text.toString(),
                                        idLot,
                                        detail.idDetail
                                    )
                                )
                                3 -> viewModel.saveDescriptif(
                                    Descriptif(
                                        UUID.randomUUID().toString(),
                                        text.toString(),
                                        idLot,
                                        detail.idDetail
                                    )
                                )
                                4 -> viewModel.saveAlteration(
                                    Alteration(
                                        UUID.randomUUID().toString(),
                                        text.toString(),
                                        idLot,
                                        detail.idDetail
                                    )
                                )
                            }
                        }
                    }
                    positiveButton(R.string.done)
                }
            }
        }
    }
}