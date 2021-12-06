package fr.atraore.edl.ui.edl.add.tenant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Tenant
import fr.atraore.edl.databinding.AddTenantFragmentBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.add.AddViewModel
import fr.atraore.edl.utils.TENANT_LABEL
import kotlinx.android.synthetic.main.add_tenant_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class AddTenantFragment(val idArgs: String?) : BaseFragment(TENANT_LABEL), View.OnClickListener, CoroutineScope {
    private val TAG = AddTenantFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = TENANT_LABEL

    private lateinit var binding: AddTenantFragmentBinding

    companion object {
        fun newInstance(idArgs: String?) = AddTenantFragment(idArgs)
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    private val addViewModel: AddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.add_tenant_fragment, container, false)
        binding.viewModel = addViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idArgs?.let { itemId ->
            addViewModel.getTenantById(itemId).observe(viewLifecycleOwner, {
                it?.let {
                    addViewModel.tenant.value = it
                }
                initListeners()
            })
        }
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

        var id = UUID.randomUUID().toString()
        addViewModel.tenant.value?.let {
            id = it.tenantId
        }

        val name = edt_name.text.toString()
        val civi = spn_civilite.selectedItem.toString()
        val tel = edt_tel.text.toString()
        val tel2 = edt_tel2.text.toString()
        val address = edt_address.text.toString()
        val address2 = edt_address2.text.toString()
        val postalCode = edt_postal_code.text.toString()
        val city = edt_city.text.toString()
        val mail = edt_mail.text.toString()
        val note = edt_note.text.toString()

        val tenant = Tenant(
            id,
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
            Date(System.currentTimeMillis())
        )

        launch {
            save(tenant)
            Log.d(TAG, "création d'un locataire ${tenant}")
        }

        findNavController().popBackStack()
    }
}