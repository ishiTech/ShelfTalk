package com.example.shelftalk.ui.summaries

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shelftalk.databinding.ActivityViewSummariesBinding
import com.example.shelftalk.ui.adapters.SummariesAdapter
import com.example.shelftalk.viewmodels.SummaryState
import com.example.shelftalk.viewmodels.SummaryViewModel

class ViewSummariesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewSummariesBinding
    private val viewModel: SummaryViewModel by viewModels()
    private lateinit var adapter: SummariesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSummariesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "All Summaries"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupObservers()
        viewModel.loadSummaries()
    }

    private fun setupRecyclerView() {
        adapter = SummariesAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.summaryState.observe(this) { state ->
            when (state) {
                is SummaryState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is SummaryState.Loaded -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is SummaryState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        viewModel.summaries.observe(this) { summaries ->
            adapter.submitList(summaries)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}