package org.freeandroidtools.trendinggithub.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.ui.login.LoginActivity
import org.freeandroidtools.trendinggithub.ui.starred.StarredFragment
import org.freeandroidtools.trendinggithub.ui.trending.GlideApp
import org.freeandroidtools.trendinggithub.ui.trending.TrendingFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setupViews()

        if (savedInstanceState == null) {
            replaceFragment(TrendingFragment.newInstance())
        }
    }

    private fun setupViews() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val user = TrendingApp.appComponent.getApp().user

        val toggleLogoutNav = nav_view.getHeaderView(0)
            .findViewById<ImageView>(R.id.toggle_logout_button)
        val userNameTextView = nav_view.getHeaderView(0)
            .findViewById<TextView>(R.id.username_textview)
        val userImageView = nav_view.getHeaderView(0)
            .findViewById<ImageView>(R.id.user_imageView)

        userNameTextView.text = user?.login
        GlideApp.with(this)
            .load(user?.avatar_url)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.ic_github)
            .into(userImageView)

        toggleLogoutNav.setOnClickListener {
            toggleLogoutNav()
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_content, fragment)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        todo remember selected item and prevent fragment reloading
        when (item.itemId) {
            R.id.nav_trending -> {
                replaceFragment(TrendingFragment.newInstance())
            }
            R.id.nav_starred -> {
                replaceFragment(StarredFragment.newInstance())
            }
            R.id.nav_logout -> {
                logout()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
//    todo add alert dialog
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        invalidateMainMenu()
        return super.onPrepareOptionsMenu(menu)
    }

    private var isLogoutNav = false
    private fun toggleLogoutNav() {
        isLogoutNav = !isLogoutNav
        invalidateMainMenu()
    }

    private fun invalidateMainMenu() {
        nav_view.menu.apply {
            setGroupVisible(R.id.nav_account_group, isLogoutNav)
            setGroupVisible(R.id.nav_main_group, !isLogoutNav)
        }
    }
}
