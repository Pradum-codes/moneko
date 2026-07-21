package com.pradumcodes.moneko.navigation

import kotlinx.serialization.Serializable

sealed interface Destination

@Serializable
data object Home : Destination

@Serializable
data object Transaction : Destination

@Serializable
data object Loan : Destination

@Serializable
data object Report : Destination

@Serializable
data object AddTransaction : Destination