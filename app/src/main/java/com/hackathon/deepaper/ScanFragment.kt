package com.hackathon.deepaper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hackathon.deepaper.content.ContentActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_scan.*


class ScanFragment : Fragment() {

    private var mDisposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_scan, container, false)

    override fun onResume() {
        super.onResume()

        startScanner()
    }

    override fun onPause() {
        super.onPause()

        mDisposable?.dispose()
    }

    private fun startScanner() {
        mDisposable = barcodeView
            .getObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .firstOrError()
            .subscribe(
                { barcode ->
                    context!!.launchActivity<ContentActivity> {
                        putExtra(ContentActivity.CONTENT_KEY, barcode.displayValue)
                    }
                },
                { throwable ->
                    Log.e(javaClass.simpleName, "Barcode Error", throwable)
                })
    }
}