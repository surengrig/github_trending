package org.freeandroidtools.trendinggithub.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.repo_list_item.view.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.ui.repodetail.RepoDetailActivity


@GlideModule
class MyAppGlideModule : AppGlideModule()

class ReposAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<ReposAdapter.ViewHolder>() {

    private val items = ArrayList<GithubRepo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.repo_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.initData(items[position])
    }

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun initData(repo: GithubRepo) {
            itemView.repo_description_textview.text = repo.description
            itemView.repo_name_textview.text = repo.name
            itemView.stars_textview.text = repo.stargazersCount.toString()
            itemView.fork_count_textview.text = repo.forksCount.toString()
            itemView.owner_name_textview.text = repo.owner.login

            GlideApp.with(itemView)
                .load(repo.owner.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_github)
                .into(itemView.avatar_imageview)

            itemView.setOnClickListener {
                openDetails(itemView.context, repo)
            }
        }

    }

    private fun openDetails(context: Context, repo: GithubRepo) {
        val intent = Intent(context, RepoDetailActivity::class.java).apply {
            putExtra(RepoDetailActivity.EXTRA_REPO_ID, repo.id)
        }
        context.startActivity(intent)
    }

    fun updateData(repos: List<GithubRepo>) {
        items.clear()
        items.addAll(repos)
        notifyDataSetChanged()
    }
}
