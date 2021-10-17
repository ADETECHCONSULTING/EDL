package fr.atraore.edl.ui.edl.add.tenant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.TENANT_LABEL
import kotlinx.android.synthetic.main.add_tenant_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddTenantFragment : BaseFragment(TENANT_LABEL), View.OnClickListener, CoroutineScope {
    private val TAG = AddTenantFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = TENANT_LABEL

    companion object {
        fun newInstance() = AddTenantFragment()
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_tenant_fragment, container, false)
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
            edt_name.text.toString().trim().isEmpty()
            || edt_tel.text.toString().trim().isEmpty()
            || edt_address.text.toString().trim().isEmpty()
            || edt_postal_code.text.toString().trim().isEmpty()
            || edt_city.text.toString().trim().isEmpty()
        ) {
            Toast.makeText(context, "Veuillez renseigner les champs obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        val name = edt_name.text.toString()
        val civi = spn_civilite.selectedItem.toString()
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

        val tenant = Tenant(
            UUID.randomUUID().toString(),
            civi,
            name,
            address,
            address2,
            postalCode,
            city,
            tel,
            tel2,
            mail,
            note,
            null,
            null,
            null,
            null,
            Date(1607686070062)
        )

        launch {
            save(tenant)
            Log.d(TAG, "création d'un locataire ${tenant}")
        }

        findNavController().popBackStack()
    }
}