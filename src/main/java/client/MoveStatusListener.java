package client;

import piece.Piece;

public interface MoveStatusListener {
    public void onMove(Piece.Direction direction);
}
