package fr.atraore.edl.ui.edl.add.property

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Property
import fr.atraore.edl.ui.edl.BaseFragment
import kotlinx.android.synthetic.main.add_property_fragment.*
import kotlinx.android.synthetic.main.add_property_fragment.btn_cancel
import kotlinx.android.synthetic.main.add_property_fragment.btn_create
import kotlinx.android.synthetic.main.add_property_fragment.edt_address
import kotlinx.android.synthetic.main.add_property_fragment.edt_address2
import kotlinx.android.synthetic.main.add_property_fragment.edt_city
import kotlinx.android.synthetic.main.add_property_fragment.edt_postal_code
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddPropertyFragment : BaseFragment<Property>(), View.OnClickListener, CoroutineScope {
    private val TAG = AddPropertyFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = "Biens"

    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_property_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
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


        val property = Property(
            UUID.randomUUID().toString(),
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