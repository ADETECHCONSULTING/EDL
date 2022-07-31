package fr.atraore.edl.ui.edl.constat.second_page.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.*
import fr.atraore.edl.databinding.FragmentDetailEndConstatBinding
import fr.atraore.edl.generated.callback.OnClickListener
import fr.atraore.edl.photo.PhotoPickerFragment
import fr.atraore.edl.ui.adapter.PhotoGridAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.second_page.CompteurFragment
import fr.atraore.edl.utils.*
import fr.atraore.edl.utils.InsertMedia.Companion.savePhotoToInternalStorage
import kotlinx.android.synthetic.main.fragment_detail_end_constat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class DetailEndConstatFragment : BaseFragment(SUITE_CONSTAT_LABEL),
    MainActivity.OnNavigationFragment, CoroutineScope, View.OnClickListener, PhotoPickerFragment.Callback {

    private val TAG = DetailEndConstatFragment::class.simpleName

    override val title: String
        get() = SUITE_CONSTAT_LABEL

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private lateinit var binding: FragmentDetailEndConstatBinding

    private val viewModel: DetailEndConstatViewModel by viewModels()
    private lateinit var detail: Detail
    private lateinit var alterationRefs: List<Alteration>
    private lateinit var etatsRefs: List<Etat>
    private lateinit var descriptifRefs: List<Descriptif>
    private lateinit var currentEtat: String
    private lateinit var currentProprete: String
    private lateinit var currentImageView: ImageView
    private lateinit var photoGridAdapter: PhotoGridAdapter

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
        binding.photoClickListener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoGridAdapter = PhotoGridAdapter(requireActivity(), viewModel)
        rcv_photos.layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)

        arguments?.getString("detailId").let { detailId ->
            if (!detailId.isNullOrEmpty()) {
                viewModel.getDetailById(detailId).observe(viewLifecycleOwner) {
                    it?.let {
                        binding.detail = it
                        detail = it
                        viewModel.currentDetail = it

                        tgb_fonctionnement.isChecked = it.fonctionmt == "Oui"

                        if (!this::currentEtat.isInitialized) {
                            chipCheckedState(
                                cg_etat,
                                IdDetailStatesEnum.ETAT.value
                            )
                        }

                        if (!this::currentProprete.isInitialized) {
                            chipCheckedState(
                                cg_proprete,
                                IdDetailStatesEnum.PROPRETE.value
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

                        edt_intitule.setOnFocusChangeListener { view, focus ->
                            if (!focus) {
                                launch {
                                    Log.d(TAG, "onViewCreated: sauvegarde du détail $detail")
                                    detail.intitule = (view as EditText).text.toString()
                                    viewModel.saveDetail(detail)
                                }
                            }
                        }

                        edt_note.setOnFocusChangeListener { view, focus ->
                            if (!focus) {
                                launch {
                                    Log.d(TAG, "onViewCreated: sauvegarde du détail $detail")
                                    detail.notes = (view as EditText).text.toString()
                                    viewModel.saveDetail(detail)
                                }
                            }
                        }

                        //photos
                        detail.imagesPaths?.let { paths ->
                            photoGridAdapter.swapData(paths.split(","))
                            rcv_photos.adapter = photoGridAdapter
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
                        IdDetailStatesEnum.PROPRETE.value -> {
                            if (view.text == detail.proprete) {
                                view.isChecked = true
                                currentProprete = detail.proprete.toString()
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
                    val btnCancel = dialog.findViewById(R.id.btn_cancel) as Button
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
                    btnCancel.setOnClickListener {
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

    fun toggleFonctionmentChange(view: View, checked: Boolean) {
        launch {
            if (checked) {
                viewModel.updateFonctionnement("Oui", this@DetailEndConstatFragment.detail.idDetail)
            } else {
                viewModel.updateFonctionnement("Non", this@DetailEndConstatFragment.detail.idDetail)
            }
        }
    }

    //photo
    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 5,
            theme = R.style.ChiliPhotoPicker_Light
        ).show(childFragmentManager, "picker")
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        var paths = mutableListOf<String>()
        if (photos.isNotEmpty()) {
            for (imagePath in photos) {
                activity?.let {
                    val path = "PHO_DETAIL_${detail.idConstat}_${detail.idDetail}"
                    savePhotoToInternalStorage(requireActivity(), path, getBitmapFromUri(imagePath))
                    Log.d(TAG, "onImagesPicked: Image sauvegardée")
                    paths.add(path)
                }
            }
            launch {
                viewModel.updateImagesPaths(paths.joinToString(","), detail.idDetail)
            }
        }
    }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageUri))
        } else {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
        }
    }

    override fun onClick(p0: View?) {
        openPicker()
    }

}