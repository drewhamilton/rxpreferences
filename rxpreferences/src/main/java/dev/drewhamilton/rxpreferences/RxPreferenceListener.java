package dev.drewhamilton.rxpreferences;

import android.content.SharedPreferences;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;

abstract class RxPreferenceListener<T> implements SharedPreferences.OnSharedPreferenceChangeListener {

    final String key;
    private final ObservableEmitter<T> emitter;

    RxPreferenceListener(@NonNull String key, @NonNull ObservableEmitter<T> emitter) {
        this.key = key;
        this.emitter = emitter;
    }

    @Override
    public final void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (shouldEmit(key)) {
            try {
                emitter.onNext(getCurrentValue(preferences));
            } catch (Throwable error) {
                emitter.onError(error);
            }
        }
    }

    boolean shouldEmit(String key) {
        return this.key.equals(key);
    }

    abstract T getCurrentValue(SharedPreferences preferences);
}
