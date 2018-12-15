package drewhamilton.rxpreferences

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class RxPreferencesExtensionsTest {

  @Mock private lateinit var mockSharedPreferences: SharedPreferences
  @Mock private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

  @InjectMocks private lateinit var rxPreferences: RxPreferences

  private lateinit var testScheduler: TestScheduler

  private val disposable = CompositeDisposable()

  @Before
  fun setUp() {
    whenever(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
    whenever(mockSharedPreferencesEditor.commit()).thenReturn(true)

    testScheduler = TestScheduler()
  }

  @After
  fun tearDown() {
    disposable.clear()
  }

  @Test
  fun edit_commitsAllChangesInOrder() {
    val testStringKey = "Test string key"
    val testStringValue = "Test string value"
    val testIntKey = "Test int key"
    val testIntValue = 8348

    rxPreferences.edit {
      putString(testStringKey, testStringValue)
      putInt(testIntKey, testIntValue)
    }.subscribeOn(testScheduler)
        .subscribe()
        .trackUntilTearDown()

    val editingOrder = inOrder(mockSharedPreferencesEditor)

    verify(mockSharedPreferences).edit()
    verifyNoMoreInteractions(mockSharedPreferences)
    editingOrder.verify(mockSharedPreferencesEditor).putString(testStringKey, testStringValue)
    editingOrder.verify(mockSharedPreferencesEditor).putInt(testIntKey, testIntValue)
    editingOrder.verifyNoMoreInteractions()

    testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS)
    verifyNoMoreInteractions(mockSharedPreferences)
    editingOrder.verify(mockSharedPreferencesEditor).commit()
    editingOrder.verifyNoMoreInteractions()
  }

  private fun Disposable.trackUntilTearDown() = disposable.add(this)
}