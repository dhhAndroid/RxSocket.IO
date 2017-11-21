package io.socket.rx;

import android.support.annotation.NonNull;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by dhh on 2017/11/13.
 *
 * @author dhh
 */

public class RxSocketManager {
    private static RxSocketManager instance;
    private SerializedSubject<SocketEvent, SocketEvent> socketBus;
    private Map<String, Socket> socketCacheMap;
    private Map<String, Observable<Socket>> observableCacheMap;


    private RxSocketManager() {
        socketBus = PublishSubject.<SocketEvent>create().toSerialized();
        socketCacheMap = Collections.synchronizedMap(new ConcurrentHashMap<String, Socket>());
        observableCacheMap = Collections.synchronizedMap(new ConcurrentHashMap<String, Observable<Socket>>());
    }

    public static RxSocketManager getInstance() {
        if (instance == null) {
            synchronized (RxSocketManager.class) {
                if (instance == null) {
                    instance = new RxSocketManager();
                }
            }
        }
        return instance;
    }

    public void postEvent(SocketEvent socketEvent) {
        socketBus.onNext(socketEvent);
    }

    private Observable<Socket> getSocketObservable(String url) {
        return getSocketObservable(url, null);
    }

    private Observable<Socket> getSocketObservable(String url, IO.Options opts) {
        Observable<Socket> observable = observableCacheMap.get(url);
        if (observable == null) {
            observable = Observable
                    .create(new SocketIoOnSubscribe(url, opts))
                    .share()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            observableCacheMap.put(url, observable);
        } else {
            Socket socket = socketCacheMap.get(url);
            if (socket != null) {
                observable = observable.startWith(socket);
            }
        }
        return observable;
    }

    public Observable<Object> toObservable(@NonNull final String url, @NonNull final String event) {
        return toObservable(url, null, event);
    }

    public Observable<Object> toObservable(@NonNull final String url, IO.Options opts, @NonNull final String event) {
        return getSocketObservable(url, opts)
                .flatMap(new Func1<Socket, Observable<SocketEvent>>() {
                    @Override
                    public Observable<SocketEvent> call(Socket socket) {
                        return socketBus.asObservable();
                    }
                })
                .filter(new Func1<SocketEvent, Boolean>() {
                    @Override
                    public Boolean call(SocketEvent socketEvent) {
                        return url.equals(socketEvent.getUrl()) && event.equals(socketEvent.getEvent());
                    }
                })
                .map(new Func1<SocketEvent, Object>() {
                    @Override
                    public Object call(SocketEvent socketEvent) {
                        return socketEvent.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SocketEvent> toObservable(String url, @NonNull final String... events) {
        return toObservable(url, null, events);
    }

    public Observable<SocketEvent> toObservable(String url, IO.Options opts, @NonNull final String... events) {
        final List<String> list = Arrays.asList(events);
        return getSocketObservable(url, opts)
                .flatMap(new Func1<Socket, Observable<SocketEvent>>() {
                    @Override
                    public Observable<SocketEvent> call(Socket socket) {
                        return socketBus.asObservable();
                    }
                })
                .filter(new Func1<SocketEvent, Boolean>() {
                    @Override
                    public Boolean call(SocketEvent socketEvent) {
                        return list.contains(socketEvent.getEvent());
                    }
                })
                .startWith(new SocketEvent(url, Socket.EVENT_CONNECT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Object> getDataOnce(final String url, final String event) {
        return toObservable(url, event)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        asyncEmit(url, event);
                    }
                })
                .first();
    }

    public Observable<Object> getDataOnceWithArgs(final String url, final String event, final Object... args) {
        return toObservable(url, event)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        asyncEmit(url, event, args);
                    }
                })
                .first();
    }

    public Observable<SocketEvent> getDataOnce(final String url, final String... events) {
        return toObservable(url, events)
                .distinct(new Func1<SocketEvent, String>() {
                    @Override
                    public String call(SocketEvent socketEvent) {
                        return socketEvent.getEvent();
                    }
                })
                .take(events.length)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        asyncEmit(url, events);
                    }
                });
    }

    public void emit(String url, String event) {
        Socket socket = socketCacheMap.get(url);
        if (socket != null) {
            socket.emit(event);
        } else {
            throw new IllegalStateException("The sokcet is not connected !");
        }
    }

    public void emit(String url, String event, Object... args) {
        Socket socket = socketCacheMap.get(url);
        if (socket != null) {
            socket.emit(event, args);
        } else {
            throw new IllegalStateException("The sokcet is not connected !");
        }
    }

    public void emit(String url, String event, Object[] args, Ack ack) {
        Socket socket = socketCacheMap.get(url);
        if (socket != null) {
            socket.emit(event, args, ack);
        } else {
            throw new IllegalStateException("The sokcet is not connected !");
        }
    }

    public void send(String url, Object... args) {
        Socket socket = socketCacheMap.get(url);
        if (socket != null) {
            socket.send(args);
        } else {
            throw new IllegalStateException("The sokcet is not connected !");
        }
    }

    public void asyncEmit(String url, final String event) {
        getSocketObservable(url, null)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Socket, Emitter>() {
                    @Override
                    public Emitter call(Socket socket) {
                        return socket.emit(event);
                    }
                })
                .subscribe();
    }

    public void asyncEmit(String url, final String event, final Object... args) {
        getSocketObservable(url, null)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Socket, Emitter>() {
                    @Override
                    public Emitter call(Socket socket) {
                        return socket.emit(event, args);
                    }
                })
                .subscribe();
    }

    public void asyncEmit(String url, final String... events) {
        getSocketObservable(url)
                .flatMapIterable(new Func1<Socket, Iterable<String>>() {
                    @Override
                    public Iterable<String> call(Socket socket) {
                        return Arrays.asList(events);
                    }
                }, new Func2<Socket, String, Emitter>() {
                    @Override
                    public Emitter call(Socket socket, String s) {
                        return socket.emit(s);
                    }
                })
                .last()
                .subscribe();
    }

    private final class SocketIoOnSubscribe implements Observable.OnSubscribe<Socket> {

        private String url;
        private IO.Options opts;
        private Socket socket;

        public SocketIoOnSubscribe(String url, IO.Options opts) {
            this.url = url;
            this.opts = opts;
        }

        @Override
        public void call(final Subscriber<? super Socket> subscriber) {
            try {
                socket = IO.socket(url, opts);
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(socket);
                        }
                        socketCacheMap.put(url, socket);
                        socket.off(Socket.EVENT_CONNECT, this);
                    }
                });
                //不自动重连
                if (opts != null && !opts.reconnection) {
                    socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onError((Throwable) args[0]);
                            }
                            socket.off(Socket.EVENT_ERROR, this);
                        }
                    });
                }
                socket.connect();
                subscriber.add(new Subscription() {
                    @Override
                    public void unsubscribe() {
                        socket.close();
                        socketCacheMap.remove(url);
                        observableCacheMap.remove(url);
                    }

                    @Override
                    public boolean isUnsubscribed() {
                        return !socket.connected();
                    }
                });
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
