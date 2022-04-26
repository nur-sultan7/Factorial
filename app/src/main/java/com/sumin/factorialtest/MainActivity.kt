package com.sumin.factorialtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sumin.factorialtest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var value: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
        binding.btnCalculate.setOnClickListener {
            val valueString = binding.editTextNumber.text.toString()
            value = valueString.toLong()
            binding.progressBarLoading.max = value.toInt()
            viewModel.calculate(valueString)
        }
    }

    private fun observeViewModel() {
        viewModel.error.observe(this) {
            if (it) {
                Toast.makeText(this@MainActivity, "Input data is empty", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.progress.observe(this) {
            binding.progressBarLoading.progress = it
            binding.btnCalculate.isEnabled = it >= value
        }
        viewModel.factorial.observe(this) {
            binding.tvFactorial.text = it
        }
    }
}
