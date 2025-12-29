package com.anwin.seekho.ui.mainScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anwin.seekho.MyApplication
import com.anwin.seekho.R
import com.anwin.seekho.databinding.ActivityMainBinding
import com.anwin.seekho.service.local.db.entity.AnimeEntity
import com.anwin.seekho.ui.AnimeDetailsScreen.AnimeDetailsActivity
import com.anwin.seekho.ui.AnimeDetailsScreen.startAnimeDetailsActivity

class MainActivity : AppCompatActivity(), OnAnimeClickListener {

    val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityVM
    
    val animeListAdapter: AnimeListAdapter by lazy {
        AnimeListAdapter(mutableListOf(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViewModel()
        setupRecyclerView()
        setupSwipeRefresh()
        observeAnimeList()
        
        viewModel.loadAnimeList()
    }

    private fun initViewModel() {
        val repository = (application as MyApplication).animeRepository
        val factory = MainActivityVMFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainActivityVM::class.java]
    }

    private fun setupRecyclerView() {
        binding.animeListRV.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = animeListAdapter
        }

        val divider = DividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
        )
        ContextCompat.getDrawable(this, R.drawable.divider)?.let {
            divider.setDrawable(it)
        }

        binding.animeListRV.addItemDecoration(divider)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
        
        binding.swipeRefresh.setColorSchemeResources(
            R.color.purple,
            R.color.primary,
            R.color.black
        )
    }

    private fun observeAnimeList() {

        viewModel.uiState.observe(this) { state ->
            when (state) {

                is UiState.Loading -> {
                    binding.loaderLayout.root.visibility = View.VISIBLE
                    binding.swipeRefresh.isRefreshing = false
                    binding.animeListRV.visibility = View.GONE
                    binding.emptyStateLayout.root.visibility = View.GONE
                    binding.errorStateLayout.root.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.loaderLayout.root.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false

                    animeListAdapter.setData(state.animeList)

                    if (state.animeList.isEmpty()) {
                        binding.animeListRV.visibility = View.GONE
                        binding.emptyStateLayout.root.visibility = View.VISIBLE
                        binding.errorStateLayout.root.visibility = View.GONE

                        binding.emptyStateLayout.retryButton.setOnClickListener {
                            viewModel.refreshData()
                        }
                    } else {
                        binding.animeListRV.visibility = View.VISIBLE
                        binding.emptyStateLayout.root.visibility = View.GONE
                        binding.errorStateLayout.root.visibility = View.GONE
                    }
                }

                is UiState.Error -> {
                    binding.loaderLayout.root.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false

                    if (animeListAdapter.itemCount == 0) {
                        binding.animeListRV.visibility = View.GONE
                        binding.errorStateLayout.root.visibility = View.VISIBLE
                        binding.emptyStateLayout.root.visibility = View.GONE
                    } else {
                        binding.animeListRV.visibility = View.VISIBLE
                        binding.errorStateLayout.root.visibility = View.GONE
                    }

                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()

                    binding.errorStateLayout.retryButton.setOnClickListener {
                        viewModel.refreshData()
                    }
                }
            }
        }
    }

    override fun onAnimeClick(item: AnimeEntity, position: Int) {
        startAnimeDetailsActivity(item.id)
    }
}