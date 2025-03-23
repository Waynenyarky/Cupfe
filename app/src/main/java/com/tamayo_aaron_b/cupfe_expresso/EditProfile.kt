package com.tamayo_aaron_b.cupfe_expresso

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yalantis.ucrop.UCrop
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class EditProfile : AppCompatActivity() {
    private lateinit var imagePicker: ImageView
    private var imageUri: Uri? = null
    private lateinit var sharedPreferences: SharedPreferences

    // Pick image from gallery
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            startCrop(it)  // Start cropping after selecting
        } ?: run {
            Toast.makeText(this, "Failed to pick image", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle UCrop result
    // Save and display the cropped image
    private val cropImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                imageUri = resultUri
                imagePicker.setImageURI(resultUri)  // Show edited image
                saveImageUri(resultUri.toString()) // Persist image
            } else {
                Toast.makeText(this, "Error retrieving cropped image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)


        imagePicker = findViewById(R.id.imagePicker)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnOTP = findViewById<Button>(R.id.btnOTP)
        val etNewPass = findViewById<EditText>(R.id.etNewPass)
        val btnPhoto = findViewById<TextView>(R.id.btnPhoto)
        val ivEye = findViewById<ImageView>(R.id.ivEye)
        val etFName = findViewById<EditText>(R.id.etFName)
        val tvName = findViewById<TextView>(R.id.tvName)
        val btnSavedName = findViewById<Button>(R.id.btnSavedName)
        val etRePass = findViewById<EditText>(R.id.etRePass)
        val ivEye2 = findViewById<ImageView>(R.id.ivEye2)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)


        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null) ?: "No email"
        tvEmail.text = email

        val username = sharedPreferences.getString("USERNAME", null) ?: "Hello User!"
        tvName.text = username

        Log.d("DEBUG", "Email Saved in Edit Profile: $email")
        Log.d("DEBUG", "Name Saved in Edit Profile: $username")

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)


        var isPasswordVisible = false
        ivEye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                etNewPass.transformationMethod = null // Show password
                ivEye.setImageResource(R.drawable.eye_show)
            } else {
                etNewPass.transformationMethod = PasswordTransformationMethod.getInstance() // Hide password
                ivEye.setImageResource(R.drawable.eye_hide)
            }

            etNewPass.setSelection(etNewPass.text.length) // Maintain cursor position
        }

        var isPasswordVisible2 = false

        ivEye2.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2

            if (isPasswordVisible2) {
                etRePass.transformationMethod = null // Show password
                ivEye2.setImageResource(R.drawable.eye_show)
            } else {
                etRePass.transformationMethod = PasswordTransformationMethod.getInstance() // Hide password
                ivEye2.setImageResource(R.drawable.eye_hide)
            }

            etRePass.setSelection(etRePass.text.length) // Maintain cursor position
        }

        // Add emoji detection to all text fields
        listOf(etEmail, etNewPass,etFName ).forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val newText = removeEmojis(it.toString())
                        if (newText != it.toString()) {
                            Toast.makeText(this@EditProfile, "Can't input an Emoji", Toast.LENGTH_SHORT).show()
                            editText.setText(newText)
                            editText.setSelection(newText.length) // Move cursor to the end
                        }
                    }
                }
            })
        }

        // Prevent Enter key press on each EditText
        listOf(etEmail, etNewPass, etFName).forEach { editText ->
            editText.setRawInputType(android.text.InputType.TYPE_CLASS_TEXT) // Force single-line input
            editText.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(this, "Can't enter a new line!", Toast.LENGTH_SHORT).show()
                    return@setOnKeyListener true // Consume the event and prevent new line
                }
                false // Allow other keys to work normally
            }
        }

        // Disable new line (Enter key) for etEmail
        etEmail.setSingleLine(true) // Forces single-line input
        etEmail.setRawInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) // Ensures email format
        etEmail.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(this, "Can't enter a new line!", Toast.LENGTH_SHORT).show()
                return@setOnKeyListener true // Consume the event to disable Enter
            }
            false
        }

        ivBack.setOnClickListener{
            val back = Intent(this, Profile::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }



        loadSavedImage()

        // Open gallery on click
        imagePicker.setOnClickListener {
            pickImage.launch("image/*")
        }

        addValidationWithIcon1(etFName) { isValidName(it) }
        addValidationWithIcon1(etEmail) { isValidEmail(it) }
        addValidationWithIcon(etNewPass) { isValidPassword(it) }
        addValidationWithIcon(etRePass) { isValidPassword(it) }

        // Send OTP
        btnOTP.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val newPassword = etNewPass.text.toString().trim()
            val confirmPassword = etRePass.text.toString().trim()

            if (!isValidEmail(email)) {
                etEmail.error = "Enter your email."
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!isValidPassword(newPassword)) {
                etNewPass.error = "Please enter your new password."
                etNewPass.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword != newPassword) {
                etNewPass.error = "Passwords do not match."
                etNewPass.requestFocus()
                return@setOnClickListener
            }

            btnOTP.isEnabled = false
            btnOTP.text = "Verifying..."

            RetrofitClient.instance.generatePasswordChangeOTP(OTPRequests(email))
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditProfile, "OTP sent successfully", Toast.LENGTH_SHORT).show()

                            // Start ForgotPassword2 activity and pass email & new password
                            val intent = Intent(this@EditProfile, EditProfile2::class.java)
                            intent.putExtra("EMAIL", email)
                            intent.putExtra("NEW_PASSWORD", newPassword)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        } else {
                            Toast.makeText(this@EditProfile, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                        }
                        btnOTP.isEnabled = true
                        btnOTP.text = "Send OTP"
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        btnOTP.isEnabled = true
                        btnOTP.text = "Send OTP"
                        Toast.makeText(this@EditProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }


        btnPhoto.setOnClickListener{
            imageUri?.let { uri ->
                val intent = Intent(this, Profile::class.java)
                intent.putExtra("IMAGE_URI", uri.toString()) // Send image URI
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "Saved Photo", Toast.LENGTH_SHORT).show()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        btnSavedName.setOnClickListener{
            val name = etFName.text.toString().trim()

            if (!isValidName(name)) {
                etFName.error = "Name is required"
                etFName.requestFocus()
                return@setOnClickListener
            }

            val editor = sharedPreferences.edit()
            editor.putString("USERNAME", name)
            editor.apply()

            tvName.text = name
            etFName.setText("")

            // Navigate to Profile.kt
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("USERNAME", name) // Send name to Profile
            startActivity(intent)


        }

    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&+=!])(?!.*[\\s\\p{So}]).{8,}\$".toRegex()
        return password.matches(passwordRegex)
    }

    private fun startCrop(uri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg" // Unique file name
        val destinationUri = Uri.fromFile(File(externalCacheDir, fileName))

        val uCrop = UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)  // Square crop
            .withMaxResultSize(500, 500)
            .withOptions(getUCropOptions())  // Apply round crop UI

        cropImage.launch(uCrop.getIntent(this))
    }



    private fun saveImageUri(uri: String) {
        val editor = sharedPreferences.edit()
        editor.putString("saved_image_uri", uri)
        editor.apply()
    }

    private fun loadSavedImage() {
        val savedUri = sharedPreferences.getString("saved_image_uri", null)
        if (savedUri != null) {
            imageUri = Uri.parse(savedUri)
            imagePicker.setImageURI(imageUri) // Restore the image
        }
    }

    // Define UCrop Options for round crop
    private fun getUCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCircleDimmedLayer(true) // Makes the crop overlay circular
        options.setShowCropFrame(false)    // Hides square frame
        options.setShowCropGrid(false)     // Hides grid
        return options
    }

    // Removes emojis from a given string
    private fun removeEmojis(input: String): String {
        return input.replace(Regex("[\\p{So}\\p{Cn}]"), "")
    }

    //VALID or INVALID
    private fun addValidationWithIcon(editText: EditText, validator: (String) -> Boolean) {
        val checkIcon: Drawable? = ContextCompat.getDrawable(editText.context, R.drawable.ic_check)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }

        val paddingEnd = 140  // Adjust the right padding to fit the icon properly

        editText.setPadding(editText.paddingLeft, editText.paddingTop, paddingEnd, editText.paddingBottom)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                editText.post {
                    if (validator(input)) {
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, checkIcon, null)
                        editText.compoundDrawablePadding = 20 // Adjust padding between text and icon
                        editText.error = null
                    } else {
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun addValidationWithIcon1(editText: EditText, validator: (String) -> Boolean) {
        val checkIcon: Drawable? = ContextCompat.getDrawable(editText.context, R.drawable.ic_check)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }

        val paddingEnd = 50  // Adjust the right padding to fit the icon properly

        editText.setPadding(editText.paddingLeft, editText.paddingTop, paddingEnd, editText.paddingBottom)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                editText.post {
                    if (validator(input)) {
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, checkIcon, null)
                        editText.compoundDrawablePadding = 20 // Adjust padding between text and icon
                        editText.error = null
                    } else {
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun isValidEmail(email: String): Boolean {
        val allowedDomains = listOf("@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com", "@icloud.com", "@aol.com", "@protonmail.com", "@zoho.com")
        val regex = "^[a-z]{8,}[a-z0-9._%+-]*@[a-z]+\\.[a-z]{2,6}\$".toRegex()
        val atIndex = email.indexOf("@")

        if (atIndex < 8 || !regex.matches(email) || email.contains(" ") || email.any { it.isUpperCase() } || email.any { !it.isLetterOrDigit() && it !in ".@_-"}) {
            return false
        }

        return allowedDomains.any { email.endsWith(it, ignoreCase = true) }
    }

    // NAME VALIDATION
    private fun isValidName(name: String): Boolean {
        val nameRegex = "^[A-Za-z]{3,20}(?: [A-Za-z]{1,20})*(?: (II|III|IV|V|VI|VII|VIII|IX|X))?$".toRegex()
        return name.matches(nameRegex)
    }
}