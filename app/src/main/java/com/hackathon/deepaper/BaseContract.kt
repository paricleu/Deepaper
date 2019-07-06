package com.hackathon.deepaper

class BaseContract {

    interface BaseView

    interface BasePresenter<T : BaseView> {
        fun attachView(view: T)

        fun detachView()

        fun isAttached(): Boolean
    }
}