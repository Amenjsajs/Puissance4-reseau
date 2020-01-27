package client;

import query.Query;

public interface SendListener {
    void onSend(Query query);
}
