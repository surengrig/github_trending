package org.freeandroidtools.trendinggithub.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.freeandroidtools.trendinggithub.ui.adapter.ReposAdapter
import org.freeandroidtools.trendinggithub.viewmodel.TrendingViewModel
import org.freeandroidtools.trendinggithub.observe
import org.freeandroidtools.trendinggithub.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val recyclerView = repos_recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ReposAdapter()
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val viewModel = ViewModelProviders.of(this).get(TrendingViewModel::class.java)
        viewModel.trendingData.observe(this) {
            swiperefresh.isRefreshing = false
            it?.let {
                adapter.updateData(it)
            }
        }

        swiperefresh.isRefreshing = true
        viewModel.getTrending()

        swiperefresh.setOnRefreshListener {
            viewModel.getTrending()
        }
    }
}
