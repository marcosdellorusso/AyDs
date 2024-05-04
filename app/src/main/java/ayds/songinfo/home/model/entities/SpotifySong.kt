package ayds.songinfo.home.model.entities

import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

sealed class Song {
    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val releaseDatePrecision: String,
        val spotifyUrl: String,
        val imageUrl: String,
        var isLocallyStored: Boolean = false
    ) : Song() {}

    object EmptySong : Song()
}

