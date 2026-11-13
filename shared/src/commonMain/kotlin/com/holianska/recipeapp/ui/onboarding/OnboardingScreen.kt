package com.holianska.recipeapp.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.holianska.recipeapp.ui.common.CenteredScrollColumn
import com.holianska.recipeapp.ui.common.isTwoPaneLayout
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import recipeapp.shared.generated.resources.*

private val TwoPaneCardMaxWidth = 420.dp

private data class OnboardingPageData(
    val title: StringResource,
    val description: StringResource,
    val shiftTitle: StringResource,
    val time: String,
    val filled: Int,
    val total: Int,
    val accent: Int,
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.Finished -> onFinish()
            }
        }
    }

    OnboardingContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun OnboardingContent(
    state: OnboardingState,
    onIntent: (OnboardingIntent) -> Unit,
) {
    val pages = remember {
        listOf(
            OnboardingPageData(Res.string.onboarding_title_1, Res.string.onboarding_desc_1,
                Res.string.shift_food, "10:00 – 14:00", 3, 5, 0),
            OnboardingPageData(Res.string.onboarding_title_2, Res.string.onboarding_desc_2,
                Res.string.shift_cleanup, "09:00 – 12:00", 4, 5, 1),
            OnboardingPageData(Res.string.onboarding_title_3, Res.string.onboarding_desc_3,
                Res.string.shift_shelter, "16:00 – 18:00", 5, 5, 2),
        )
    }
    val pagerState = rememberPagerState(pageCount = { pages.size })

    // State → Pager: "Далі" у ViewModel гортає слайд
    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }
    // Pager → Intent: свайп користувача повідомляє ViewModel
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .collect { onIntent(OnboardingIntent.PageChanged(it)) }
    }

    val onNext = { onIntent(OnboardingIntent.Next) }
    val onSkip = { onIntent(OnboardingIntent.Skip) }

    if (isTwoPaneLayout()) {
        OnboardingTwoPane(pages, pagerState, state.isLastPage, onNext, onSkip)
    } else {
        OnboardingSinglePane(pages, pagerState, state.isLastPage, onNext, onSkip)
    }
}

// Вузький екран: усе в одну колонку (як було)
@Composable
private fun OnboardingSinglePane(
    pages: List<OnboardingPageData>,
    pagerState: PagerState,
    isLastPage: Boolean,
    onNext: () -> Unit,
    onSkip: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            SkipButton(onSkip)
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) { page ->
            OnboardingPage(pages[page])
        }

        PageIndicator(
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        )

        NextButton(isLastPage = isLastPage, onClick = onNext, modifier = Modifier.fillMaxWidth())
    }
}

// Широкий екран: ліворуч Pager із картками, праворуч текст, крапки й кнопки
@Composable
private fun OnboardingTwoPane(
    pages: List<OnboardingPageData>,
    pagerState: PagerState,
    isLastPage: Boolean,
    onNext: () -> Unit,
    onSkip: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .windowInsetsPadding(
                    WindowInsets.safeContent.only(WindowInsetsSides.Start + WindowInsetsSides.Vertical)
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.widthIn(max = TwoPaneCardMaxWidth).fillMaxWidth(),
            ) { index ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ShiftMockCard(pages[index])
                }
            }
        }

        CenteredScrollColumn(
            spacing = 24.dp,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .windowInsetsPadding(
                    WindowInsets.safeContent.only(WindowInsetsSides.End + WindowInsetsSides.Vertical)
                ),
            contentPadding = PaddingValues(40.dp),
        ) {
            AnimatedContent(
                targetState = pages[pagerState.currentPage],
                transitionSpec = { fadeIn() togetherWith fadeOut() },
            ) { page ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = stringResource(page.title),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(page.description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            PageIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
                arrangement = Arrangement.Start,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NextButton(
                    isLastPage = isLastPage,
                    onClick = onNext,
                    modifier = Modifier.widthIn(min = 200.dp),
                )
                SkipButton(onSkip)
            }
        }
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    Row(modifier = modifier, horizontalArrangement = arrangement) {
        repeat(pageCount) { index ->
            val selected = currentPage == index
            val width by animateDpAsState(if (selected) 28.dp else 8.dp)
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
            )
        }
    }
}

@Composable
private fun NextButton(
    isLastPage: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(onClick = onClick, modifier = modifier.height(56.dp)) {
        Text(
            text = stringResource(
                if (isLastPage) Res.string.onboarding_start else Res.string.onboarding_next
            ),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun SkipButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(stringResource(Res.string.onboarding_skip))
    }
}

@Composable
private fun OnboardingPage(page: OnboardingPageData) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ShiftMockCard(page)
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ShiftMockCard(page: OnboardingPageData) {
    val accent = when (page.accent) {
        0 -> MaterialTheme.colorScheme.primaryContainer
        1 -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.tertiaryContainer
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(accent)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = page.time,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(page.shiftTitle),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        LinearProgressIndicator(
            progress = { page.filled / page.total.toFloat() },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surface,
        )
        Text(
            text = stringResource(Res.string.onboarding_spots, page.filled, page.total),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}