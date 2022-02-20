package fr.atraore.edl.ui.edl.add.property

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.databinding.FragmentAddPropertyBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.add.AddViewModel
import fr.atraore.edl.utils.PROPERTY_LABEL
import kotlinx.android.synthetic.main.fragment_add_property.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddPropertyFragment(val idArgs: String?) : BaseFragment(PROPERTY_LABEL), View.OnClickListener, CoroutineScope {
    private val TAG = AddPropertyFragment::class.simpleName
    private val addViewModel: AddViewModel by viewModels()
    private lateinit var binding: FragmentAddPropertyBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = PROPERTY_LABEL

    companion object {
        fun newInstance(idArgs: String?) = AddPropertyFragment(idArgs)
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_property, container, false)
        binding.viewModel = addViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idArgs?.let { itemId ->
            addViewModel.getPropertyById(itemId).observe(viewLifecycleOwner, {
                it?.let {
                    addViewModel.property.value = it
                }
                initListeners()
            })
        }
    }

    private fun initListeners() {
        btn_create.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    private fun createClicked() {
        if (
            edt_nature.text.toString().trim().isEmpty()
            || edt_type.text.toString().trim().isEmpty()
            || edt_address.text.toString().trim().isEmpty()
            || edt_postal_code.text.toString().trim().isEmpty()
            || edt_city.text.toString().trim().isEmpty()
        ) {
            Toast.makeText(context, "Veuillez renseigner les champs obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        val nature = edt_nature.text.toString()
        val type = edt_type.text.toString()
        val address = edt_address.text.toString()
        val address2 = edt_address2.text.toString()
        val postalCode = edt_postal_code.text.toString()
        val city = edt_city.text.toString()
        val floor = edt_floor.text.toString()
        val nbLevel = edt_nb_level.text.toString()
        val appartmentDoor = edt_appartment_door.text.toString()
        val comment = edt_note.text.toString()

        var id = UUID.randomUUID().toString()
        addViewModel.property.value?.let {
            id = it.propertyId
        }

        val property = Property(
            id,
            address,
            address2,
            postalCode,
            city,
            comment,
            nature,
            type,
            nbLevel.toInt(),
            "", //deuxieme commentaire
            floor.toInt(),
            0, //escalier
            appartmentDoor.toInt(),
            0, //num cave
            0, //grenier ?
            0, //parking
            0, //box
        )

        launch {
            save(property)
            Log.d(TAG, "création d'un bien ${property}")
        }

        findNavController().popBackStack()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_create -> {
                createClicked()
            }
            R.id.btn_cancel -> {
                //retourne dans la stack précédente
                findNavController().popBackStack()
            }
        }
    }

}