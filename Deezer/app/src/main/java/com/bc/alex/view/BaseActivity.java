package com.bc.alex.view;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by alex on 20/06/17.
 */

public class BaseActivity extends RxAppCompatActivity {

    protected CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
