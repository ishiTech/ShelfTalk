package com.example.shelftalk.ui.summaries

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shelftalk.databinding.ActivityAddSummaryBinding
import com.example.shelftalk.viewmodels.SummaryState
import com.example.shelftalk.viewmodels.SummaryViewModel

class AddSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSummaryBinding
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Add Summary"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.summaryState.observe(this) { state ->
            when (state) {
                is SummaryState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSave.isEnabled = false
                }
                is SummaryState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is SummaryState.Error -> {
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
            val summary = binding.etSummary.text.toString()
            val lovedPart = binding.etLovedPart.text.toString()

            viewModel.addSummary(title, summary, lovedPart)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}