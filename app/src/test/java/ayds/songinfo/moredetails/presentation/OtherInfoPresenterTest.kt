package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val otherInfoRepository: OtherInfoRepository = mockk()
    private val cardDescriptionHelper: CardDescriptionHelperImpl = mockk()
    private val otherInfoPresenter: OtherInfoPresenter =
        OtherInfoPresenterImpl(otherInfoRepository, cardDescriptionHelper)

    @Test
    fun `getArtistInfo should return artist biography ui state`() {
        val cards = listOf(
            Card("artistName", "biography from LastFM", "url1", "logoUrl1", CardSource.LAST_FM),
            Card("artistName", "biography from Wikipedia", "url2", "logoUrl2", CardSource.WIKIPEDIA),
            Card("artistName", "biography from NYTimes", "url3", "logoUrl3", CardSource.NY_TIMES)
        )
        every { otherInfoRepository.getCard("artistName") } returns cards
        cards.forEach { card ->
            every { cardDescriptionHelper.getDescription(card) } returns "${card.text} description"
        }
        val artistBiographyTester: (CardsUiState) -> Unit = mockk(relaxed = true)

        otherInfoPresenter.cardObservable.subscribe(artistBiographyTester)
        otherInfoPresenter.updateCard("artistName")

        val expectedCardUiStates = cards.map { card ->
            CardUiState(card.artistName, "${card.text} description", card.url, card.logoUrl)
        }

        val expectedCardsUiState = CardsUiState(expectedCardUiStates)

        verify { artistBiographyTester(expectedCardsUiState) }

    }

}

