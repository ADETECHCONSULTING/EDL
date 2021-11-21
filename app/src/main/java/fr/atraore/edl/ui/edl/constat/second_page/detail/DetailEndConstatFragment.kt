package fr.atraore.edl.ui.edl.constat.second_page.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.MainActivity
import fr.atraore.edl.R
import fr.atraore.edl.data.models.Detail
import fr.atraore.edl.databinding.FragmentDetailEndConstatBinding
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.SUITE_CONSTAT_LABEL


@AndroidEntryPoint
class DetailEndConstatFragment : BaseFragment(SUITE_CONSTAT_LABEL), MainActivity.OnNavigationFragment {

    override val title: String
        get() = SUITE_CONSTAT_LABEL

    private lateinit var binding: FragmentDetailEndConstatBinding

    private val viewModel: DetailEndConstatViewModel by viewModels()

    companion object {
        fun newInstance() = DetailEndConstatFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).mNavigationFragment = this
    }

    override fun goNext() {
        Toast.makeText(activity, "vers signature", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_end_constat, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("detailId").let { detailId ->
            if (!detailId.isNullOrEmpty()) {
                viewModel.getDetailById(detailId).observe(viewLifecycleOwner, {
                    it?.let {
                        binding.detail = it
                    }
                })
            }
        }
    }

    override fun navigateFragment(actionNext: Boolean) {
        if (actionNext) {
            goNext()
        } else {
            goBack()
        }
    }

    private fun createChipsInCG(text: String, cg: ChipGroup) {
        val chip = Chip(requireContext())
        chip.text = text
        chip.setChipBackgroundColorResource(R.color.white)
        chip.setTextColor(resources.getColor(R.color.black))

        cg.addView(chip)
    }


}