package com.schwarz_digits.mobilecommunity.feature.meetings.impl.domain

import com.schwarz_digits.mobilecommunity.core.model.Meeting
import kotlin.time.Instant

/**
 * Meetings grouped for display, relative to a given point in time.
 *
 * @property next The meeting that hasn't ended yet and starts first, or `null` if there is none.
 * @property upcoming All other meetings that haven't ended yet, earliest first.
 * @property past Meetings that have already ended, most recent first.
 */
data class MeetingSchedule(
    val next: Meeting?,
    val upcoming: List<Meeting>,
    val past: List<Meeting>,
)

/**
 * Groups [meetings] into a [MeetingSchedule] as seen at [now].
 *
 * A meeting counts as past once its end time is at or before [now].
 * A meeting that is currently running is still the next one.
 */
fun scheduleOf(
    meetings: List<Meeting>,
    now: Instant,
): MeetingSchedule {
    val (past, notEnded) = meetings.partition { it.endsAt <= now }
    val sorted = notEnded.sortedBy { it.startsAt }

    return MeetingSchedule(
        next = sorted.firstOrNull(),
        upcoming = sorted.drop(1),
        past = past.sortedByDescending { it.startsAt },
    )
}
