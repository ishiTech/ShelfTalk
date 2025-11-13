package com.example.shelftalk.ui.reviews

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shelftalk.databinding.ActivityViewReviewsBinding
import com.example.shelftalk.ui.adapters.ReviewsAdapter
import com.example.shelftalk.viewmodels.ReviewState
import com.example.shelftalk.viewmodels.ReviewViewModel

class ViewReviewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewReviewsBinding
    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var adapter: ReviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "All Reviews"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupObservers()
        viewModel.loadReviews()
    }

    private fun setupRecyclerView() {
        adapter = ReviewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.reviewState.observe(this) { state ->
            when (state) {
                is ReviewState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is ReviewState.Loaded -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is ReviewState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        viewModel.reviews.observe(this) { reviews ->
            adapter.submitList(reviews)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}