package drewhamilton.rxpreferences.dagger

import android.content.SharedPreferences
import dagger.BindsInstance
import dagger.Component
import drewhamilton.rxpreferences.RxPreferences

/**
 * A Dagger component that provides an [RxPreferences] instance for a given [SharedPreferences] instance.
 */
@Component(modules = [RxPreferencesModule::class])
interface RxPreferencesComponent {

    /**
     * Get this component's [RxPreferences] instance.
     */
    fun rxPreferences(): RxPreferences

    /**
     * A factory for this component.
     */
    @Component.Factory interface Factory {

        /**
         * Create a new [RxPreferencesComponent] using the given [sharedPreferences].
         */
        fun create(@BindsInstance sharedPreferences: SharedPreferences): RxPreferencesComponent
    }

    companion object {

        /**
         * Create a new [RxPreferencesComponent] instance using the given [sharedPreferences] using the factory
         * generated by Dagger.
         */
        @JvmStatic fun create(sharedPreferences: SharedPreferences): RxPreferencesComponent =
            DaggerRxPreferencesComponent.factory().create(sharedPreferences)
    }
}
