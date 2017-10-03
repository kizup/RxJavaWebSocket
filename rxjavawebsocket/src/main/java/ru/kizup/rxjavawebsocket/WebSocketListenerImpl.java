package ru.kizup.rxjavawebsocket;

import io.reactivex.FlowableEmitter;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by dpuzikov on 03.10.17.
 */

public class WebSocketListenerImpl extends WebSocketListener {

    private FlowableEmitter<WebSocketEvent> mEventFlowable;

    public WebSocketListenerImpl(FlowableEmitter<WebSocketEvent> eventFlowable) {
        mEventFlowable = eventFlowable;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        if (mEventFlowable != null) {
            mEventFlowable.onNext(WebSocketEvent.onWebSocketOpened());
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (mEventFlowable != null) {
            mEventFlowable.onNext(WebSocketEvent.onReceivedText(text));
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        if (mEventFlowable != null) {
            mEventFlowable.onNext(WebSocketEvent.onWebSockedClosed(code, reason));
            mEventFlowable.onComplete();
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (mEventFlowable != null) {
            mEventFlowable.onError(t);
        }
    }
}
