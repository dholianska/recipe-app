package com.holianska.recipeapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.computeWindowSizeClass

/** Клас розміру поточного вікна (ширина й висота в dp). */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun currentWindowSizeClass(): WindowSizeClass {
    val size = LocalWindowInfo.current.containerDpSize
    return WindowSizeClass.BREAKPOINTS_V1
        .computeWindowSizeClass(size.width.value, size.height.value)
}

/** true, якщо ширина вікна не менша за 600 dp (класи Medium та Expanded). */
@Composable
fun isTwoPaneLayout(): Boolean =
    currentWindowSizeClass()
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)