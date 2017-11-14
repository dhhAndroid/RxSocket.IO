package io.socket.rx;

/**
 * Created by dhh on 2017/11/13.
 */

public class SocketEvent {
    private String url;
    private String event;
    private Object data;

    public SocketEvent(String url, String event) {
        this.url = url;
        this.event = event;
    }

    public SocketEvent(String url, String event, Object data) {
        this.url = url;
        this.event = event;
        this.data = data;
    }

    public SocketEvent(String event, Object data) {
        this.event = event;
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SocketEvent{" +
                "url='" + url + '\'' +
                ", event='" + event + '\'' +
                ", data=" + data +
                '}';
    }
}
