package global;

import piece.Piece;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Logic {
    private static Point coord;
    private static Piece[][] gridColumn;
    private static List<Piece> pieces;
    private static Piece piece;//La piece qui vient d'être jouée

    /**
     * @param piece      qui vient d'être jouée
     * @param gridColumn Grille de toutes les pieces jouées
     * @return La liste des pieces gagnantes
     */
    public static List<Piece> getWinnerPieces(Piece piece, Piece[][] gridColumn) {
        List<Piece> pieces = new ArrayList<>();

        List<Piece> horizontal = horizontal(piece, gridColumn);
        List<Piece> vertical = vertical(piece, gridColumn);
        List<Piece> oblique1 = oblique1(piece, gridColumn);
        List<Piece> oblique2 = oblique2(piece, gridColumn);

        if (horizontal.size() >= 4) pieces.addAll(horizontal);
        if (vertical.size() >= 4) pieces.addAll(vertical);
        if (oblique1.size() >= 4) pieces.addAll(oblique1);
        if (oblique2.size() >= 4) pieces.addAll(oblique2);

        return pieces;
    }

    /**
     * @param indexLine index de la ligne dans @gridColumn
     * @return
     */
    private static boolean notPopulateHorizontal(int indexLine) {
        Piece neighbour = gridColumn[coord.x][indexLine];

        if (neighbour == null || neighbour.getColor() != piece.getColor()) return true;
        pieces.add(neighbour);
        return false;
    }

    /**
     * @param piece La pièce dont on veut connaitre les coordonnées
     * @return Les coordonnées de la pièce passée en paramètre
     */
    private static Point getCoordinates(Piece piece) {
        int x = Config.Grid.ROW_COUNT - (piece.getY() / Config.Grid.CELL_SIZE);
        int y = piece.getX() / Config.Grid.CELL_SIZE;
        return new Point(x, y);
    }

    /**
     * @param piece      Piece qui vient d'être jouée
     * @param gridColumn Grille de toutes les pieces jouées
     * @return La liste des pièces adjacentes de même type sur la ligne horizontale
     */
    private static List<Piece> horizontal(Piece piece, Piece[][] gridColumn) {
        Logic.gridColumn = gridColumn;
        Logic.piece = piece;
        pieces = new ArrayList<>();
        coord = getCoordinates(piece);

        pieces.add(piece);
        //left direction
        for (int i = coord.y - 1; i >= 0; i--) {
            if (notPopulateHorizontal(i)) break;
        }

        //right direction
        for (int i = coord.y + 1; i < Config.Grid.COLUMN_COUNT; i++) {
            if (notPopulateHorizontal(i)) break;
        }
        return pieces;
    }

    /**
     * @param piece      Piece qui vient d'être jouée
     * @param gridColumn Grille de toutes les pieces jouées
     * @return La liste des pièces adjacentes de même type sur la ligne verticale
     */
    private static List<Piece> vertical(Piece piece, Piece[][] gridColumn) {
        pieces = new ArrayList<>();
        coord = getCoordinates(piece);
        pieces.add(piece);
        Piece neighbour;
        for (int i = coord.x - 1; i >= 0; i--) {
            neighbour = gridColumn[i][coord.y];
            if (neighbour.getColor() != piece.getColor()) break;
            pieces.add(neighbour);
        }
        return pieces;
    }

    /**
     * @param piece      Piece qui vient d'être jouée
     * @param gridColumn Grille de toutes les pieces jouées
     * @return La liste des pièces adjacentes de même type sur la ligne oblique 1 "/"
     */
    private static List<Piece> oblique1(Piece piece, Piece[][] gridColumn) {
        pieces = new ArrayList<>();
        coord = getCoordinates(piece);
        pieces.add(piece);
        Piece neighbour;

        //Top
        int indexLine = coord.x + 1;
        int indexColumn = coord.y + 1;
        while (indexLine < Config.Grid.ROW_COUNT && indexColumn < Config.Grid.COLUMN_COUNT) {
            neighbour = gridColumn[indexLine++][indexColumn++];
            if (neighbour == null || neighbour.getColor() != piece.getColor()) break;
            pieces.add(neighbour);
        }

        //Bottom
        indexLine = coord.x;
        indexColumn = coord.y;
        while (indexLine > 0 && indexColumn > 0) {
            neighbour = gridColumn[--indexLine][--indexColumn];
            if (neighbour == null || neighbour.getColor() != piece.getColor()) break;
            pieces.add(neighbour);
        }

        return pieces;
    }

    /**
     * @param piece      Piece qui vient d'être jouée
     * @param gridColumn Grille de toutes les pieces jouées
     * @return La liste des pièces adjacentes de même type sur la ligne oblique 2 "\"
     */
    private static List<Piece> oblique2(Piece piece, Piece[][] gridColumn) {
        pieces = new ArrayList<>();
        coord = getCoordinates(piece);
        pieces.add(piece);
        Piece neighbour;

        //Top
        int indexLine = coord.x + 1;
        int indexColumn = coord.y - 1;
        while (indexLine < Config.Grid.ROW_COUNT && indexColumn > 0) {
            neighbour = gridColumn[indexLine][indexColumn];
            if (neighbour == null || neighbour.getColor() != piece.getColor()) break;
            pieces.add(neighbour);
            indexLine++;
            indexColumn--;
        }

        //Bottom
        indexLine = coord.x - 1;
        indexColumn = coord.y + 1;
        while (indexLine >= 0 && indexColumn <= Config.Grid.COLUMN_COUNT - 1) {
            neighbour = gridColumn[indexLine][indexColumn];
            System.out.println("Bline: " + indexLine + " column: " + indexColumn);
            if (neighbour == null || neighbour.getColor() != piece.getColor()) break;
            pieces.add(neighbour);
            indexLine--;
            indexColumn++;
        }

        return pieces;
    }
}
