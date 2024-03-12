package com.example.presentation.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.presentation.databinding.BottomFragmentDeletePairBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteBottomSheetDialogFragment() : BottomSheetDialogFragment() {

    private var _binding: BottomFragmentDeletePairBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels(ownerProducer = { requireActivity() })

    companion object {
        fun newInstance(from: String, to: String): DeleteBottomSheetDialogFragment {
            val instance = DeleteBottomSheetDialogFragment()
            instance.arguments = Bundle().apply {
                putString(FROM_KEY, from)
                putString(TO_KEY, to)
            }
            return instance
        }

        private const val FROM_KEY = "fromKey"
        private const val TO_KEY = "toKey"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomFragmentDeletePairBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(requireActivity(), theme) {}.apply {
            window!!.setDimAmount(0.5f)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val from = requireArguments().getString(FROM_KEY)!!
        val to = requireArguments().getString(TO_KEY)!!

        binding.yesButton.setOnClickListener {
            viewModel.removePair(from, to)
            dismiss()
        }

        binding.noButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}