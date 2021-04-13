package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.data.models.Owner
import fr.atraore.edl.databinding.OwnerItemBinding
import fr.atraore.edl.ui.edl.search.owner.OwnerSearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class OwnerAdapter(private val ownerSearchViewModel: OwnerSearchViewModel, private val constatDetail: ConstatWithDetails) : ListAdapter<Owner, OwnerAdapter.ViewHolder>(DiffOwnerCallback()), CoroutineScope {
    private val TAG = OwnerAdapter::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OwnerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val owner = getItem(position)
        //si proprio déjà dans la liste alors event de suppression sinon event d'ajout
        if (constatDetail.owners.find { it.ownerId == owner.ownerId } !== null) {
            holder.apply {
                bind(deleteOwnerClickListener(owner), owner, false)
                itemView.tag = owner
            }
        } else {
            holder.apply {
                bind(createOwnerClickListener(owner), owner, true)
                itemView.tag = owner
            }
        }
    }

    private fun createOwnerClickListener(owner: Owner): View.OnClickListener {
        return View.OnClickListener {
            launch {
                ownerSearchViewModel.saveConstatOwner(constatDetail.constat.constatId, owner.ownerId)
                Log.d(TAG, "Creation ConstatOwnerCrossRef ${owner} in ${constatDetail.constat.constatId}")
            }
        }
    }

    private fun deleteOwnerClickListener(owner: Owner): View.OnClickListener {
        return View.OnClickListener {
            launch {
                ownerSearchViewModel.deleteConstatOwner(constatDetail.constat.constatId, owner.ownerId)
                Log.d(TAG, "Suppression ConstatOwnerCrossRef ${owner} in ${constatDetail.constat.constatId}")
            }
        }
    }

    class ViewHolder(
        private val binding: OwnerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Owner,
            itemState: Boolean
        ) {
            binding.apply {
                ownerItem = item
                addClickListener = listenerProperty
                state = itemState
            }
        }
    }
}

private class DiffOwnerCallback : DiffUtil.ItemCallback<Owner>() {

    override fun areItemsTheSame(oldItem: Owner, newItem: Owner): Boolean {
        return oldItem.ownerId == newItem.ownerId
    }

    override fun areContentsTheSame(oldItem: Owner, newItem: Owner): Boolean {
        return oldItem == newItem
    }


}