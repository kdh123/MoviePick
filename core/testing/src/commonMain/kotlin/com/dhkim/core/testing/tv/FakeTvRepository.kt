package com.dhkim.core.testing.tv

import app.cash.paging.PagingData
import com.dhkim.common.Genre
import com.dhkim.common.Language
import com.dhkim.common.Region
import com.dhkim.common.Review
import com.dhkim.common.SeriesImage
import com.dhkim.common.Video
import com.dhkim.domain.tv.model.Tv
import com.dhkim.domain.tv.model.TvDetail
import com.dhkim.domain.tv.repository.TvRepository
import kotlinx.coroutines.flow.Flow

class FakeTvRepository : TvRepository {

    private val remoteTvDataSource = FakeRemoteTvDataSource()

    override fun getAiringTodayTvs(language: Language): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getAiringTodayTvs(language)
    }

    override fun getOnTheAirTvs(language: Language): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getOnTheAirTvs(language)
    }

    override fun getTopRatedTvs(language: Language): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getTopRatedTvs(language)
    }

    override fun getTvWithCategory(language: Language, genre: Genre?, region: Region?): Flow<PagingData<Tv>> {
        return remoteTvDataSource.getTvWithCategory(language, genre, region)
    }

    override fun getTvVideos(id: String, language: Language): Flow<List<Video>> {
        return remoteTvDataSource.getTvVideos(id, language)
    }

    override fun getTvDetail(id: String, language: Language): Flow<TvDetail> {
        return remoteTvDataSource.getTvDetail(id, language)
    }

    override fun getTvReviews(id: String): Flow<PagingData<Review>> {
        return remoteTvDataSource.getTvReviews(id)
    }

    override fun getTvCastMembers(id: String, language: Language): Flow<List<String>> {
        return remoteTvDataSource.getTvCastMembers(id, language)
    }

    override fun getTvImages(id: String): Flow<List<SeriesImage>> {
        return remoteTvDataSource.getTvImages(id)
    }
}