package com.example.shelftalk.ui.reviews

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shelftalk.databinding.ActivityAddReviewBinding
import com.example.shelftalk.viewmodels.ReviewState
import com.example.shelftalk.viewmodels.ReviewViewModel

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Add Review"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.reviewState.observe(this) { state ->
            when (state) {
                is ReviewState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSave.isEnabled = false
                }
                is ReviewState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ReviewState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            val title = binding.etBookTitle.text.toString()
            val author = binding.etAuthor.text.toString()
            val rating = binding.ratingBar.rating
            val review = binding.etReview.text.toString()

            viewModel.addReview(title, author, rating, review)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}