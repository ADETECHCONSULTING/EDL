package fr.atraore.edl.ui.edl.add.owner

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
import fr.atraore.edl.data.models.entity.Owner
import fr.atraore.edl.databinding.FragmentAddOwnerBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.add.AddViewModel
import fr.atraore.edl.utils.OWNER_LABEL
import kotlinx.android.synthetic.main.fragment_add_owner.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddOwnerFragment(val idArgs: String?) : BaseFragment(OWNER_LABEL), View.OnClickListener, CoroutineScope {
    private val TAG = AddOwnerFragment::class.simpleName
    private val addViewModel: AddViewModel by viewModels()
    private lateinit var binding: FragmentAddOwnerBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override val title: String
        get() = OWNER_LABEL

    companion object {
        fun newInstance(idArgs: String?) = AddOwnerFragment(idArgs)
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_owner, container, false)
        binding.viewModel = addViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idArgs?.let { itemId ->
            addViewModel.getOwnerById(itemId).observe(viewLifecycleOwner) {
                it?.let {
                    addViewModel.owner.value = it
                }
                initListeners()
            }
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
        addViewModel.owner.value?.let {
            id = it.ownerId
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

        val owner = Owner(
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
            note
        )

        launch(Dispatchers.Main) {
            save(owner)
            Log.d(TAG, "création d'un propriétaire ${owner}")
        }

        findNavController().popBackStack()
    }
}