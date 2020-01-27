package client;

public interface RequestStatusListener {
    public void receive(String login);
    public void accept(String login);
    public void decline(String login);
}
