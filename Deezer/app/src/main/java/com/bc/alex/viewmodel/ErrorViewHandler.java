package com.bc.alex.viewmodel;

import com.bc.alex.viewmodel.exception.NoNetworkException;

import java.io.IOException;

/**
 * Created by alex on 20/06/17.
 */

public class ErrorViewHandler {


    public final static int ERROR_NO_NETWORK = 1;
    public final static int ERROR_SERVER_ERROR = 2;
    public final static int ERROR_EMPTY = 3;

    int errorCode;

    public ErrorViewHandler(Throwable throwable) {
        if (throwable instanceof IOException){
            errorCode = ERROR_SERVER_ERROR;
        } else if (throwable instanceof NoNetworkException){
            errorCode = ERROR_NO_NETWORK;
        }
    }

    public ErrorViewHandler(int errorCode) {
       this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }
}
