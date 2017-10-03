package ru.kizup.rxjavawebsocket;

import okio.ByteString;

/**
 * Created by dpuzikov on 03.10.17.
 */

public class WebSocketEvent {

    int mCode;
    String mReason;
    String mText;
    ByteString mByteString;
    Event mEvent;

    static WebSocketEvent onWebSocketOpened() {
        WebSocketEvent webSocketEvent = new WebSocketEvent();
        webSocketEvent.mEvent = Event.OPENED;
        return webSocketEvent;
    }

    static WebSocketEvent onWebSockedClosed(int code, String reason) {
        WebSocketEvent webSocketEvent = new WebSocketEvent();
        webSocketEvent.mCode = code;
        webSocketEvent.mReason = reason;
        webSocketEvent.mEvent = Event.CLOSED;
        return webSocketEvent;
    }


    static WebSocketEvent onReceivedText(String text) {
        WebSocketEvent webSocketEvent = new WebSocketEvent();
        webSocketEvent.mText = text;
        webSocketEvent.mEvent = Event.RECEIVED_TEXT;
        return webSocketEvent;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public ByteString getByteString() {
        return mByteString;
    }

    public void setByteString(ByteString byteString) {
        mByteString = byteString;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }

    public enum Event {
        OPENED,
        CLOSED,
        FAILURE,
        RECEIVED_TEXT,
        RECEIVED_DATA
    }

}
