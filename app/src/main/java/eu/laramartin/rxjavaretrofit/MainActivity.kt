package eu.laramartin.rxjavaretrofit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textChangesObservable = RxTextView.textChanges(edit)
        disposable = textChangesObservable.subscribe({
            Log.v(TAG, "textview: $it")
        })

        val service = service()
        service.search("android")
                .subscribeOn(Schedulers.io())
                .subscribe({ result : BookSearchResult ->
            Log.v(TAG, "google book service $result")
        }, {t: Throwable -> Log.e(TAG, "google book service error $t", t)})
    }

    private fun service(): GoogleBooksService {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create<GoogleBooksService>(GoogleBooksService::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}

interface GoogleBooksService {
    @GET("books/v1/volumes")
    fun search(@Query("q") search: String): Single<BookSearchResult>
}

data class BookSearchResult(val items: List<Book>)

data class Book(val volumeInfo : BookVolumeInfo)

data class BookVolumeInfo(val title: String)

