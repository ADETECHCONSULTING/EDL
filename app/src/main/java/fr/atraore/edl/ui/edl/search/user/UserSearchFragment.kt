package fr.atraore.edl.ui.edl.search.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.search.BaseFragment

class UserSearchFragment : BaseFragment() {

    override val title: String
        get() = "Utilisateurs"

    companion object {
        fun newInstance() = UserSearchFragment()
    }

    private lateinit var viewModel: UserSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserSearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}