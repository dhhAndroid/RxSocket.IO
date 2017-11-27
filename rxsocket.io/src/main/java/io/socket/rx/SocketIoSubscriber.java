package io.socket.rx;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Subscriber;

/**
 * Created by dhh on 2017/11/14.
 */

public abstract class SocketIoSubscriber<T> extends Subscriber<Object> {
    private static final Gson GSON = new Gson();
    private Type type;

    public SocketIoSubscriber() {
        analysisType();
    }

    private void analysisType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("No generics found!");
        }
        ParameterizedType type = (ParameterizedType) superclass;
        this.type = type.getActualTypeArguments()[0];
    }

    @Override
    public final void onNext(Object obj) {
        String json = obj.toString();
        try {
            T data = GSON.fromJson(json, type);
            onSuccess(data);
        } catch (JsonSyntaxException e) {
            try {
                T data = GSON.fromJson(GSON.fromJson(json, String.class), type);
                onSuccess(data);
            } catch (JsonSyntaxException e1) {
                Log.e("SocketIoSubscriber", "type:" + type);
                throw new JsonParseException(e1);
            }
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }


    public Type getType() {
        return type;
    }

    public abstract void onSuccess(T data);
}

