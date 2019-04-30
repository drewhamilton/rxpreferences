package drewhamilton.rxpreferences.example

import dagger.BindsInstance
import dagger.Component
import drewhamilton.rxpreferences.example.edit.EditingFragment
import drewhamilton.rxpreferences.example.observe.ObservationFragment

@Component(modules = [
  ApplicationModule::class,
  PersistenceModule::class
])
interface ApplicationComponent {

  fun inject(editingFragment: EditingFragment)

  fun inject(observationFragment: ObservationFragment)

  @Component.Factory interface Factory {
    fun create(@BindsInstance application: ExampleApplication): ApplicationComponent
  }

  companion object {
    fun create(application: ExampleApplication): ApplicationComponent {
      return DaggerApplicationComponent.factory().create(application)
    }
  }
}
