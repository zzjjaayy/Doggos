package com.jay.doggos.fragments.breedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jay.doggos.databinding.FragmentBreedListBinding
import com.jay.doggos.network.NetworkViewModel
import com.jay.doggos.network.StatusEnum

class BreedListFragment : Fragment() {

    companion object {
        const val LOG_TAG = "jayischecking"
    }

    private var _binding : FragmentBreedListBinding? = null
    private val binding get() = _binding!!

    private val networkViewModel : NetworkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BreedListAdapter{
            val action = BreedListFragmentDirections
                .actionBreedListFragmentToBreedDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.breedListRecyclerView.adapter = adapter
        networkViewModel.allBreeds.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        networkViewModel.status.observe(viewLifecycleOwner, {
            handleIndicators(it)
        })
    }

    private fun handleIndicators(status : StatusEnum) {
        when(status) {
            StatusEnum.DONE -> {
                binding.apply {
                    loadingIcon.visibility = View.GONE
                    errorImage.visibility = View.GONE
                    errorText.visibility = View.GONE
                    breedListRecyclerView.visibility = View.VISIBLE
                }
            }
            StatusEnum.ERROR -> {
                binding.apply {
                    loadingIcon.visibility = View.GONE
                    errorImage.visibility = View.VISIBLE
                    errorText.visibility = View.VISIBLE
                    breedListRecyclerView.visibility = View.GONE
                }
            }
            StatusEnum.LOADING -> {
                binding.apply {
                    loadingIcon.visibility = View.VISIBLE
                    errorImage.visibility = View.GONE
                    errorText.visibility = View.GONE
                    breedListRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}