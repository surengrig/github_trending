package org.freeandroidtools.trendinggithub.ui.repodetail

import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_repo_detail.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.helpers.observe
import org.freeandroidtools.trendinggithub.ui.main.GlideApp
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*


class RepoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)

        val id = intent.getStringExtra(EXTRA_REPO_ID)

        val viewModel = ViewModelProviders.of(this).get(RepoDetailViewModel::class.java)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        viewModel.getRepo(id)
                .observe(this) {
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
    }
}
