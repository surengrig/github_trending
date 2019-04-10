package org.freeandroidtools.trendinggithub.ui.repodetail

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_repo_detail.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.helpers.Constants
import org.freeandroidtools.trendinggithub.helpers.observe
import org.freeandroidtools.trendinggithub.ui.trending.GlideApp
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.Locale


class RepoDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: RepoDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)

        val id = intent.getStringExtra(EXTRA_REPO_ID)
        val repository = intent.getStringExtra(EXTRA_REPO)

        viewModel = ViewModelProviders.of(this).get(RepoDetailViewModel::class.java)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupObservers(id, repository)
    }

    private fun setupObservers(id: String, repository: String) {
        val liveData = when (repository) {
            Constants.REPOSITORY_STARRED -> viewModel.geStarredRepo(id)
            Constants.REPOSITORY_TRENDING -> viewModel.getRepo(id)
            else -> throw Exception("Unknown repository table")
        }

        liveData.observe(this) {
            it?.let {
                val repo = it[0]
                runOnUiThread {
                    toolbar.title = repo.fullName
                }

                repo_description_textview.text = repo.description
                fork_count_textview.text = repo.forks.toString()
                stars_textview.text = repo.stargazersCount.toString()
                repo_name_textview.text = repo.name

                GlideApp.with(this)
                    .load(repo.owner.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_github)
                    .into(avatar_imageview)

                owner_name_textview.text = repo.owner.login

                val formatter = DateTimeFormat
                    .forStyle("M-")
                    .withLocale(Locale.getDefault())
                val date = DateTime(repo.updatedAt)
                repo_updated_textview.text = formatter.print(date)

                open_url.setOnClickListener { openUrl(repo.htmlUrl) }
            }
        }
    }

    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setShowTitle(true)
            .build()
            .launchUrl(this, uri)
    }

    companion object {
        const val EXTRA_REPO_ID = "extra_repo_id"
        const val EXTRA_REPO = "extra_repo"
    }
}
