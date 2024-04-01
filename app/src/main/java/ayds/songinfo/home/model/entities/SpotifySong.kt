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
    ) : Song() {

        //Tal vez tendria que guardar un String en date
        //Tal vez hacer una funcion para esto
        val date: String = when (releaseDatePrecision) {
            "day" -> {
                val parsedDate = LocalDate.parse(releaseDate)
                parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
            "month" -> {
                val parsedDate = YearMonth.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy-MM"))
                parsedDate.format(DateTimeFormatter.ofPattern("MMMM, yyyy"))
            }
            "year" -> {
                val parsedYear = releaseDate.toIntOrNull() ?: throw IllegalArgumentException("Invalid year format: $releaseDate")
                "$parsedYear${if (!Year.isLeap(parsedYear.toLong())) " (not a leap year)" else ""}"
            }
            else -> throw IllegalArgumentException("Unknown precision: $releaseDatePrecision")
        }
    }

    object EmptySong : Song()
}

