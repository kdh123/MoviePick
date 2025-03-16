package com.dhkim.core.ui

import androidx.compose.runtime.Stable
import com.dhkim.common.Series

@Stable
interface RecommendationSeriesScope {
    val series: Series
}

class DefaultRecommendationSeriesScope(override val series: Series) : RecommendationSeriesScope