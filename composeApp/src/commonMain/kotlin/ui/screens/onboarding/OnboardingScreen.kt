package ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avialogapp.composeapp.generated.resources.Res
import avialogapp.composeapp.generated.resources.onboarding_image_1
import avialogapp.composeapp.generated.resources.onboarding_image_2
import avialogapp.composeapp.generated.resources.onboarding_image_3
import avialogapp.composeapp.generated.resources.onboarding_phone_image_1
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.HorizontalPagerIndicator
import ui.components.LoaderFullScreen

private const val PAGE_COUNT = 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onNewEvent: (OnboardingEvent) -> Unit,
) {
    if (state.showFullScreenLoader) {
        LoaderFullScreen()
    } else {
        Column {
            val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })

            Carousel(
                pagerState = pagerState,
                modifier = Modifier.weight(weight = 1f),
            )
            val scope = rememberCoroutineScope()
            Buttons(
                onNextClick = {
                    scope.launch {
                        if (pagerState.currentPage == PAGE_COUNT - 1) {
                            onNewEvent(OnboardingEvent.SkipClick)
                        } else {
                            pagerState.animateScrollToPage(page = (pagerState.currentPage + 1).coerceAtMost(maximumValue = PAGE_COUNT))
                        }
                    }
                },
                onSkipClick = {
                    onNewEvent(OnboardingEvent.SkipClick)
                },
                modifier = Modifier.padding(all = 16.dp),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Carousel(
    pagerState: PagerState,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(weight = 1f),
        ) {
            Page(pageIndex = it)
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = PAGE_COUNT,
            modifier =
                Modifier.align(alignment = Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp),
        )
    }
}

@Composable
private fun Page(pageIndex: Int) {
    Column {
        Box(modifier = Modifier.weight(weight = 1f)) {
            BackgroundImage(pageIndex = pageIndex)
            PhoneImage(
                pageIndex = pageIndex,
                modifier =
                    Modifier.align(Alignment.TopCenter)
                        .offset(y = 45.dp),
            )
        }
        Text(
            text = getPageText(pageIndex = pageIndex),
            style =
                TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            modifier =
                Modifier
                    .padding(top = 62.dp)
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 100.dp),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun BackgroundImage(pageIndex: Int) {
    Image(
        painter =
            painterResource(
                resource =
                    when (pageIndex) {
                        0 -> Res.drawable.onboarding_image_1
                        1 -> Res.drawable.onboarding_image_2
                        else -> Res.drawable.onboarding_image_3
                    },
            ),
        contentDescription = null,
        modifier =
            Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun PhoneImage(
    pageIndex: Int,
    modifier: Modifier,
) {
    val borderShape = RoundedCornerShape(size = 16.dp)
    Box(
        modifier =
            modifier
                .fillMaxHeight()
                .aspectRatio(ratio = 411 / 731f, matchHeightConstraintsFirst = true)
                .border(
                    border =
                        BorderStroke(
                            width = 8.dp,
                            color = Color(0xFFE7E9E4),
                        ),
                    shape = borderShape,
                ).padding(all = 8.dp),
    ) {
        Image(
            painter =
                painterResource(
                    resource =
                        when (pageIndex) { // TODO: Add other images
                            else -> Res.drawable.onboarding_phone_image_1
                        },
                ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun getPageText(pageIndex: Int) =
    when (pageIndex) {
        0 -> "Prowadź profesjonalny dziennik pilota"
        1 -> "Eksportuj do PDF w formacie zgodnym z EASA, FAA, CASA, TCA"
        else -> "Uzupełniaj wpisy korzystając z gotowych kontaktów"
    }

@Composable
private fun Buttons(
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier,
    ) {
        Button(
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Next")
        }
        TextButton(
            onClick = onSkipClick,
        ) {
            Text(text = "Skip")
        }
    }
}
