package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter


interface ReleaseDateResolverFactory{
    fun getReleaseDateResolver(song: Song.SpotifySong): ReleaseDateResolver
}

class ReleaseDateResolverFactoryImpl: ReleaseDateResolverFactory{
    override fun getReleaseDateResolver(song: Song.SpotifySong): ReleaseDateResolver =
        when (song.releaseDatePrecision) {
            "day" -> ReleaseDateDayResolver(song)
            "month" -> ReleaseDateMonthResolver(song)
            "year" -> ReleaseDateYearResolver(song)
            else -> ReleaseDateDefaultResolver(song)
        }
}

interface ReleaseDateResolver {
    val song: Song.SpotifySong
    fun getReleaseDate(): String
}

internal class ReleaseDateDayResolver(override val song: Song.SpotifySong) : ReleaseDateResolver{
    override fun getReleaseDate(): String {
        val parsedDate = LocalDate.parse(song.releaseDate)
        return parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}

internal class ReleaseDateMonthResolver(override val song: Song.SpotifySong) : ReleaseDateResolver {
    override fun getReleaseDate(): String {
        val parsedDate = YearMonth.parse(song.releaseDate, DateTimeFormatter.ofPattern("yyyy-MM"))
        return parsedDate.format(DateTimeFormatter.ofPattern("MMMM, yyyy"))
    }
}

internal class ReleaseDateYearResolver(override val song: Song.SpotifySong) : ReleaseDateResolver {
    override fun getReleaseDate(): String {
        val parsedYear = song.releaseDate.toIntOrNull() ?: throw IllegalArgumentException("Invalid year format: $song.releaseDate")
        return "$parsedYear${if (!Year.isLeap(parsedYear.toLong())) " (not a leap year)" else " (leap year)"}"
    }
}


internal class ReleaseDateDefaultResolver(override val song: Song.SpotifySong) :
    ReleaseDateResolver {
         override fun getReleaseDate() = song.releaseDate
    }