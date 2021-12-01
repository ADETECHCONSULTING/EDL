package fr.atraore.edl.ui.edl.constat.second_page

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.databinding.CompteurFragmentBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.ui.edl.constat.ConstatViewModel
import fr.atraore.edl.ui.edl.constat.first_page.StartConstatFragment
import fr.atraore.edl.utils.ARGS_CONSTAT_ID
import fr.atraore.edl.utils.assistedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class CompteurFragment : BaseFragment("Compteur"), View.OnClickListener, LifecycleObserver,
    MainActivity.OnNavigationFragment, CoroutineScope {

    private val TAG = StartConstatFragment::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override val title: String
        get() = "Compteur"

    override fun goNext() {
        Log.d(TAG, "goNext: declenché")
        findNavController().popBackStack()
    }

    companion object {
        fun newInstance() = CompteurFragment()
    }

    private lateinit var binding: CompteurFragmentBinding

    @Inject
    lateinit var constatViewModelFactory: ConstatViewModel.AssistedStartFactory
    private val viewModel: ConstatViewModel by assistedViewModel {
        constatViewModelFactory.create(arguments?.getString(ARGS_CONSTAT_ID)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.compteur_fragment, container, false)
        binding.constatViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }

}