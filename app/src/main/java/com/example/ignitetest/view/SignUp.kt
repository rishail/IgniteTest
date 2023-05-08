package com.example.ignitetest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ignitetest.R
import com.example.ignitetest.databinding.ActivitySignUpBinding
import com.example.ignitetest.model.DBHelper
import com.example.ignitetest.model.User
import com.google.android.material.snackbar.Snackbar

class SignUp : AppCompatActivity(), View.OnClickListener {

    private lateinit var  binding: ActivitySignUpBinding
    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DBHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initListeners()

        initObjects()
    }


    private fun initListeners() {
        binding.appCompatButtonRegister.setOnClickListener(this)
        binding.appCompatTextViewLoginLink.setOnClickListener(this)
    }

    private fun initObjects() {
        inputValidation = InputValidation(this)
        databaseHelper = DBHelper(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.appCompatButtonRegister -> postDataToSQLite()
            R.id.appCompatTextViewLoginLink -> finish()
        }
    }

    private fun postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(binding.textInputEditTextName, binding.textInputLayoutName, getString(
                R.string.error_message_name
            ))) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(binding.textInputEditTextEmail, binding.textInputLayoutEmail, getString(
                R.string.error_message_email
            ))) {
            return
        }
        if (!inputValidation.isInputEditTextEmail(binding.textInputEditTextEmail, binding.textInputLayoutEmail, getString(
                R.string.error_message_email
            ))) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(binding.textInputEditTextPassword, binding.textInputLayoutPassword, getString(
                R.string.error_message_password
            ))) {
            return
        }
        if (!inputValidation.isInputEditTextMatches(binding.textInputEditTextPassword,binding.textInputEditTextConfirmPassword,
                binding.textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return
        }
        if (!databaseHelper.checkUser(binding.textInputEditTextEmail.text.toString().trim())) {
            val user = User(name = binding.textInputEditTextName.text.toString().trim(),
                email = binding.textInputEditTextEmail.text.toString().trim(),
                password = binding.textInputEditTextPassword.text.toString().trim())
            databaseHelper.addUser(user)

            Snackbar.make(binding.nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show()
            emptyInputEditText()
        } else {
            Snackbar.make(binding.nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun emptyInputEditText() {
        binding.textInputEditTextName.text = null
        binding.textInputEditTextEmail.text = null
       binding.textInputEditTextPassword.text = null
       binding.textInputEditTextConfirmPassword.text = null
    }
}