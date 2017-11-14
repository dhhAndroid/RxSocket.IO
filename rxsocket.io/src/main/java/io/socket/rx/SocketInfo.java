package io.socket.rx;

import io.socket.client.Socket;

/**
 * Created by dhh on 2017/11/13.
 *
 * @author dhh
 */

public class SocketInfo {
    private Socket mSocket;
    private String event;
    private Object[] msg;

    public SocketInfo(Socket socket, String event) {
        mSocket = socket;
        this.event = event;
    }

    public SocketInfo(Socket socket, String event, Object... msg) {
        mSocket = socket;
        this.event = event;
        this.msg = msg;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void setSocket(Socket socket) {
        mSocket = socket;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object[] getMsg() {
        return msg;
    }

    public void setMsg(Object... msg) {
        this.msg = msg;
    }
}
