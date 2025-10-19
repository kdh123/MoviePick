package com.dhkim.moviepick

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhkim.common.Series
import com.dhkim.common.SeriesType
import com.dhkim.core.designsystem.MoviePickTheme
import com.dhkim.core.ui.ContentItem
import com.dhkim.core.ui.noRippleClick
import com.dhkim.domain.movie.model.Movie
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    movieListState: LazyListState,
    tvListState: LazyListState,
    onAction: (SearchAction) -> Unit,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            QueryInputField(
                onAction = onAction
            )
            SearchResult(
                movieListState = movieListState,
                tvListState = tvListState,
                isLoading = uiState.isLoading,
                contentState = uiState.contentState,
                navigateToSeriesDetail = navigateToSeriesDetail
            )
        }
    }
}

@Composable
fun QueryInputField(
    hint: String = "검색어를 입력해주세요",
    onAction: (SearchAction) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onAction(SearchAction.TypeSearchQuery(it))
        },
        placeholder = { Text(hint) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = LightGray,
            unfocusedBorderColor = LightGray,
            cursorColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
    )
}

@Composable
fun SearchResult(
    movieListState: LazyListState,
    tvListState: LazyListState,
    isLoading: Boolean,
    contentState: SearchContentState,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        when (contentState) {
            SearchContentState.Idle -> Unit
            is SearchContentState.Content -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (contentState.movies.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "영화",
                                style = MoviePickTheme.typography.bodyMediumBold,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            )
                            SearchSeries(
                                listState = movieListState,
                                series = contentState.movies,
                                navigateToSeriesDetail = navigateToSeriesDetail
                            )
                        }
                    }

                    if (contentState.tvs.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "TV 프로그램",
                                style = MoviePickTheme.typography.bodyMediumBold,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            )
                            SearchSeries(
                                listState = tvListState,
                                series = contentState.tvs,
                                navigateToSeriesDetail = navigateToSeriesDetail
                            )
                        }
                    }
                }
            }

            SearchContentState.Empty -> {
                Text(
                    text = "검색 결과가 존재하지 않습니다",
                    style = MoviePickTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )
            }

            is SearchContentState.Error -> {
                Text(
                    text = "검색 결과를 불러오는데 실패하였습니다",
                    style = MoviePickTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(32.dp)
                    .align(Alignment.TopCenter),
                color = Color.White.copy(alpha = 0.6f),
                trackColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun SearchSeries(
    listState: LazyListState,
    series: ImmutableList<Series>,
    navigateToSeriesDetail: (seriesType: SeriesType, seriesId: String) -> Unit,
) {
    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        items(items = series, key = { it.id }) {
            ContentItem(
                series = it,
                onClick = {
                    val seriesType = if (it is Movie) SeriesType.MOVIE else SeriesType.TV
                    navigateToSeriesDetail(seriesType, it.id)
                }
            )
        }
    }
}
