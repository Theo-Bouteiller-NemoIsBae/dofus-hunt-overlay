package shared.simpleObservable

import ui.splashscreen.step.SplashScreenStep

class SimpleObservable<T> {
    private var value: T? = null
    private var callback: ((T?)->(Unit))? = null

    fun setOnChangeListener(callback: (T?)->(Unit)) {
        this.callback = callback
    }

    fun removeOnChangeListener() {
        this.callback = null
    }

    fun setValue(value: T) {
        this.value = value
        updateListener()
    }

    private fun updateListener() {
        if (null != callback) {
            callback!!(value)
        }
    }
}