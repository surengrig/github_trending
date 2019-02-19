package org.freeandroidtools.trendinggithub.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.helpers.observe


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val recyclerView = repos_recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val adapter = ReposAdapter()
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        val viewModel = ViewModelProviders.of(this).get(TrendingViewModel::class.java)

        swiperefresh.isRefreshing = true
        viewModel.getTrending()
            .observe(this) {
                swiperefresh.isRefreshing = false
                it?.let {
                    adapter.updateData(it)
                }
            }

        swiperefresh.setOnRefreshListener {
            viewModel.refreshTrending()
        }
    }
}
