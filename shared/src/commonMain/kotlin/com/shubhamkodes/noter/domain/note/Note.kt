package com.shubhamkodes.noter.domain.note

import com.shubhamkodes.noter.presentation.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

data class Note(
    val id: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String,
    val content: String,
    val colorHex: Long,
    val created: LocalDateTime = Clock.System.now()
        .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
) {
    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)

        fun generateRandomColor() = colors.random()
    }
}