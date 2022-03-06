package com.devtech.counting.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.devtech.counting.R
import com.devtech.counting.databinding.FragmentCountingBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CountingFragment : Fragment() {

    private var _binding: FragmentCountingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CountingViewModel>()
    private val textToSpeech = lazy { TextToSpeech(requireContext()) {} }

    private val horizontalAdapter by lazy { HorizontalItemsAdapter() }
    private val treeAdapter by lazy { TreeItemsAdapter { viewModel.onItemClicked(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountingBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setOnClickListeners()
        setObservers()
    }

    private fun setUpUI() {
        binding.horizontalRv.adapter = horizontalAdapter
        binding.treeRv.adapter = treeAdapter
    }

    private fun setOnClickListeners() {
        binding.restart.setOnClickListener {
            viewModel.reStart()
            textToSpeech.value.speak(
                getString(R.string.start_again),
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalItems.collectLatest {
                    treeAdapter.submitList(it)
                    val countedItems = it.filter { item -> item.isSelected }
                    horizontalAdapter.submitList(countedItems)
                    binding.countText.visibility = if (countedItems.isNotEmpty()) View.VISIBLE
                    else View.INVISIBLE
                    binding.countText.text = countedItems.size.toString()
                    if (binding.countText.visibility == View.VISIBLE) {
                        textToSpeech.value.speak(
                            binding.countText.text,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        if (textToSpeech.isInitialized()) textToSpeech.value.shutdown()
        super.onDestroyView()
    }

}