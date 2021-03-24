package fr.atraore.edl.ui.adapter.start

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.data.models.PrimaryInfo

class PrimaryInfoNoDataBindAdapter(private val dataSet: List<PrimaryInfo>) : RecyclerView.Adapter<PrimaryInfoNoDataBindAdapter.ViewHolder>() {
    private val TAG: String? = PrimaryInfoNoDataBindAdapter::class.simpleName

    var edit: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.primary_info_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPrimary = dataSet[position]
        holder.apply {
            edtItem.setText(itemPrimary.primaryInfo())
            edtItem.isEnabled = itemPrimary.enableInfo
        }
    }

    private fun editClickListener(tenant: PrimaryInfo): View.OnClickListener {
        //TODO update
        return View.OnClickListener {
            Log.d(TAG, "editClickListener: CLICKED")
        }
    }

    fun saveContent(baseEntities: PrimaryInfo) {
        Log.d(TAG, "sauvegarde de l'entity")

    }

    fun editUpdate() {
        dataSet.forEach { primaryInfo -> primaryInfo.enableInfo = !primaryInfo.enableInfo }
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val edtItem: EditText = view.findViewById(R.id.edt_item)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}