package com.example.shelftalk.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.shelftalk.R
import com.example.shelftalk.databinding.ActivityHomeBinding
import com.example.shelftalk.repository.FirebaseRepository
import com.example.shelftalk.ui.auth.LoginActivity
import com.example.shelftalk.ui.reviews.AddReviewActivity
import com.example.shelftalk.ui.reviews.ViewReviewsActivity
import com.example.shelftalk.ui.summaries.AddSummaryActivity
import com.example.shelftalk.ui.summaries.ViewSummariesActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val repository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "ShelfTalk"

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardAddReview.setOnClickListener {
            startActivity(Intent(this, AddReviewActivity::class.java))
        }

        binding.cardViewReviews.setOnClickListener {
            startActivity(Intent(this, ViewReviewsActivity::class.java))
        }

        binding.cardAddSummary.setOnClickListener {
            startActivity(Intent(this, AddSummaryActivity::class.java))
        }

        binding.cardViewSummaries.setOnClickListener {
            startActivity(Intent(this, ViewSummariesActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                repository.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}