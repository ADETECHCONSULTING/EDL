package fr.atraore.edl.ui.adapter.start

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.*
import fr.atraore.edl.ui.edl.start.StartConstatViewModel

class PrimaryInfoNoDataBindAdapter(private val dataSet: List<PrimaryInfo>, private val startConstatViewModel: StartConstatViewModel) : RecyclerView.Adapter<PrimaryInfoNoDataBindAdapter.ViewHolder>() {
    private val TAG: String? = PrimaryInfoNoDataBindAdapter::class.simpleName

    var edit: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.primary_info_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPrimary = dataSet[position]
        holder.apply {
            txvCivi.text = itemPrimary.civiInfo()
            edtItem.setText(itemPrimary.primaryInfo())
            edtItem.isEnabled = itemPrimary.enableInfo

            edtItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //none
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //none
                }

                override fun afterTextChanged(s: Editable?) {
                    when (dataSet.first()) {
                        is Tenant -> {
                            (itemPrimary as Tenant).name = s.toString()
                        }
                        is Owner -> {

                        }
                        is Contractor -> {

                        }
                        is Property -> {

                        }
                    }
                }
            })
        }
    }

    private fun editClickListener(tenant: PrimaryInfo): View.OnClickListener {
        //TODO update
        return View.OnClickListener {
            Log.d(TAG, "editClickListener: CLICKED")
        }
    }

    fun saveContent() {
        Log.d(TAG, "sauvegarde de l'entity")
        //type erasure m'empeche de checker toute la liste donc je check que la premire valeur
        when (dataSet.first()) {
            is Tenant -> {
                //checked cast juste avant sur la premiere valeur
                startConstatViewModel.saveTenants(dataSet as List<Tenant>)
            }
            is Owner -> {

            }
            is Contractor -> {

            }
            is Property -> {

            }
        }
    }

    fun editUpdate() {
        dataSet.forEach { primaryInfo -> primaryInfo.enableInfo = !primaryInfo.enableInfo }
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val edtItem: EditText = view.findViewById(R.id.edt_item)
        val txvCivi: TextView = view.findViewById(R.id.txv_civi)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}