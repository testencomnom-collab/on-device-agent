package com.example.services

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import java.util.TimeZone

data class CalendarEvent(
    val id: Long,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val description: String?,
    val location: String?
)

class CalendarManager {
    companion object {
        private const val TAG = "CalendarManager"

        fun hasPermissions(context: Context): Boolean {
            val readPerm = context.checkSelfPermission(Manifest.permission.READ_CALENDAR)
            val writePerm = context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR)
            return readPerm == PackageManager.PERMISSION_GRANTED && writePerm == PackageManager.PERMISSION_GRANTED
        }

        fun fetchEvents(context: Context, startMillis: Long, endMillis: Long): List<CalendarEvent> {
            val eventsList = mutableListOf<CalendarEvent>()
            if (!hasPermissions(context)) {
                Log.w(TAG, "No calendar permissions available to read.")
                return eventsList
            }

            val uri: Uri = CalendarContract.Events.CONTENT_URI
            val projection = arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.EVENT_LOCATION
            )

            val selection = "(${CalendarContract.Events.DTSTART} >= ?) AND (${CalendarContract.Events.DTSTART} <= ?) AND (${CalendarContract.Events.DELETED} != 1)"
            val selectionArgs = arrayOf(startMillis.toString(), endMillis.toString())
            val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

            try {
                context.contentResolver.query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )?.use { cursor ->
                    val idCol = cursor.getColumnIndex(CalendarContract.Events._ID)
                    val titleCol = cursor.getColumnIndex(CalendarContract.Events.TITLE)
                    val startCol = cursor.getColumnIndex(CalendarContract.Events.DTSTART)
                    val endCol = cursor.getColumnIndex(CalendarContract.Events.DTEND)
                    val descCol = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)
                    val locCol = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idCol)
                        val title = cursor.getString(titleCol) ?: "Unnamed Event"
                        val start = cursor.getLong(startCol)
                        val end = cursor.getLong(endCol)
                        val desc = cursor.getString(descCol)
                        val loc = cursor.getString(locCol)

                        eventsList.add(CalendarEvent(id, title, start, end, desc, loc))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching calendar events", e)
            }

            return eventsList
        }

        fun getFirstWritableCalendarId(context: Context): Long {
            if (!hasPermissions(context)) return 1L
            val uri = CalendarContract.Calendars.CONTENT_URI
            val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL)
            
            try {
                context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                    val idCol = cursor.getColumnIndex(CalendarContract.Calendars._ID)
                    val accessCol = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL)
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idCol)
                        val access = cursor.getInt(accessCol)
                        // Make sure cal is owner or writer
                        if (access >= CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR) {
                            return id
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error finding calendar id", e)
            }
            return 1L // fallback
        }

        fun insertEvent(
            context: Context,
            title: String,
            description: String,
            startMillis: Long,
            endMillis: Long,
            location: String? = null
        ): Uri? {
            if (!hasPermissions(context)) {
                Log.w(TAG, "No calendar permissions available to write.")
                return null
            }

            val calId = getFirstWritableCalendarId(context)
            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, title)
                put(CalendarContract.Events.DESCRIPTION, description)
                put(CalendarContract.Events.CALENDAR_ID, calId)
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                if (location != null) {
                    put(CalendarContract.Events.EVENT_LOCATION, location)
                }
            }

            return try {
                context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            } catch (e: Exception) {
                Log.e(TAG, "Error writing calendar event", e)
                null
            }
        }
    }
}
