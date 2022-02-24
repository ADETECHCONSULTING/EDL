package fr.atraore.edl.ui.edl.constat.second_page.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.databinding.FragmentDetailEndConstatBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.IdDetailStatesEnum
import fr.atraore.edl.utils.SUITE_CONSTAT_LABEL
import fr.atraore.edl.utils.whenAllNotNull
import kotlinx.android.synthetic.main.fragment_detail_end_constat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class DetailEndConstatFragment : BaseFragment(SUITE_CONSTAT_LABEL),
    MainActivity.OnNavigationFragment, CoroutineScope {

    private val TAG = DetailEndConstatFragment::class.simpleName

    override val title: String
        get() = SUITE_CONSTAT_LABEL

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var binding: FragmentDetailEndConstatBinding

    private val viewModel: DetailEndConstatViewModel by viewModels()
    private lateinit var detail: Detail
    private lateinit var alterationRefs: List<Alteration>
    private lateinit var etatsRefs: List<Etat>
    private lateinit var propreteRefs: List<Proprete>
    private lateinit var descriptifRefs: List<Descriptif>
    private lateinit var currentEtat: String

    companion object {
        fun newInstance() = DetailEndConstatFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).mNavigationFragment = this
    }

    override fun goNext() {

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
                viewModel.getDetailById(detailId).observe(viewLifecycleOwner) {
                    it?.let {
                        binding.detail = it
                        detail = it

                        if (!this::currentEtat.isInitialized) {
                            chipCheckedState(
                                cg_etat,
                                IdDetailStatesEnum.ETAT.value
                            )
                        }

                        if (!this::descriptifRefs.isInitialized) {
                            viewModel.getAllDescriptifs.observe(viewLifecycleOwner) { listRefs ->
                                listRefs.let { list: List<Descriptif> ->
                                    descriptifRefs = list
                                    cg_descriptif.removeAllViews()
                                    list.forEach { item ->
                                        createChipsInCG(
                                            item.label,
                                            cg_descriptif,
                                            IdDetailStatesEnum.DESCRIPTIF.value
                                        )
                                    }
                                    chipCheckedState(
                                        cg_descriptif,
                                        IdDetailStatesEnum.DESCRIPTIF.value
                                    )
                                }
                            }
                        }

                        if (!this::propreteRefs.isInitialized) {
                            viewModel.getAllPropretes.observe(viewLifecycleOwner) { listRefs ->
                                listRefs.let { list: List<Proprete> ->
                                    propreteRefs = list
                                    cg_proprete.removeAllViews()
                                    list.forEach { item ->
                                        createChipsInCG(
                                            item.label,
                                            cg_proprete,
                                            IdDetailStatesEnum.PROPRETE.value
                                        )
                                    }
                                    chipCheckedState(cg_proprete, IdDetailStatesEnum.PROPRETE.value)
                                }
                            }
                        }

                        if (!this::alterationRefs.isInitialized) {
                            viewModel.getAllAlterations.observe(viewLifecycleOwner) { listRefs ->
                                listRefs.let { list: List<Alteration> ->
                                    alterationRefs = list
                                    cg_alterations.removeAllViews()
                                    list.forEach { item ->
                                        createChipsInCG(
                                            item.label,
                                            cg_alterations,
                                            IdDetailStatesEnum.ALTERATION.value
                                        )
                                    }
                                    chipCheckedState(
                                        cg_alterations,
                                        IdDetailStatesEnum.ALTERATION.value
                                    )
                                }
                            }
                        }
                    }
                }
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

    fun chipEtatClicked(view: View) {
        (view as Chip).let {
            launch {
                Log.d(TAG, "chipEtatClicked: ${it.text}")
                arguments?.getString("detailId")?.let { idDetail ->
                    viewModel.updateEtat(
                        it.text.toString(),
                        idDetail
                    )
                }
            }
        }
    }

    fun chipPropreteClicked(view: View) {
        (view as Chip).let {
            launch {
                Log.d(TAG, "chipPropreteClicked: ${it.text}")
                arguments?.getString("detailId")?.let { idDetail ->
                    viewModel.updateProprete(
                        it.text.toString(),
                        idDetail
                    )
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun createChipsInCG(text: String, cg: ChipGroup, idOtherState: Int) {
        val chip: Chip = layoutInflater.inflate(R.layout.chip_only, null) as Chip
        chip.text = text
        chip.setOnClickListener { view ->
            chipOtherStatesClickListener(view, idOtherState)
        }

        cg.addView(chip)
    }

    /**
     * Gère la pre selection des chips
     */
    private fun chipCheckedState(viewGroup: ViewGroup, id: Int) {
        val views = getAllChildrenInsideViewGroup(viewGroup)
        if (!views.isNullOrEmpty()) {
            views.forEach { view ->
                if (view is Chip) {
                    when (id) {
                        IdDetailStatesEnum.ETAT.value -> {
                            if (view.text == detail.etat) {
                                view.isChecked = true
                                currentEtat = detail.etat.toString()
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun chipOtherStatesClickListener(view: View, id: Int) {
        if (view is Chip) {
            when (id) {
                IdDetailStatesEnum.DESCRIPTIF.value -> {
                    val idDescriptif = this.descriptifRefs.find { stt -> stt.label == view.text }
                    idDescriptif?.let {
                        //séparation par virgules
                        if (detail.descriptif.isNullOrEmpty()) {
                            detail.descriptif = it.label
                        } else {
                            detail.descriptif += ", ${it.label}"
                        }
                        launch {
                            Log.d(TAG, "set du descriptif dans le detail : ${it.id}")
                            viewModel.saveDetail(detail)
                        }
                    }
                }
                IdDetailStatesEnum.ALTERATION.value -> {
                    val dialog = Dialog(requireContext())
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(true)
                    dialog.setContentView(R.layout.dialog_color_picker)

                    val btnDone = dialog.findViewById(R.id.btn_save) as Button
                    val rdgLevel = dialog.findViewById(R.id.rdg_level_alteration) as RadioGroup
                    val rdgVerif = dialog.findViewById(R.id.rdg_verif_alteration) as RadioGroup

                    btnDone.setOnClickListener {
                        var rdbLevel: RadioButton? = null
                        var rdbVerif: RadioButton? = null

                        if (rdgLevel.checkedRadioButtonId > 0) {
                            rdbLevel = dialog.findViewById(rdgLevel.checkedRadioButtonId)
                        }

                        if (rdgVerif.checkedRadioButtonId > 0) {
                            rdbVerif = dialog.findViewById(rdgVerif.checkedRadioButtonId)
                        }

                        val foundAlteration =
                            alterationRefs.find { stt -> stt.label == view.text }
                        //Si alteration trouvée dans la liste
                        foundAlteration?.let { alteration ->
                            if (!this.detail.alteration!!.contains(alteration.label)) {
                                var alterationLevelVerif = alteration.label
                                var levelVerifPassed = false
                                whenAllNotNull(rdbLevel, rdbVerif) {
                                    levelVerifPassed = true
                                    alterationLevelVerif += " (${rdbLevel?.text} - ${rdbVerif?.text})"
                                }

                                if (!levelVerifPassed) {
                                    rdbLevel?.let {
                                        alterationLevelVerif += " (${rdbLevel.text})"
                                    }
                                    rdbVerif?.let {
                                        alterationLevelVerif += " (${rdbVerif.text})"
                                    }
                                }

                                //séparation par virgules
                                if (detail.alteration.isNullOrEmpty()) {
                                    detail.alteration = alterationLevelVerif
                                } else {
                                    detail.alteration += ", ${alterationLevelVerif}"
                                }

                                launch {
                                    Log.d(
                                        TAG,
                                        "set de l'alteration dans le detail : ${alteration.id}"
                                    )
                                    viewModel.saveDetail(detail)
                                }
                            }
                        }
                        dialog.dismiss()
                    }
                    dialog.show()
                    //control width
                    val lp = WindowManager.LayoutParams()
                    dialog.window?.let {
                        lp.copyFrom(it.attributes)
                        lp.width = 1000
                        lp.height = 700
                        it.attributes = lp
                    }
                }
            }
        }
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
                                IdDetailStatesEnum.PROPRETE.value -> viewModel.saveProprete(
                                    Proprete(
                                        UUID.randomUUID().toString(),
                                        text.toString(),
                                        idLot,
                                        detail.idDetail
                                    )
                                )
                                IdDetailStatesEnum.DESCRIPTIF.value -> viewModel.saveDescriptif(
                                    Descriptif(
                                        UUID.randomUUID().toString(),
                                        text.toString(),
                                        idLot,
                                        detail.idDetail
                                    )
                                )
                                IdDetailStatesEnum.ALTERATION.value -> viewModel.saveAlteration(
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

    private fun getAllChildrenInsideViewGroup(viewGroup: ViewGroup): MutableList<View> {
        val list = mutableListOf<View>()
        val childCount = viewGroup.childCount
        for (childIndex in 0..childCount) {
            val view = viewGroup.getChildAt(childIndex)
            view?.let {
                list.add(it)
            }
        }
        return Collections.unmodifiableList(list)
    }

    fun razDetail() {
        MaterialDialog(requireContext()).show {
            title(R.string.raz_title)
            message(R.string.raz_message)
            positiveButton(R.string.done) { _ ->
                run {
                    this@DetailEndConstatFragment.detail.razDetail()
                    launch {
                        Log.d(
                            TAG,
                            "RAZ du détail : ${this@DetailEndConstatFragment.detail.idDetail}"
                        )
                        viewModel.saveDetail(detail)
                    }
                }
            }
        }
    }
}