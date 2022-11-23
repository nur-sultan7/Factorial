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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
        binding.btnCalculate.setOnClickListener {
            val valueString = binding.editTextNumber.text.toString()
            binding.tvFactorial.text = null
            binding.btnCalculate.isEnabled = false
            binding.progressBarLoading.max = 100
            viewModel.calculate(valueString)
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) {
            when (it) {
                is Error -> {
                    Toast.makeText(this@MainActivity, "Input data is empty", Toast.LENGTH_SHORT)
                        .show()
                }
                is Progress -> {
                    binding.progressBarLoading.progress = it.value

                }
                is Factorial -> {
                    binding.tvFactorial.text = it.value
                    binding.btnCalculate.isEnabled = true
                }
            }
        }
    }
}
