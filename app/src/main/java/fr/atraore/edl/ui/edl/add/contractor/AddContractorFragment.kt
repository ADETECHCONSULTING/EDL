package fr.atraore.edl.ui.edl.add.contractor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Contractor
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.CONTRACTOR_LABEL
import fr.atraore.edl.utils.OWNER_LABEL
import kotlinx.android.synthetic.main.add_contractor_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddContractorFragment : BaseFragment(CONTRACTOR_LABEL), View.OnClickListener, CoroutineScope {
    private val TAG = AddContractorFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = CONTRACTOR_LABEL

    companion object {
        fun newInstance() = AddContractorFragment()
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    private lateinit var viewModel: AddContractorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_contractor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        btn_create.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (
            edt_denomination.text.toString().trim().isEmpty()
            || edt_tel.text.toString().trim().isEmpty()
            || edt_address.text.toString().trim().isEmpty()
            || edt_postal_code.text.toString().trim().isEmpty()
            || edt_city.text.toString().trim().isEmpty()
        ) {
            Toast.makeText(context, "Veuillez renseigner les champs obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        val denomination = edt_denomination.text.toString()
        val tel = edt_tel.text.toString()
        val tel2 = edt_tel2.text.toString()
        val address = edt_address.text.toString()
        val address2 = edt_address2.text.toString()
        val postalCode = edt_postal_code.text.toString()
        val postalCode2 = edt_postal_code2.text.toString()
        val city = edt_city.text.toString()
        val city2 = edt_city2.text.toString()
        val mail = edt_mail.text.toString()
        val note = edt_note.text.toString()

        val contractor = Contractor(
            UUID.randomUUID().toString(),
            denomination,
            mail,
            address,
            address2,
            null,
            postalCode,
            postalCode2,
            null,
            city
        )

        launch {
            save(contractor)
            Log.d(TAG, "création d'un mandataire ${contractor}")
        }

        findNavController().popBackStack()
    }
}