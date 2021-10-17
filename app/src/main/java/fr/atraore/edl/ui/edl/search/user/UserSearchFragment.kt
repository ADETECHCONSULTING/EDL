package fr.atraore.edl.ui.edl.search.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.EdlApplication
import fr.atraore.edl.R
import fr.atraore.edl.data.models.ConstatWithDetails
import fr.atraore.edl.data.models.Users
import fr.atraore.edl.ui.adapter.UserAdapter
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.USER_LABEL
import kotlinx.android.synthetic.main.user_search_fragment.*

@AndroidEntryPoint
class UserSearchFragment(private val constat: ConstatWithDetails) : BaseFragment(USER_LABEL) {

    override val title: String
        get() = USER_LABEL

    companion object {
        fun newInstance(constat: ConstatWithDetails) = UserSearchFragment(constat)
    }

    override fun goNext() {
        TODO("Not yet implemented")
    }

    private val userSearchViewModel: UserSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserAdapter(userSearchViewModel, constat)
        rcv_users.adapter = adapter
        rcv_users.layoutManager = GridLayoutManager(context, 4)

        userSearchViewModel.allUsers.observe(viewLifecycleOwner, { users ->
            users?.let { adapter.submitList(it) }
        })
    }

}