package fr.atraore.edl.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import fr.atraore.edl.R
import fr.atraore.edl.data.models.entity.EquipmentReference
import fr.atraore.edl.utils.LOTS_LABELS
import fr.atraore.edl.utils.ROOMS_LABELS

class EquipmentReferenceAdapter(
    private var list: MutableList<EquipmentReference>,
    private val viewModel: ListViewModel
) : RecyclerView.Adapter<EquipmentReferenceAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun updateList(newList: List<EquipmentReference>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.edit_equipement_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipmentReference = list[position]

        holder.view.findViewById<EditText>(R.id.editTextLevel1).apply {
            setText(equipmentReference.level1)
            addTextChangedListener { equipmentReference.level1 = it.toString() }
        }

        holder.view.findViewById<EditText>(R.id.editTextLevel2).apply {
            setText(equipmentReference.level2)
            addTextChangedListener { equipmentReference.level2 = it.toString() }
        }

        holder.view.findViewById<EditText>(R.id.editTextLevel3).apply {
            setText(equipmentReference.level3 ?: "")
            addTextChangedListener { equipmentReference.level3 = it.toString() }
        }

        holder.view.findViewById<TextView>(R.id.txvRoomRef).apply {
            text = ROOMS_LABELS[equipmentReference.idRoomRef ?: 0]
        }

        holder.view.findViewById<TextView>(R.id.txvLot).apply {
            text = LOTS_LABELS[equipmentReference.idLot ?: 0]
        }

        holder.view.findViewById<ImageButton>(R.id.editButton).setOnClickListener {
            MaterialDialog(it.context).show {
                title(text = "Modification de l'équipement")
                message(text = "Etes-vous sûr de vouloir modifier l'équipement suivant ${equipmentReference.level1} ${equipmentReference.level2} ${equipmentReference.level3 ?: ""} ?")
                positiveButton(text = "Oui") {
                    viewModel.updateEquipmentReference(equipmentReference)
                }
                negativeButton(text = "Non")
                lifecycleOwner(holder.view.context as LifecycleOwner)
            }
        }

        holder.view.findViewById<ImageButton>(R.id.deleteButton).setOnClickListener {
            MaterialDialog(it.context).show {
                title(text = "Suppression de l'équipement")
                message(text = "Etes-vous sûr de vouloir supprimer l'équipement suivant ${equipmentReference.level1} ${equipmentReference.level2} ${equipmentReference.level3 ?: ""} ?")
                positiveButton(text = "Oui") {
                    viewModel.deleteEquipmentRef(equipmentReference.id)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                }
                negativeButton(text = "Non")
                lifecycleOwner(holder.view.context as LifecycleOwner)
            }
        }
    }

    override fun getItemCount() = list.size
}