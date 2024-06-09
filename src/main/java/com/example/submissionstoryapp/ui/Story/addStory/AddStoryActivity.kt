package com.example.submissionstoryapp.ui.Story.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.submissionstoryapp.CameraActivity
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.response.repository.Result
import com.example.submissionstoryapp.databinding.ActivityAddStoryBinding
import com.example.submissionstoryapp.ui.Story.ListStoryActivity
import com.example.submissionstoryapp.utils.ViewModelFactory
import com.riyaldi.storyapp.utils.reduceFileImage
import com.riyaldi.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var binding: ActivityAddStoryBinding
    private val createStoryViewModel: CreateStoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedUriString = intent.getStringExtra("selected_image")
        if (savedUriString != null) {
            val savedUri = Uri.parse(savedUriString)
            try {
                val myFile = uriToFile(savedUri, this)
                getFile = myFile
                binding.fotoStory.setImageURI(savedUri)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnCamera.setOnClickListener{
            val intent = Intent(this,CameraActivity::class.java)
            startActivity(intent)
        }
        binding.btnGalery.setOnClickListener{
            startGallery()
        }
        binding.buttonAdd.setOnClickListener{
            uploadImage()
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.fotoStory.setImageURI(selectedImg)
        }
    }

    private var getFile: File? = null
    private fun uploadImage() {
        if (getFile != null) {
            showLoading(true)
            val file = reduceFileImage(getFile as File)
            val descriptionText = binding.inputDeskripsi.text
            if (!descriptionText.isNullOrEmpty()) {
                val description = descriptionText.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                createStoryViewModel.postStory(imageMultipart, description).observe(this) {
                    if (it != null) {
                        when (it) {
                            is Result.Success -> {
                                showLoading(false)
                                Toast.makeText(this, it.data.message, Toast.LENGTH_LONG).show()
                                val intent = Intent(this,ListStoryActivity::class.java)
                                startActivity(intent)
                            }
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Caption Harus Diisi", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        } else {
            Toast.makeText(this, "Harus Menyertakan Gambar", Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }
    private fun showLoading(state: Boolean) {
        binding.pbCreateStory.isVisible = state
        binding.inputDeskripsi.isInvisible = state
        binding.fotoStory.isInvisible = state
        binding.btnGalery.isInvisible = state
        binding.btnCamera.isInvisible = state
        binding.buttonAdd.isInvisible = state
    }



}