package com.example.frequency.presentation.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frequency.R
import com.example.frequency.databinding.FragmentPreviewBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator

class PreviewFragment : Fragment(), ProvidesCustomTitle {

    private var _binding: FragmentPreviewBinding? = null

    private val binding get() = _binding!!

    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageUri = arguments?.getString(ARG_IMAGE_URI)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.previewIv.setOnClickListener {
            navigator().openProfile()
        }

        Log.d(TAG, "uri value $imageUri")


        Glide.with(requireContext())
            .load(imageUri)
            .error(R.drawable.ic_unknown_user_photo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.previewIv)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(image: String) = PreviewFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_IMAGE_URI, image)
            }
        }

        @JvmStatic
        private val ARG_IMAGE_URI = "ARG_IMAGE_URI"

        @JvmStatic
        private val TAG = PreviewFragment::class.java.simpleName
    }

    override fun getTitleRes() = R.string.user_preview

}