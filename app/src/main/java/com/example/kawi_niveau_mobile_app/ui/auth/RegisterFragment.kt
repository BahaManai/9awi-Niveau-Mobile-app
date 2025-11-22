package com.example.kawi_niveau_mobile_app.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.net.Uri
import android.provider.OpenableColumns
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentRegisterBinding
import com.example.kawi_niveau_mobile_app.ui.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.buffer
import okio.sink
import androidx.lifecycle.lifecycleScope
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.profileImagePreview.setImageURI(it)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            if (validateInput()) {
                val firstName = binding.firstNameInput.text.toString().trim()
                val lastName = binding.lastNameInput.text.toString().trim()
                val dateOfBirth = binding.dateOfBirthInput.text.toString().trim()
                val email = binding.emailInput.text.toString().trim()
                val password = binding.passwordInput.text.toString()
                val phoneNumber = binding.phoneNumberInput.text.toString().trim()
                viewModel.register(firstName, lastName, dateOfBirth, email, password, if (phoneNumber.isEmpty()) null else phoneNumber)
                // if image selected, wait for register success and then upload
            }
        }

        binding.selectImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.loginLink.setOnClickListener {
            findNavController().navigateUp()
        }

        observeViewModel()
    }

    private fun validateInput(): Boolean {
        val firstName = binding.firstNameInput.text.toString().trim()
        val lastName = binding.lastNameInput.text.toString().trim()
        val dateOfBirth = binding.dateOfBirthInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()
        val passwordConfirm = binding.passwordConfirmInput.text.toString()
        val phoneNumber = binding.phoneNumberInput.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(requireContext(), "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Format d'email invalide", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(requireContext(), "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != passwordConfirm) {
            Toast.makeText(requireContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
            return false
        }

        // phone number basic validation: expect international format or local 8 digits
        if (phoneNumber.isNotEmpty()) {
            val digits = phoneNumber.filter { it.isDigit() }
            if (!(phoneNumber.startsWith("+") || digits.length == 8 || digits.length >= 8)) {
                Toast.makeText(requireContext(), "Numéro de téléphone invalide", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = if (result is Resource.Loading) View.VISIBLE else View.GONE
            when (result) {
                is Resource.Success -> {
                    result.data.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                    // after successful register, if an image was selected, upload it using public endpoint
                    val email = binding.emailInput.text.toString().trim()
                    if (selectedImageUri != null) {
                        uploadSelectedImageAfterRegister(selectedImageUri!!, email)
                    } else {
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        viewModel.uploadResult.observe(viewLifecycleOwner) { res ->
            if (res is Resource.Success) {
                Toast.makeText(requireContext(), "Image uploadée", Toast.LENGTH_SHORT).show()
            } else if (res is Resource.Error) {
                Toast.makeText(requireContext(), "Upload image échoué: ${res.message}", Toast.LENGTH_LONG).show()
            }
            // navigate to login regardless
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun uploadSelectedImageAfterRegister(uri: Uri, email: String) {
        // convert uri to temp file and create multipart
        lifecycleScope.launchWhenStarted {
            val part = withContext(Dispatchers.IO) {
                val fileName = queryName(uri)
                val input = requireContext().contentResolver.openInputStream(uri) ?: return@withContext null
                val temp = File.createTempFile("upload", fileName.substringAfterLast('.'), requireContext().cacheDir)
                temp.outputStream().use { output -> input.copyTo(output) }
                val mediaType = requireContext().contentResolver.getType(uri) ?: "image/*"
                val reqFile = temp.asRequestBody(mediaType.toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", temp.name, reqFile)
            }
            if (part != null) {
                val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                viewModel.uploadImageAfterRegister(part, emailBody)
            } else {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun queryName(uri: Uri): String {
        var name = "temp"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) name = it.getString(index)
            }
        }
        return name
    }
}