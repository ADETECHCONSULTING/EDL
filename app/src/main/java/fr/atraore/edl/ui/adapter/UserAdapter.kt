package fr.atraore.edl.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Users
import fr.atraore.edl.databinding.UserItemBinding
import fr.atraore.edl.ui.edl.search.user.UserSearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserAdapter(private val userSearchViewModel: UserSearchViewModel, private val constatDetails: ConstatWithDetails) : ListAdapter<Users, UserAdapter.ViewHolder>(DiffUserCallBack()), CoroutineScope {
    private val TAG = UserAdapter::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.apply {
            bind(createClickListener(user), user)
            itemView.tag = user
        }
    }

    private fun createClickListener(user: Users): View.OnClickListener {
        return View.OnClickListener {
            launch {
                if (constatDetails.user != null) {
                    userSearchViewModel.updateExistingUser(constatDetails.constat, user)
                    Log.d(TAG, "Update user ${user} in ${constatDetails.constat.constatId}")
                } else {
                    userSearchViewModel.save(constatDetails.constat, user)
                    Log.d(TAG, "Ajout user ${user} in ${constatDetails.constat.constatId}")
                }
            }
        }
    }


    class ViewHolder(
        private val binding: UserItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listenerProperty: View.OnClickListener,
            item: Users
        ) {
            binding.apply {
                userItem = item
                addClickListener = listenerProperty
            }
        }
    }
}

private class DiffUserCallBack : DiffUtil.ItemCallback<Users>() {

    override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem == newItem
    }


}