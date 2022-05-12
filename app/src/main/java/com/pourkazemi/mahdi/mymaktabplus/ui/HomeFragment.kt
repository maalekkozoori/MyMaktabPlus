package com.pourkazemi.mahdi.mymaktabplus.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.pourkazemi.mahdi.mymaktabplus.R
import com.pourkazemi.mahdi.mymaktabplus.data.remotedata.model.PictureItem
import com.pourkazemi.mahdi.mymaktabplus.databinding.FragmentHomeBinding
import com.pourkazemi.mahdi.mymaktabplus.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()


    @Inject
    lateinit var adapter: MyPagingDataAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.recycleView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.myPager.collect{
                        adapter.submitData(it)

                    }
                }
                launch {
                    adapter.loadStateFlow.collect{
                        when(it.refresh){
                            is LoadState.Loading -> {
                                Log.d("TAG", "onViewCreated: Loading")
                                binding.stateView.onLoading()
                            }
                            is LoadState.Error -> {
                                binding.stateView.onFail()
                                binding.stateView.clickRequest {
                                    adapter.retry()
                                }

                            }
                            is LoadState.NotLoading -> {
                                Log.d("TAG", "onViewCreated: NotLoading")
                                binding.stateView.onSuccess()
                            }
                        }
                    }
                }
            }

        }



/*
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureFlow.collect {
                    when (it) {
                        is ResultWrapper.Loading -> binding.stateView.onLoading()
                        is ResultWrapper.Success -> {
                            //val emptyList= listOf<PictureItem>() //#for check empty list worked
                            adapter.submitList(it.value)
                            if (it.value.isNotEmpty()) {
                                binding.stateView.onSuccess()
                            } else {
                                binding.stateView.onEmpty()
                            }
                        }
                        is ResultWrapper.Error -> {
                            binding.stateView.onFail()
                            binding.stateView.clickRequest {
                                viewModel.getPictureList()
                            }
                            Toast.makeText(
                                requireActivity(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }*/


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}