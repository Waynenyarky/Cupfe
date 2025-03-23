package com.tamayo_aaron_b.cupfe_expresso

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feedback)



        val radioGroupFeedback: RadioGroup = findViewById(R.id.radioGroupFeedback)
        val editTextFeedback: EditText = findViewById(R.id.editTextFeedback)
        val radioOption6 = findViewById<RadioButton>(R.id.radioOption6)
        val ivBack = findViewById<ImageView>(R.id.ivBack)

        ivBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        // Apply button tint to all radio buttons inside the group
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                getColor(R.color.light_brown), // Checked color
                getColor(R.color.grey)  // Unchecked color
            )
        )

        for (i in 0 until radioGroupFeedback.childCount) {
            val radioButton = radioGroupFeedback.getChildAt(i)
            if (radioButton is RadioButton) {
                radioButton.buttonTintList = colorStateList
            }
        }

        radioGroupFeedback.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton? = findViewById(checkedId)
            selectedRadioButton?.let {
                Toast.makeText(this, "Selected: ${it.text}", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle custom text input for "Other" option
        radioOption6.setOnClickListener {
            val feedbackText = editTextFeedback.text.toString().trim()
            if (feedbackText.isNotEmpty()) {
                Toast.makeText(this, "Other Feedback: $feedbackText", Toast.LENGTH_SHORT).show()
            } else {
                editTextFeedback.error = "Please enter feedback"
                editTextFeedback.setPadding(editTextFeedback.paddingLeft, editTextFeedback.paddingTop, editTextFeedback.paddingRight, 55)
            }
        }


    }
}