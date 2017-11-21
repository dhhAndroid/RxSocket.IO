package com.dhh.rxsocketio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.rx.RxSocketManager;
import io.socket.rx.SocketEvent;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    final String url = "http://remote.gs-robot.com:3001/robot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        socketIO(url);
//        RxSocketManager.getInstance().getDataOnce(url, "fetchAllRoom", Socket.EVENT_CONNECT, Socket.EVENT_PING)
//                .compose(RxLifecycle.with(this).<SocketEvent>bindToLifecycle())
//                .subscribe(new Subscriber<SocketEvent>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d("MainActivity", "onCompleted:");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(SocketEvent socketEvent) {
//                        Log.d("MainActivity", socketEvent.getEvent());
//                    }
//                });
        RxSocketManager.getInstance().getDataOnce(url, "fetchAllRoom")
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Log.d("MainActivity", "o:" + o);
                    }
                });
//        RxSocketManager.getInstance().toObservable(url, "fetchAllRoom")
//                .subscribe(new SocketIOSibscrober<List<Prodect>>() {
//                    @Override
//                    public void onSuccess(List<Prodect> data) {
//                        for (Prodect prodect : data) {
//                            Log.d("MainActivity", "prodect:" + prodect);
//                        }
//                    }
//                });
        RxSocketManager.getInstance().toObservable(url, "event1", "event2", "event3")
                .subscribe(new Action1<SocketEvent>() {
                    @Override
                    public void call(SocketEvent socketEvent) {
                        switch (socketEvent.getEvent()) {
                            case "event1":
                                Object data = socketEvent.getData();
                                break;
                            case "event2":

                                break;
                            case "event3":

                                break;
                        }
                    }
                });
//        RxSocketManager.getInstance().emit(url, "join");
    }

    public void onClick(View view) {
        RxSocketManager.getInstance().asyncEmit(url, "fetchAllRoom");

    }

    private void socketIO(final String url) {
        try {
            IO.Options opts = new IO.Options();
            opts.reconnection = true;
            final Socket socket = IO.socket(url, opts);
            Log.d("MainActivity--", "socket:" + socket);
            Emitter emitter = socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_CONNECT:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                    socketIO(url);
                }
            }).on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_CONNECTING:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_DISCONNECT:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_ERROR:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_MESSAGE:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_CONNECT_ERROR:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_CONNECT_TIMEOUT:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_RECONNECT:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_RECONNECT_ERROR:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_RECONNECT_FAILED:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_RECONNECT_ATTEMPT:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_RECONNECTING:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on(Socket.EVENT_PING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_PING:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                    socket.emit("fetchAllRoom");
                }
            }).on(Socket.EVENT_PONG, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "EVENT_PONG:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on("socket.roomsInfo", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "socket.roomsInfo:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                }
            }).on("fetchAllRoom", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MainActivity", "fetchAllRoom:");
                    if (args != null && args.length != 0) {
                        for (Object arg : args) {
                            Log.d("MainActivity", "arg:" + arg);
                        }
                    }
                    try {
                        Socket socket = IO.socket(url);
                        Log.d("MainActivity", "socket:" + socket);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
