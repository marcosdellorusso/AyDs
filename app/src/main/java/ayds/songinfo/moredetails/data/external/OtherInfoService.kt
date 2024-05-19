import ayds.songinfo.moredetails.data.external.LastFMAPI
import ayds.songinfo.moredetails.data.external.LastFMToArtistBiographyResolver
import ayds.songinfo.moredetails.domain.ArtistBiography
import java.io.IOException

interface OtherInfoService {
    fun getArticle(artistName: String): ArtistBiography
}

internal class OtherInfoServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver
) : OtherInfoService {

    override fun getArticle(artistName: String): ArtistBiography {

        var artistBiography =
            ayds.songinfo.moredetails.domain.ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = lastFMToArtistBiographyResolver.map(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()
}