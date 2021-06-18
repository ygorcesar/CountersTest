package com.cornershop.counterstest.counter.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.databinding.CountersActivityBinding
import com.cornershop.counterstest.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountersActivity : AppCompatActivity() {

    private val binding by viewBinding(CountersActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}