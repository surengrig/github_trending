package org.freeandroidtools.trendinggithub.ui.starred


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_starred.*
import org.freeandroidtools.trendinggithub.R
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.helpers.Constants
import org.freeandroidtools.trendinggithub.helpers.observe
import org.freeandroidtools.trendinggithub.ui.trending.ReposAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [StarredFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class StarredFragment : androidx.fragment.app.Fragment() {

    private lateinit var viewModel: StarredViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(StarredViewModel::class.java)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_starred, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = starred_recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        val adapter = ReposAdapter(Constants.REPOSITORY_STARRED)
        recyclerView.adapter = adapter

        val user = TrendingApp.appComponent.getApp().user
        user?.let {
            viewModel.getStarred(it.login).observe(this) {
                if (it != null) {
                    adapter.updateData(it)
                }
            }
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
        fun newInstance() = StarredFragment()
    }
}
