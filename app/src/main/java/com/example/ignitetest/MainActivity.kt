package com.example.ignitetest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ignitetest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)

        initListeners()
        initObjects()

       setContentView(binding.root)
    }

    private fun initListeners() {

        binding.appCompatButtonLogin.setOnClickListener(this)

        binding.textViewLinkRegister.setOnClickListener(this)
    }


    private fun initObjects() {
        databaseHelper = DBHelper(this)
        inputValidation = InputValidation(this)
    }


    private fun verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(
                binding.textInputEditTextEmail,
                binding.textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!inputValidation.isInputEditTextEmail(
                binding.textInputEditTextEmail,
                binding.textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(
                binding.textInputEditTextPassword,
                binding.textInputLayoutPassword,
                getString(R.string.error_message_password)
            )
        ) {
            return
        }

        if (databaseHelper.checkUser(
                binding.textInputEditTextEmail.text.toString().trim { it <= ' ' },
                binding.textInputEditTextPassword.text.toString().trim { it <= ' ' })
        ) {
            val accountsIntent = Intent(this, DashboardActivity::class.java)
            accountsIntent.putExtra("EMAIL",binding.textInputEditTextEmail.text.toString().trim { it <= ' ' })
            emptyInputEditText()
            startActivity(accountsIntent)
        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(binding.nestedScrollView,getString(R.string.error_valid_email_password),
                Snackbar.LENGTH_LONG
            ).show()

        }
    }

    private fun emptyInputEditText() {
        binding.textInputEditTextEmail.text = null
        binding.textInputEditTextPassword.text = null
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.appCompatButtonLogin -> verifyFromSQLite()
                R.id.textViewLinkRegister -> {
                    val intentRegister = Intent(applicationContext, SignUp::class.java)
                    startActivity(intentRegister)
                }
            }
        }

    }
}