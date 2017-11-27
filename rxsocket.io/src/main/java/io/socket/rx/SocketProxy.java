package io.socket.rx;

import android.support.annotation.NonNull;

import java.util.List;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import rx.Observable;

/**
 * Created by dhh on 2017/11/27.
 */

public class SocketProxy {
    private RxSocketManager mRxSocketManager;
    private String url;

    private SocketProxy(String url) {
        this.url = url;
        mRxSocketManager = RxSocketManager.getInstance();
    }

    public static SocketProxy get(String url) {
        return new SocketProxy(url);
    }

    public Socket getSocket() {
        return mRxSocketManager.getSocket(url);
    }

    public Observable<Object> toObservable(@NonNull String event) {
        return mRxSocketManager.toObservable(url, null, event);
    }

    public Observable<Object> toObservable(IO.Options opts, @NonNull String event) {
        return mRxSocketManager.toObservable(url, opts, event);
    }

    public Observable<SocketEvent> toObservable(@NonNull String... events) {
        return mRxSocketManager.toObservable(url, null, events);
    }

    public Observable<SocketEvent> toObservable(IO.Options opts, @NonNull String... events) {
        return mRxSocketManager.toObservable(url, opts, events);
    }

    public Observable<Object> getDataOnce(String event) {
        return mRxSocketManager.getDataOnce(url, event);
    }

    public Observable<Object> getDataOnce(String emitEvent, String onEvent) {
        return mRxSocketManager.getDataOnce(url, emitEvent, onEvent);
    }

    public Observable<Object> getDataOnceWithArgs(String event, Object... args) {
        return mRxSocketManager.getDataOnceWithArgs(url, event, args);
    }

    public Observable<Object> getDataOnceWithArgs(String emitEvent, String onEvent, Object args) {
        return mRxSocketManager.getDataOnceWithArgs(url, emitEvent, onEvent, args);
    }

    public Observable<Object> getDataOnceWithArgs(String emitEvent, String onEvent, List<Object> args) {
        return mRxSocketManager.getDataOnceWithArgs(url, emitEvent, onEvent, args);
    }

    public Observable<SocketEvent> getDataOnce(String... events) {
        return mRxSocketManager.getDataOnce(url, events);
    }

    public void emit(String event) {
        mRxSocketManager.emit(url, event);
    }

    public void emit(String event, Object... args) {
        mRxSocketManager.emit(url, event, args);
    }

    public void emit(String event, Object[] args, Ack ack) {
        mRxSocketManager.emit(url, event, args, ack);
    }

    public void send(Object... args) {
        mRxSocketManager.send(url, args);
    }

    public void asyncEmit(String event) {
        mRxSocketManager.asyncEmit(url, event);
    }

    public void asyncEmit(String event, Object... args) {
        mRxSocketManager.asyncEmit(url, event, args);
    }

    public void asyncEmit(String... events) {
        mRxSocketManager.asyncEmit(url, events);
    }

}
