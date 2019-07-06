package com.hackathon.deepaper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<T : BaseContract.BaseView> : BaseContract.BasePresenter<T> {
    private var mView: T? = null
    private val isViewAttached: Boolean get() = mView != null

    private val mDisposables = CompositeDisposable()

    override fun attachView(view: T) {
        mView = view
    }

    override fun isAttached(): Boolean = isViewAttached

    override fun detachView() {
        mView = null
        mDisposables.clear()
    }

    fun getView(): T? {
        return mView
    }

    fun addDisposable(d: Disposable) {
        mDisposables.add(d)
    }
}