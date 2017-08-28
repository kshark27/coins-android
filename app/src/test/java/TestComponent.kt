import android.content.Context
import com.bubelov.coins.dagger.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * @author Igor Bubelov
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface TestComponent {
    fun inject(target: CurrenciesRepositoryTest)
    fun inject(target: ExchangeRatesRepositoryTest)
    fun inject(target: NotificationAreaRepositoryTest)
    fun inject(target: PlaceCategoriesRepositoryTest)
    fun inject(target: PlacesRepositoryTest)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): TestComponent
    }
}