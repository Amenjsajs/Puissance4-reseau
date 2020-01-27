package query;

import piece.Piece;

import java.io.Serializable;

public class Query implements Serializable {
    private static final long serialVersionUID = 7686357021438259870L;
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String LOGIN_OK = "login_ok";
    public static final String LOGIN_FAILED = "login_failed";
    public static final String LOGOUT_OK = "logout_ok";
    public static final String MOVE_TO_LEFT = "move_to_left";
    public static final String MOVE_TO_RIGHT = "move_to_right";
    public static final String MOVE_TO_BOTTOM = "move_to_bottom";
    public static final String MOVE_PIECE = "move_piece";
    public static final String RECEIVE_MOVE_PIECE = "receive_move_piece";
    public static final String SEND = "sendMove";
    public static final String RECEIVE = "receive";
    public static final String REQUEST = "request";
    public static final String RECEIVE_REQUEST = "receive_request";
    public static final String ACCEPT_REQUEST = "accept_request";
    public static final String RECEIVE_ACCEPT_REQUEST = "receive_accept_request";
    public static final String DECLINE_REQUEST = "decline_request";
    public static final String RECEIVE_DECLINE_REQUEST = "receive_decline_request";

    private String senderLogin;
    private String receiverLogin;
    private String cmd;
    private Piece.Direction direction;

    public Piece.Direction getDirection() {
        return direction;
    }

    public void setDirection(Piece.Direction direction) {
        this.direction = direction;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public String getReceiverLogin() {
        return receiverLogin;
    }

    public void setReceiverLogin(String receiverLogin) {
        this.receiverLogin = receiverLogin;
    }
}
