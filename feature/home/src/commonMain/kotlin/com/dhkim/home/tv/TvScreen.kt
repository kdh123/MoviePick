package com.dhkim.home.tv

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.dhkim.common.SeriesBookmark
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.Chip
import com.dhkim.core.ui.CircleCloseButton
import com.dhkim.core.ui.ContentItem
import com.dhkim.core.ui.RecommendationSeries
import com.dhkim.core.ui.Resources
import com.dhkim.core.ui.SeriesList
import com.dhkim.domain.tv.model.Tv
import com.dhkim.home.Category
import com.dhkim.home.CategoryModal
import com.dhkim.home.Genre
import com.dhkim.home.Group
import com.dhkim.home.HomeState
import com.dhkim.home.RecommendationButtons
import com.dhkim.home.SeriesItem
import com.dhkim.home.rememberHomeState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource

@ExperimentalSharedTransitionApi
@Composable
fun TvScreen(
    uiState: TvUiState,
    bookmarks: ImmutableList<SeriesBookmark>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onAction: (TvAction) -> Unit,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToSeriesCollection: (seriesType: SeriesType, genreId: Int?, region: String?) -> Unit,
    onBack: () -> Unit,
) {
    val homeState = (uiState.displayState as? TvDisplayState.Contents)?.tvs?.let { seriesItems ->
        rememberHomeState(seriesItems = seriesItems, mainRecommendationSeriesGroup = Group.TvGroup.MAIN_RECOMMENDATION_TV)
    } ?: rememberHomeState(seriesItems = persistentListOf(), mainRecommendationSeriesGroup = Group.TvGroup.MAIN_RECOMMENDATION_TV)

    val backgroundGradientColors = listOf(
        homeState.backgroundColor,
        MaterialTheme.colorScheme.background
    )

    Box(
        modifier = Modifier
            .background(Brush.verticalGradient(backgroundGradientColors))
            .fillMaxSize()
    ) {
        when (uiState.displayState) {
            TvDisplayState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = Color.White.copy(alpha = 0.6f),
                    trackColor = MaterialTheme.colorScheme.primary,
                )
            }

            is TvDisplayState.Contents -> {
                val tvs = uiState.displayState.tvs
                ContentsScreen(
                    homeState = homeState,
                    tvSeriesItems = tvs,
                    bookmarks = bookmarks,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onAction = onAction,
                    navigateToSeriesDetail = navigateToSeriesDetail,
                    navigateToVideo = navigateToVideo,
                    navigateToSeriesCollection = navigateToSeriesCollection,
                    onBack = onBack
                )
            }

            is TvDisplayState.Error -> {

            }
        }
    }
}

