package ru.kizup.rxjavawebsocketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.kizup.rxjavawebsocket.RxWebSocket;
import ru.kizup.rxjavawebsocket.WebSocketEvent;

public class MainActivity extends AppCompatActivity {

    private Disposable mDisposable;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text);
        EditText editText = findViewById(R.id.input_text);

        RxWebSocket.getInstance()
                .init("185.66.84.201", 35001)
                .setPassword("12qwasZX");

        mDisposable = RxWebSocket.getInstance().getWebSocketFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWebSocketEvent, this::onError);

        findViewById(R.id.bn_send).setOnClickListener(view -> {
            String message = "{\n" +
                    "\t\"Message\": \"" + editText.getText().toString() + "\",\n" +
                    "\t\"Identifier\": 10\n" +
                    "}";
            RxWebSocket.getInstance()
                    .sendMessage(message);
        });
    }

    private void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void onWebSocketEvent(WebSocketEvent webSocketEvent) {
        switch (webSocketEvent.getEvent()) {
            case OPENED: {
                mTextView.append("WebSocket Opened");
                break;
            }
            case RECEIVED_TEXT: {
                mTextView.append(webSocketEvent.getText());
                break;
            }
            default: {
                mTextView.append("\n");
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
