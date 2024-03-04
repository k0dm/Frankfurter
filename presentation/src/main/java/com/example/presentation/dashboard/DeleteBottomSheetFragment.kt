package com.example.presentation.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.core.ProvideViewModel
import com.example.presentation.databinding.BottomFragmentDeletePairBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteBottomSheetFragment() : BottomSheetDialogFragment() {

    private var _binding: BottomFragmentDeletePairBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel

    companion object {
        fun newInstance(from: String, to: String): DeleteBottomSheetFragment {
            val instance = DeleteBottomSheetFragment()
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
        viewModel =
            (requireActivity() as ProvideViewModel).viewModel(DashboardViewModel::class.java)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(requireActivity(), theme) {

            override fun onBackPressed() {
                super.onBackPressed()
                dismiss()
            }
        }.apply {
            window!!.setDimAmount(0.5f)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val from = requireArguments().getString(FROM_KEY)!!
        val to = requireArguments().getString(TO_KEY)!!

        (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.touch_outside)!!
            .setOnClickListener {
                dismiss()
            }

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