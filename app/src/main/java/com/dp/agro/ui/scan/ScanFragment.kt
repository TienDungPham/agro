package com.dp.agro.ui.scan

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dp.agro.R
import com.dp.agro.databinding.FragmentScanBinding
import com.dp.agro.utils.ClassifyMediator
import com.dp.agro.utils.getOutputDirectory
import com.dp.agro.utils.nonFallbackTryCatch
import com.dp.agro.utils.saveScreenshotFromView
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding
    private val viewModel: ScanViewModel by viewModels()

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private var isBackCamera = true

    private lateinit var classifyMediator: ClassifyMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        val localModel = LocalModel.Builder()
            .setAssetFilePath("plant_diseases/model.tflite")
            .build()
        val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
            .setConfidenceThreshold(0.5f)
            .setMaxResultCount(1)
            .build()
        val labeler = ImageLabeling.getClient(customImageLabelerOptions)

        classifyMediator = ClassifyMediator(labeler) {
            // TODO: Save scan result and change to dynamic result
            if (it == "unknown"
                || it == "soy_healthy"
                || it == "cabbage_healthy"
                || it == "tomato_healthy"
                || it == "maize_healthy"
            ) {
                val toast = Toast.makeText(
                    requireContext(),
                    "No disease found!", Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 0)
                toast.show()
                binding.imgSelected.visibility = View.INVISIBLE
                binding.imgSelected.setImageDrawable(null)
                startCamera(false)
            } else {
                startResultScreen()
                viewModel.findDiseaseBySlug(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        binding.toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.to_setting -> {
                        findNavController().navigate(R.id.setting_des)
                        true
                    }
                    else -> false
                }
            }
            setNavigationOnClickListener {
                findNavController().navigate(R.id.explore_des)
            }
        }

        binding.apply {
            btnCameraFlip.setOnClickListener {
                isBackCamera = !isBackCamera
                startCamera(false)
            }
            btnCameraScan.setOnClickListener {
                cameraProvider.unbindAll()
                classifyMediator.classify(binding.previewCamera.bitmap!!)
            }
            btnCameraSelect.setOnClickListener {
                val pickIntent = Intent(Intent.ACTION_GET_CONTENT)
                pickIntent.type = "image/*"
                startActivityForResult(pickIntent, SELECT_IMAGE_REQUEST_CODE)
            }
        }

        binding.includeScanResult.apply {
            btnSearch.setOnClickListener {
                googleSearch(textDiseaseName.text.toString())
            }
            btnCopy.setOnClickListener { copyToClipBoard(textDiseaseName.text.toString()) }
            btnShare.setOnClickListener { shareScreenshot() }
        }

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
            }
        }
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startCamera(false)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.navigationBarColor =
            resources.getColor(R.color.cameraNavigationBarColor)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.navigationBarColor =
            resources.getColor(R.color.backgroundColor)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun startCamera(isAnimate: Boolean) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProvider = cameraProviderFuture.get()

        cameraProviderFuture.addListener(Runnable {
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.previewCamera.surfaceProvider)

            val cameraSelector = if (isBackCamera) CameraSelector.DEFAULT_BACK_CAMERA
            else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )
                if (isAnimate) binding.motionBase.transitionToState(R.id.state_1)
            } catch (exc: Exception) {
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun startResultScreen() {
        binding.toolbar.visibility = View.INVISIBLE
        binding.imgSelected.visibility = View.VISIBLE
        binding.motionBase.let {
            it.setTransition(R.id.transition_1)
            it.transitionToEnd()
        }
        binding.toolbar.startActionMode(object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean = true
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = true
            override fun onDestroyActionMode(mode: ActionMode?) = stopResultScreen()
        })
    }

    private fun stopResultScreen() {
        binding.toolbar.visibility = View.VISIBLE
        binding.imgSelected.visibility = View.INVISIBLE
        binding.imgSelected.setImageDrawable(null)
        startCamera(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && data != null) {
            cameraProvider.unbindAll()
            val pickedImage = data.data as Uri
            Glide.with(requireContext())
                .load(pickedImage)
                .transform(CenterCrop())
                .into(binding.imgSelected)
            classifyMediator.classify(pickedImage, requireContext())
        }
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

    companion object {
        const val SELECT_IMAGE_REQUEST_CODE = 20
    }
}