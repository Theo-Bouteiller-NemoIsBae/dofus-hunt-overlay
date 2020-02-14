package ui.splashscreen

import com.google.gson.Gson
import extension.getFileFromResources
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shared.api.httprequest.HttpRequest
import shared.api.httprequest.result.DataVersion
import shared.api.httprequest.result.HintsData
import shared.simpleobservable.SimpleObservable
import ui.splashscreen.step.SplashScreenErrorStep
import ui.splashscreen.step.SplashScreenStep
import java.io.*
import java.util.concurrent.TimeUnit

class SplashScreenViewModel {

    val splashScreenStepObservableCallBack: SimpleObservable<SplashScreenStep> = SimpleObservable()
    val splashScreenErrorStepObservableCallBack: SimpleObservable<SplashScreenErrorStep> = SimpleObservable()
    val splashScreenFinishCallBack: SimpleObservable<SplashScreenFinishCallBack> = SimpleObservable()

    private var storeApiDataVersion: DataVersion? = null

    private var timerDisposable: Disposable? = null

    private var storedHintsData: HintsData? = null

    private var isOffline: Boolean = false

    fun startSplashScreen() {
        timer(1000) {
            splashScreenStepObservableCallBack.setValue(SplashScreenStep.CHECK_BASE_FILE)
        }
    }

    fun checkBaseFile() {
        val file: File = File("hintsData.json")
        if (file.exists()) {
            splashScreenStepObservableCallBack.setValue(SplashScreenStep.CHECK_UPDATE_BASE_FILE)

            return
        }

        splashScreenStepObservableCallBack.setValue(SplashScreenStep.WRITE_BASE_FILE)
    }

    fun writeBaseFile() {
        val fileWriter: FileWriter = FileWriter("hintsData.json")
        val hintData: HintsData = Gson().fromJson<HintsData>(
            InputStreamReader(getFileFromResources("json/defaultHints.json", this)),
            HintsData::class.java
        )
        fileWriter.write(Gson().toJson(hintData))
        fileWriter.flush()
        fileWriter.close()
        splashScreenStepObservableCallBack.setValue(SplashScreenStep.CHECK_UPDATE_BASE_FILE)
    }

    fun checkUpdateBaseFile() {
        val fileInputStream: FileInputStream = FileInputStream(File("hintsData.json"))
        val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)

        val hintData: HintsData = Gson().fromJson<HintsData>(
            inputStreamReader,
            HintsData::class.java
        )

        inputStreamReader.close()
        fileInputStream.close()

        var apiDataVersion: DataVersion? = null

        HttpRequest().getVersion {
            if (null != it.httpError) {
                splashScreenErrorStepObservableCallBack.setValue(SplashScreenErrorStep.ERROR_CHECK_UPDATE_BASE_FILE)
                return@getVersion
            }

            apiDataVersion = Gson().fromJson<DataVersion>(it.resultHints.toString(), DataVersion::class.java)
            println("apiVersion : ${apiDataVersion!!.version} | jsonVersion : ${hintData.dataVersion}")
            if (hintData.dataVersion < apiDataVersion!!.version) {
                storeApiDataVersion = apiDataVersion
                splashScreenStepObservableCallBack.setValue(SplashScreenStep.UPDATE_BASE_FILE)

                return@getVersion
            }

            splashScreenStepObservableCallBack.setValue(SplashScreenStep.FINISH)
            return@getVersion
        }
    }

    fun updateBaseFile() {
        HttpRequest().getHints {
            if (null != it.httpError) {
                splashScreenErrorStepObservableCallBack.setValue(SplashScreenErrorStep.ERROR_UPDATE_BASE_FILE)
                return@getHints
            }

            Gson().fromJson<HintsData>(it.resultHints.toString(), HintsData::class.java)?.let { hintsData ->

                hintsData.dataVersion = storeApiDataVersion?.version ?: 0
                storedHintsData = hintsData
                val fileWriter: FileWriter = FileWriter("hintsData.json")
                fileWriter.write(Gson().toJson(hintsData))
                fileWriter.flush()
                fileWriter.close()

                splashScreenStepObservableCallBack.setValue(SplashScreenStep.FINISH)
            }

            splashScreenErrorStepObservableCallBack.setValue(SplashScreenErrorStep.ERROR_UPDATE_BASE_FILE)
        }
    }

    fun finish() {
        timer(500) {
            if (null == storedHintsData) {
                val fileInputStream: FileInputStream = FileInputStream(File("hintsData.json"))
                val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)

                storedHintsData = Gson().fromJson<HintsData>(
                    inputStreamReader,
                    HintsData::class.java
                )

                inputStreamReader.close()
                fileInputStream.close()
            }

            if (null != storedHintsData) {
                splashScreenFinishCallBack.setValue(SplashScreenFinishCallBack(isOffline, storedHintsData!!))
            }
        }
    }

    fun goToOfflineMode() {
        isOffline = true
        splashScreenStepObservableCallBack.setValue(SplashScreenStep.FINISH)
    }

    private fun timer(delayInMs: Int, callback: ()->(Unit)) {
        timerDisposable = Observable.timer(delayInMs.toLong(), TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.newThread())
            .doOnSubscribe {
                println("Subscribe")
            }
            .doOnComplete {
                println("Complete")
                callback()
                if (null != timerDisposable && !timerDisposable!!.isDisposed) {
                    timerDisposable!!.dispose()
                }
            }
            .subscribe({ /* we not care */ }, { /* we not care */ })
    }
}