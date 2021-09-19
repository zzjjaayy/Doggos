package com.jay.doggos.fragments.breedDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.jay.doggos.fragments.SharedViewModel
import com.jay.doggos.databinding.FragmentBreedDetailsBinding
import com.jay.doggos.network.NetworkViewModel
import com.jay.doggos.network.StatusEnum

class BreedDetailsFragment : Fragment() {

    private var _binding : FragmentBreedDetailsBinding? = null
    private val binding get() = _binding!!

    private val networkViewModel : NetworkViewModel by activityViewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private val args: BreedDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBreedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shuffle.visibility = View.GONE
        val currentBreed : String = args.breedName
        networkViewModel.getCurrentBreed(currentBreed)
        val adapter = BreedDetailsAdapter{
            sharedViewModel.downloadImage(requireContext(), it, currentBreed)
        }
        binding.currentBreedRecyclerView.adapter = adapter
        networkViewModel.currentBreed.observe(viewLifecycleOwner, {
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
                    currentBreedRecyclerView.visibility = View.VISIBLE
                }
            }
            StatusEnum.ERROR -> {
                binding.apply {
                    loadingIcon.visibility = View.GONE
                    errorImage.visibility = View.VISIBLE
                    errorText.visibility = View.VISIBLE
                    currentBreedRecyclerView.visibility = View.GONE
                }
            }
            StatusEnum.LOADING -> {
                binding.apply {
                    loadingIcon.visibility = View.VISIBLE
                    errorImage.visibility = View.GONE
                    errorText.visibility = View.GONE
                    currentBreedRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        networkViewModel.clearCurrentBreed()
        _binding = null
    }
}