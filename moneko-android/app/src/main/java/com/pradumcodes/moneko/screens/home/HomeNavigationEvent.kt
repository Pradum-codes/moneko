package com.pradumcodes.moneko.screens.home

sealed interface HomeNavigationEvent {

    data object OpenTransactions : HomeNavigationEvent

    data object OpenBorrowing : HomeNavigationEvent

    data object OpenAI : HomeNavigationEvent

    data object AddTransaction : HomeNavigationEvent
}