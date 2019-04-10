package org.freeandroidtools.trendinggithub.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.helpers.Error
import org.freeandroidtools.trendinggithub.helpers.Success
import org.freeandroidtools.trendinggithub.helpers.handleError
import org.freeandroidtools.trendinggithub.helpers.observe
import org.freeandroidtools.trendinggithub.ui.main.MainActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        setupObservers()
        sign_in_button.setOnClickListener {
            val user = username_edittext.text.toString()
            val password = password_edittext.text.toString()
            viewModel.signin(user, password)
        }
    }

    private fun setupObservers() {
        viewModel.singinData.observe(this) {
            when (it) {
                is Success -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
                is Error -> {
                    username_edittext.handleError(it)
                }
            }
        }
    }
}
