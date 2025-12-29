package com.anwin.seekho.ui.AnimeDetailsScreen

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.anwin.seekho.MyApplication
import com.anwin.seekho.R
import com.anwin.seekho.databinding.ActivityAnimeDetailsBinding
import com.anwin.seekho.service.local.db.entity.AnimeEntity
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout

class AnimeDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ANIME_ID = "extra_anime_id"
    }

    private lateinit var binding: ActivityAnimeDetailsBinding
    private lateinit var viewModel: AnimeDetailsVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViewModel()
        setupClickListeners()
        observeAnimeDetails()
        
        // Get anime ID from intent and load details
        val animeId = intent.getIntExtra(EXTRA_ANIME_ID, -1)
        if (animeId != -1) {
            viewModel.start(animeId)
        } else {
            showError("Invalid anime ID")
        }
    }

    private fun initViewModel() {
        val repository = (application as MyApplication).animeRepository
        val factory = AnimeDetailsVMFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AnimeDetailsVM::class.java]
    }

    private fun observeAnimeDetails() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is AnimeDetailsUiState.Loading -> {
                    showLoading(true)
                    hideError()
                }
                is AnimeDetailsUiState.Success -> {
                    showLoading(false)
                    hideError()
                    bindAnimeData(state.anime)
                }
                is AnimeDetailsUiState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        // You can add a progress bar to your layout and show/hide it here
        // For now, we'll just hide/show the main content
        binding.root.alpha = if (show) 0.5f else 1.0f
    }

    private fun hideError() {
        // Hide any error views if you have them
    }

    private fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
        // Optionally finish the activity if the error is critical
        // finish()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.ivPlayButton.setOnClickListener {
            playTrailer()
        }
    }

    private fun setupVideoPlayer(trailerUrl: String) {
        with(binding.videoPlayer) {
            // Convert HTTP to HTTPS if needed
            val secureUrl = if (trailerUrl.startsWith("http://")) {
                trailerUrl.replace("http://", "https://")
            } else {
                trailerUrl
            }
            
            setVideoPath(secureUrl)
            setOnPreparedListener { mediaPlayer ->
                // Video is ready to play
                mediaPlayer.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            }
            
            setOnCompletionListener {
                // Video finished playing, show play button again
                binding.ivPlayButton.visibility = View.VISIBLE
            }
            
            setOnErrorListener { _, what, extra ->
                // Error playing video, show error message
                android.util.Log.e("VideoPlayer", "Error: what=$what, extra=$extra")
//                Toast.makeText(this@AnimeDetailsActivity, "Error playing trailer", Toast.LENGTH_SHORT).show()
                binding.ivPlayButton.visibility = View.GONE

                binding.videoPlayer.visibility = View.GONE
//                binding.ivPlayButton.visibility = View.VISIBLE
                binding.ivPoster.visibility = View.VISIBLE

                true
            }
        }
    }

    private fun playTrailer() {
        with(binding) {
            if (videoPlayer.visibility == View.VISIBLE) {
                videoPlayer.start()
                ivPlayButton.visibility = View.GONE
            } else {
                Toast.makeText(this@AnimeDetailsActivity, "No trailer available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindAnimeData(anime: AnimeEntity) {
        with(binding) {
            // Title
            tvTitle.text = anime.title

            // English title (show only if different from main title)
            if (!anime.titleEnglish.isNullOrEmpty() && anime.titleEnglish != anime.title) {
                tvTitleEnglish.text = anime.titleEnglish
                tvTitleEnglish.visibility = View.VISIBLE
            }

            // Load poster image
            anime.imageUrl?.let { imageUrl ->
                Glide.with(this@AnimeDetailsActivity)
                    .load(imageUrl)
                    .placeholder(R.drawable.sample_image)
                    .error(R.drawable.sample_image)
                    .into(ivPoster)
            }

            // Handle trailer video or poster image
            if (!anime.trailerUrl.isNullOrEmpty()) {
                videoPlayer.visibility = View.VISIBLE
                ivPlayButton.visibility = View.VISIBLE
                ivPoster.visibility = View.GONE

                setupVideoPlayer(anime.trailerUrl)
//                setupVideoPlayer("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
            } else {
                // Show poster image when no trailer
                videoPlayer.visibility = View.GONE
                ivPlayButton.visibility = View.GONE
                ivPoster.visibility = View.VISIBLE
            }

            // Rating
            anime.rating?.let { rating ->
                tvRating.text = String.format("%.1f", rating)
            }

            // Rank
            anime.rank?.let { rank ->
                tvRank.text = getString(R.string.rank_format, rank)
                tvRank.visibility = View.VISIBLE
            }

            // Year
            anime.year?.let { year ->
                tvYear.text = year.toString()
            }

            // Type
            anime.type?.let { type ->
                tvType.text = type
            }

            // Episodes
            anime.episodes?.let { episodes ->
                tvEpisodes.text = if (episodes == 1) {
                    getString(R.string.episode_format, episodes)
                } else {
                    getString(R.string.episodes_format, episodes)
                }
            }

            // Duration
            anime.duration?.let { duration ->
                tvDuration.text = duration
            }

            // Status
            anime.status?.let { status ->
                tvStatus.text = status
            }

            // Synopsis
            anime.synopsis?.let { synopsis ->
                tvSynopsis.text = synopsis
            }

            // Genres
            anime.genres?.let { genresString ->
                setupGenreChips(genresString)
            }

            // Source
            anime.source?.let { source ->
                tvSource.text = source
            }

            // Age Rating
            anime.ageRating?.let { ageRating ->
                tvAgeRating.text = ageRating
                layoutAgeRating.visibility = View.VISIBLE
            }

            // Popularity
            anime.popularity?.let { popularity ->
                tvPopularity.text = getString(R.string.popularity_format, popularity)
                layoutPopularity.visibility = View.VISIBLE
            }

            // Members
            anime.members?.let { members ->
                tvMembers.text = getString(R.string.members_format, members)
                layoutMembers.visibility = View.VISIBLE
            }

            // Favorites
            anime.favorites?.let { favorites ->
                tvFavorites.text = getString(R.string.favorites_format, favorites)
                layoutFavorites.visibility = View.VISIBLE
            }
        }
    }

    private fun setupGenreChips(genresString: String) {
        val genres = genresString.split(" • ").map { it.trim() }
        val flexboxLayout = binding.flexboxGenres

        flexboxLayout.removeAllViews()

        genres.forEach { genre ->
            if (genre.isNotEmpty()) {
                val chip = TextView(this).apply {
                    text = genre
                    setBackgroundResource(R.drawable.type_bg)
                    setPadding(
                        resources.getDimensionPixelSize(R.dimen.chip_padding_horizontal),
                        resources.getDimensionPixelSize(R.dimen.chip_padding_vertical),
                        resources.getDimensionPixelSize(R.dimen.chip_padding_horizontal),
                        resources.getDimensionPixelSize(R.dimen.chip_padding_vertical)
                    )
                    setTextColor(getColor(R.color.white))
                    textSize = 12f
                }

                val layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0,
                        resources.getDimensionPixelSize(R.dimen.chip_margin_end),
                        resources.getDimensionPixelSize(R.dimen.chip_margin_bottom)
                    )
                }

                chip.layoutParams = layoutParams
                flexboxLayout.addView(chip)
            }
        }
    }
}