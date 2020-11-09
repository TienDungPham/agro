package com.dp.agro.ui.scanresult

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dp.agro.R
import com.dp.agro.databinding.FragmentScanResultBinding
import com.dp.agro.utils.getOutputDirectory
import com.dp.agro.utils.nonFallbackTryCatch
import com.dp.agro.utils.saveScreenshotFromView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

@AndroidEntryPoint
class ScanResultFragment : Fragment() {
    private lateinit var binding: FragmentScanResultBinding
    private val args: ScanResultFragmentArgs by navArgs()
    private val viewModel: ScanResultViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanResultBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.findDiseaseBySlug(args.diseaseSlug)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.includeScanResult.apply {
                progressContainer.visibility = if (it) View.VISIBLE
                else View.INVISIBLE
            }
        }

        viewModel.disease.observe(viewLifecycleOwner) {
            binding.includeScanResult.apply {
                disease = it
                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(5))
                    .placeholder(R.drawable.icon_error_rounded)
                    .into(imgDisease)
                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .transform(CenterCrop())
                    .placeholder(R.drawable.icon_error_rounded)
                    .into(binding.imgDisease)
            }
        }

        binding.includeScanResult.apply {
            btnSearch.setOnClickListener {
                googleSearch(textDiseaseName.text.toString())
            }
            btnCopy.setOnClickListener { copyToClipBoard(textDiseaseName.text.toString()) }
            btnShare.setOnClickListener { shareScreenshot() }
        }
        return binding.root
    }

    private fun shareScreenshot() {
        nonFallbackTryCatch {
            val shareFile = File(
                requireActivity().getOutputDirectory(), UUID.randomUUID().toString().plus(
                    ".png"
                )
            )
            val shareFileSaved =
                saveScreenshotFromView(binding.motionBase, shareFile) ?: return@nonFallbackTryCatch

            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName.toString() + ".provider",
                shareFileSaved
            )

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "image/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, photoURI)
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
    }

    private fun copyToClipBoard(message: String) {
        nonFallbackTryCatch {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Label", message)
            clipboard.setPrimaryClip(clip)
            Snackbar.make(binding.root, "Copied to clipboard", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun googleSearch(query: String) {
        val searchUrl = "https://google.com/search?q=$query"
        val searchIntent = Intent(Intent.ACTION_VIEW);
        searchIntent.data = Uri.parse(searchUrl);
        startActivity(Intent.createChooser(searchIntent, "Where to search?"))
    }
}