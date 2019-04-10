package org.freeandroidtools.trendinggithub.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.helpers.Success
import org.freeandroidtools.trendinggithub.helpers.observe
import org.freeandroidtools.trendinggithub.ui.BaseActivity
import org.freeandroidtools.trendinggithub.ui.login.LoginActivity
import org.freeandroidtools.trendinggithub.ui.main.MainActivity
import timber.log.Timber


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        viewModel.autoLogin().observe(this) {
            Timber.d("authorisedUser = $it")

            val i = when (it) {
                is Success -> Intent(this, MainActivity::class.java)
                else -> Intent(this, LoginActivity::class.java)
            }
            startActivity(i)
            finish()
        }
    }
}
