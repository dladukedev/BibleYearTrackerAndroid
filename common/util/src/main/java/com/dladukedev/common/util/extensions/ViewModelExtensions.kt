package com.dladukedev.common.util.extensions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed

private const val VIEW_MODEL_SUBSCRIBED_TIMEOUT = 5000L
val ViewModel.shareInPolicy: SharingStarted
    get() = WhileSubscribed(VIEW_MODEL_SUBSCRIBED_TIMEOUT)