@ExperimentalSharedTransitionApi
@Composable
fun ContentsScreen(
    homeState: HomeState,
    bookmarks: ImmutableList<SeriesBookmark>,
    tvSeriesItems: ImmutableList<SeriesItem>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
    navigateToVideo: (String) -> Unit,
    navigateToSeriesCollection: (seriesType: SeriesType, genreId: Int?, region: String?) -> Unit,
    onAction: (TvAction) -> Unit,
    onBack: () -> Unit
) {
    val onBackgroundColor by animateColorAsState(
        targetValue = if (homeState.showCategory) MaterialTheme.colorScheme.onBackground else homeState.onBackgroundColor,
        animationSpec = tween(500),
        label = ""
    )

    val selectedChipTextColor by animateColorAsState(
        targetValue = if (homeState.showCategory) MaterialTheme.colorScheme.background else homeState.backgroundColor,
        animationSpec = tween(500),
        label = ""
    )

    Box {
        LazyColumn(state = homeState.listState) {
            items(items = tvSeriesItems, key = { item -> item.group }) { item ->
                when (item.group as Group.TvGroup) {
                    Group.TvGroup.APP_BAR -> {
                        AppBar(onBackGroundColor = homeState.onBackgroundColor)
                    }

                    Group.TvGroup.CATEGORY -> {
                        TvCategoryChips(
                            chipColor = homeState.onBackgroundColor,
                            selectedChipTextColor = selectedChipTextColor,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onCategoryClick = { homeState.showCategoryModal = !homeState.showCategoryModal },
                            onBack = onBack
                        )
                    }

                    Group.TvGroup.MAIN_RECOMMENDATION_TV -> {
                        val tvs = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        if (tvs.itemCount > 0) {
                            val series = tvs[0] as Tv
                            val isBookmarked = bookmarks.any { it.id == series.id }
                            RecommendationSeries(
                                series = series,
                                onClick = { navigateToSeriesDetail(SeriesType.TV, series.id) },
                                onUpdateRecommendationSeriesHeight = homeState::updateHeight
                            ) {
                                Genre()
                                RecommendationButtons(
                                    isBookmarked = isBookmarked,
                                    onBookmarkClick = {
                                        if (isBookmarked) {
                                            onAction(TvAction.DeleteBookmark(it, SeriesType.TV))
                                        } else {
                                            onAction(TvAction.AddBookmark(it, SeriesType.TV))
                                        }
                                    },
                                    navigateToVideo = navigateToVideo
                                )
                            }
                        }
                    }

                    Group.TvGroup.NEWS_TV,
                    Group.TvGroup.ANIMATION_TV,
                    Group.TvGroup.COMEDY_TV,
                    Group.TvGroup.AIRING_TODAY_TV,
                    Group.TvGroup.ON_THE_AIR_TV,
                    Group.TvGroup.TOP_RATED_TV -> {
                        val tvs = (item as SeriesItem.Content).series.collectAsLazyPagingItems()
                        SeriesList(title = (item.group as Group.TvGroup).title, series = tvs) { _, tv ->
                            ContentItem(
                                series = tv as Tv,
                                onClick = {
                                    navigateToSeriesDetail(SeriesType.TV, tv.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (homeState.showTab) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = homeState.backgroundColor)
                ) {
                    TvCategoryChips(
                        chipColor = onBackgroundColor,
                        selectedChipTextColor = selectedChipTextColor,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onCategoryClick = { homeState.showCategoryModal = !homeState.showCategoryModal },
                        onBack = onBack
                    )
                }
            }
        }

        if (homeState.showCategoryModal) {
            val categories = mutableListOf<Category>().apply {
                Category.Region.entries.forEach { add(it) }
                Category.TvGenre.entries.forEach { add(it) }
            }
            CategoryModal(
                categories = categories,
                onCategoryClick = {
                    when (it) {
                        is Category.Region -> navigateToSeriesCollection(SeriesType.TV, null, it.code)
                        is Category.TvGenre -> navigateToSeriesCollection(SeriesType.TV, it.id, null)
                        else -> {}
                    }
                    homeState.showCategoryModal = false
                },
                onClose = { homeState.showCategoryModal = false }
            )
        }
    }
}

@Composable
private fun AppBar(onBackGroundColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, bottom = 4.dp)
    ) {
        Text(
            text = "TV 프로그램",
            style = MoviePickTheme.typography.headlineSmallBold,
            color = onBackGroundColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
    }
}

@ExperimentalSharedTransitionApi
@Composable
private fun TvCategoryChips(
    chipColor: Color,
    selectedChipTextColor: Color,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onCategoryClick: () -> Unit,
    onBack: () -> Unit,
) {
    with(sharedTransitionScope) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            CircleCloseButton(color = chipColor, onClick = onBack)

            Chip(
                modifier = Modifier
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "tv-category"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                backgroundColor = chipColor,
                borderColor = chipColor,
            ) {
                Text(
                    text = "TV 프로그램",
                    color = selectedChipTextColor,
                    style = MoviePickTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                )
            }

            Chip(
                borderColor = chipColor,
                modifier = Modifier
                    .clickable(onClick = onCategoryClick),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "카테고리",
                        color = chipColor,
                        style = MoviePickTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                    )

                    Icon(
                        painter = painterResource(Resources.Icon.DropDown),
                        contentDescription = null,
                        tint = chipColor
                    )
                }
            }
        }
    }
}