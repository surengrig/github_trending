package org.freeandroidtools.trendinggithub.ui.trending


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_trending.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.helpers.Constants
import org.freeandroidtools.trendinggithub.helpers.observe


/**
 * A simple [Fragment] subclass.
 * Use the [TrendingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TrendingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = repos_recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        val adapter = ReposAdapter(Constants.REPOSITORY_TRENDING)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                context,
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TrendingFragment.
         */
        @JvmStatic
        fun newInstance() =
            TrendingFragment()
    }
}
