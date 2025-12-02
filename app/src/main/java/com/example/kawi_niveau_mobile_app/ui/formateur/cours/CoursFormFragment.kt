package com.example.kawi_niveau_mobile_app.ui.formateur.cours

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.requests.CoursRequest
import com.example.kawi_niveau_mobile_app.databinding.FragmentCoursFormBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class CoursFormFragment : Fragment() {

    private var _binding: FragmentCoursFormBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: CoursViewModel by viewModels()
    private val args: CoursFormFragmentArgs by navArgs()
    
    private var selectedImageUri: Uri? = null
    private var uploadedFilename: String? = null
    private val isEditMode get() = args.coursId != -1L

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                displayImage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupCategorieSpinner()
        setupObservers()
        setupListeners()
        
        if (isEditMode) {
            viewModel.loadCoursById(args.coursId)
        }
    }

    private fun setupCategorieSpinner() {
        val categories = listOf(
            "Sélectionnez une catégorie",
            "Programmation",
            "Design",
            "Marketing",
            "Business",
            "Langues",
            "Sciences",
            "Mathématiques",
            "Développement Personnel",
            "Autre"
        )
        
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategorie.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.coursDetail.observe(viewLifecycleOwner) { cours ->
            binding.apply {
                editTextTitre.setText(cours.titre)
                editTextDescription.setText(cours.description)
                
                cours.categorie?.let { cat ->
                    val position = (binding.spinnerCategorie.adapter as ArrayAdapter<String>)
                        .getPosition(cat)
                    if (position >= 0) {
                        binding.spinnerCategorie.setSelection(position)
                    }
                }
                
                cours.thumbnailUrl?.let { filename ->
                    uploadedFilename = filename
                    val imageUrl = "${BuildConfig.API_BASE_URL}images/cours/$filename"
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_course_placeholder)
                        .into(imageViewPreview)
                    imageViewPlaceholder.visibility = View.GONE
                    imageViewPreview.visibility = View.VISIBLE
                    buttonRemoveImage.visibility = View.VISIBLE
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonSave.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (it.contains("upload", ignoreCase = true) || it.contains("401")) {
                    Snackbar.make(binding.root, "Impossible d'uploader l'image. Créer le cours sans image ?", Snackbar.LENGTH_LONG)
                        .setAction("Oui") {
                            selectedImageUri = null
                            uploadedFilename = null
                            saveCours()
                        }
                        .show()
                } else {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
                viewModel.clearMessages()
            }
        }

        viewModel.success.observe(viewLifecycleOwner) { success ->
            success?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                viewModel.clearMessages()
                findNavController().navigateUp()
            }
        }

        viewModel.uploadedFilename.observe(viewLifecycleOwner) { filename ->
            filename?.let {
                uploadedFilename = it
                saveCours()
            }
        }
    }

    private fun setupListeners() {
        binding.buttonSelectImage.setOnClickListener {
            openImagePicker()
        }

        binding.buttonRemoveImage.setOnClickListener {
            removeImage()
        }

        binding.buttonSave.setOnClickListener {
            if (validateForm()) {
                if (selectedImageUri != null) {
                    uploadImage()
                } else {
                    saveCours()
                }
            }
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun displayImage(uri: Uri) {
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.ic_course_placeholder)
            .into(binding.imageViewPreview)
        
        binding.imageViewPlaceholder.visibility = View.GONE
        binding.imageViewPreview.visibility = View.VISIBLE
        binding.buttonRemoveImage.visibility = View.VISIBLE
    }

    private fun removeImage() {
        selectedImageUri = null
        uploadedFilename = null
        binding.imageViewPlaceholder.visibility = View.VISIBLE
        binding.imageViewPreview.visibility = View.GONE
        binding.buttonRemoveImage.visibility = View.GONE
    }

    private fun validateForm(): Boolean {
        val titre = binding.editTextTitre.text.toString().trim()
        
        if (titre.isEmpty()) {
            binding.editTextTitre.error = "Le titre est obligatoire"
            return false
        }
        
        return true
    }

    private fun uploadImage() {
        selectedImageUri?.let { uri ->
            try {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val file = File(requireContext().cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(file)
                
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                
                viewModel.uploadThumbnail(file)
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Erreur lors de la préparation de l'image", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun saveCours() {
        val titre = binding.editTextTitre.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()
        val categoriePosition = binding.spinnerCategorie.selectedItemPosition
        val categorie = if (categoriePosition > 0) {
            binding.spinnerCategorie.selectedItem.toString()
        } else {
            null
        }
        
        val request = CoursRequest(
            titre = titre,
            description = description.ifEmpty { null },
            categorie = categorie,
            thumbnailUrl = uploadedFilename
        )
        
        if (isEditMode) {
            viewModel.updateCours(args.coursId, request)
        } else {
            viewModel.createCours(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
