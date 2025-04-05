package com.dhkim.moviepick

import androidx.lifecycle.ViewModel
import com.dhkim.domain.movie.usecase.GetMovieDetailUseCase
import com.dhkim.domain.movie.usecase.GetMovieReviewsUseCase
import com.dhkim.domain.tv.usecase.GetTvDetailUseCase
import com.dhkim.domain.tv.usecase.GetTvReviewsUseCase

class SeriesDetailViewModel(
    private val series: String,
    private val seriesId: String,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getTvDetailUseCase: GetTvDetailUseCase,
    private val getTvReviewsUseCase: GetTvReviewsUseCase,
) : ViewModel() {

}