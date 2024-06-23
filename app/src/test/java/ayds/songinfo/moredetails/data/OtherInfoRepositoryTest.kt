package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OtherInfoRepositoryTest {

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk()
    private val otherInfoBroker: OtherInfoBroker = mockk()
    private val otherInfoRepository: OtherInfoRepository =
        OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoBroker)

    @Test
    fun `on getCard call getCards from local storage`() {
        val cards = listOf(
            Card("artistName", "biography from LastFM", "url1", "logoUrl1", CardSource.LAST_FM),
            Card("artistName", "biography from Wikipedia", "url2", "logoUrl2", CardSource.WIKIPEDIA),
            Card("artistName", "biography from NYTimes", "url3", "logoUrl3", CardSource.NY_TIMES)
        )
        every { otherInfoLocalStorage.getCard("artistName") } returns cards

        val result = otherInfoRepository.getCard("artistName")

        assertEquals(cards, result)
        assertTrue(result.all { it.isLocallyStored })
    }

    @Test
    fun `on getCard call getCards from broker`() {
        val cards = listOf(
            Card("artistName", "biography from LastFM", "url1", "logoUrl1", CardSource.LAST_FM),
            Card("artistName", "biography from Wikipedia", "url2", "logoUrl2", CardSource.WIKIPEDIA),
            Card("artistName", "biography from NYTimes", "url3", "logoUrl3", CardSource.NY_TIMES)
        )
        every { otherInfoLocalStorage.getCard("artistName") } returns emptyList()
        every { otherInfoBroker.getCards("artistName") } returns cards
        every { otherInfoLocalStorage.insertCard(any()) } returns Unit

        val result = otherInfoRepository.getCard("artistName")

        assertEquals(cards, result)
        assertFalse(result.all { it.isLocallyStored })
        verify { otherInfoLocalStorage.insertCard(any()) }
    }

    @Test
    fun `on empty bio, getCard call getCards from broker`() {
        val cards = listOf(
            Card("artistName", "", "url", "logoUrl", CardSource.LAST_FM)
        )
        every { otherInfoLocalStorage.getCard("artistName") } returns emptyList()
        every { otherInfoBroker.getCards("artistName") } returns cards
        every { otherInfoLocalStorage.insertCard(any()) } returns Unit

        val result = otherInfoRepository.getCard("artistName")

        assertEquals(cards, result)
        assertFalse(result.all { it.isLocallyStored })
        verify { otherInfoLocalStorage.insertCard(any()) }
    }
}
