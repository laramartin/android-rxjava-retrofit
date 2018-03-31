package eu.laramartin.rxjavaretrofit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var disposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textChangesObservable = RxTextView.textChanges(edit)
        disposable = textChangesObservable.subscribe({
            Log.v(TAG, "textview: $it")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
