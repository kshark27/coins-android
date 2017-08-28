import com.bubelov.coins.repository.place.PlacesRepository
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

/**
 * @author Igor Bubelov
 */

class PlacesRepositoryTest : BaseRobolectricTest() {
    @Inject lateinit var repository: PlacesRepository

    @Before
    fun init() {
        TestInjector.testComponent.inject(this)
    }

    @Test
    fun isNotEmptyByDefault() {
        assertTrue(repository.getPlaces("").isNotEmpty())
    }

    @Test
    fun returnsRandomPlace() {
        assertNotNull(repository.getRandomPlace())
    }

    @Test
    fun fetchesNewPlaces() {
        val newPlaces = repository.fetchNewPlaces()
        assertTrue(newPlaces.isNotEmpty())
    }
}