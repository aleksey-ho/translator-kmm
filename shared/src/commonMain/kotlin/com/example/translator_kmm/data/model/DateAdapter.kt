package com.example.translator_kmm.data.model

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

class DateAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long): Instant = Instant.fromEpochMilliseconds(databaseValue)
    override fun encode(value: Instant): Long = value.toEpochMilliseconds()
}