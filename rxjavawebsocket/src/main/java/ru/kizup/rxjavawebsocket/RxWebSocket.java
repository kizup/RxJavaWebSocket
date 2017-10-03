package ru.kizup.rxjavawebsocket;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by dpuzikov on 03.10.17.
 */

public class RxWebSocket {

    private static RxWebSocket instance = null;
    private WebSocket mWebSocket;
    private String mHost;
    private int mPort;
    private String mPassword;
    private WebSocketListener mWebSocketListener;
    private CompositeDisposable mCompositeDisposable;

    public static RxWebSocket getInstance() {
        if (instance == null) {
            instance = new RxWebSocket();
        }
        return instance;
    }

    private RxWebSocket() {
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public RxWebSocket init(String host, int port) {
        if ((mHost != null &&  !mHost.isEmpty()) || mPort > 0) {
            throw new IllegalStateException("WebSocket params already defined");
        }
        this.mHost = host;
        this.mPort = port;
        this.mCompositeDisposable = new CompositeDisposable();
        return this;
    }

    public void sendMessage(String text) {
        if (mWebSocket != null) {
            mWebSocket.send(text);
        }
    }

    public void disconnect() {
        mCompositeDisposable.dispose();
    }

    public Flowable<WebSocketEvent> getWebSocketFlowable() {
        return Flowable.create(e -> {
            if (mWebSocketListener == null) {
                mWebSocketListener = new WebSocketListenerImpl(e);
            }

            Disposable disposable = new Disposable() {
                boolean disposed = false;
                int code = 1000;
                String reason = "finished";

                @Override
                public void dispose() {
//                    disposed = mWebSocket.close(code, reason);
//                    e.onNext(WebSocketEvent.onWebSockedClosed(code, reason));
                }

                @Override
                public boolean isDisposed() {
                    return disposed;
                }
            };

            mCompositeDisposable.add(disposable);
            if (mWebSocket == null) {
                Request request = new Request.Builder().url("ws:" + mHost + ":" + mPort + "/" + mPassword).build();
                mWebSocket = new OkHttpClient().newWebSocket(request, mWebSocketListener);
            }
        }, BackpressureStrategy.BUFFER);
    }

}
