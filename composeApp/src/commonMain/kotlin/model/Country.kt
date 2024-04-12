package model

import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
data class Country(
    val name:String,
    val timeZone:TimeZone,
    val flag:DrawableResource
)
