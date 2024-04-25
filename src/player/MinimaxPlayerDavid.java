package player;

import game.BoardHelper;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author David Molina Gámez
 * @author Cristian Ojeda del Moral
 * Jugador de Othello desarrollado implementando un algoritmo minimax restringido
 */
public class MinimaxPlayerDavid extends GamePlayer {

    public MinimaxPlayerDavid(int mark, int depth) {
        super(mark, depth);
    }

    /**
     * Función sobreescrita para determinar si el jugador es o no un Usuario
     * @return False, este jugador no es usuario
     */
    @Override
    public boolean isUserPlayer() {
        return false;
    }

    /**
     * Función sobreescrita para determinar el nombre del jugador
     * @return nombre del jugador
     */
    @Override
    public String playerName() {
        return "MiniMax Player";
    }


    /**
     * Función heurística de evaluación f(n,m) = n - m;
     * @param board Estado actual del tablero de juego
     * @param player Identificador del jugador
     * @return Determina la diferencia entre el número de fichas del jugador y el número de fichas del oponente
     */
    private int evaluate(int[][] board, int player) {
        // Heuristic function F(n,m) = n - m
        int playerCount = BoardHelper.getPlayerStoneCount(board, player);
        int opponentCount = BoardHelper.getPlayerStoneCount(board, 3 - player);
        return playerCount - opponentCount;
    }

    /**
     * Función para la implementación del algoritmo Minimax
     * @param node Nodo actual del árbol de juego
     * @param depth Profundidad de búsqueda sobre el árbol de juego
     * @param isMaximizing Variable para determinar si el jugador está o no maximizando su número de fichas
     * @return El mayor valor de evaluación posible para cada nodo considerando los hijos hasta una profundidad dada.
     */
    private int minimax(Node node, int depth, boolean isMaximizing) {
        int eval;

        if (depth == 0 || BoardHelper.isGameFinished(node.board)) {
            return evaluate(node.board, node.playerToMove);
        }

        if (isMaximizing) {
            eval = Integer.MIN_VALUE;
            for (Node child: node.generateChildren()) {
                int evalMinMax = minimax(child, depth-1,true);
                eval = Math.max(evalMinMax, eval);
            }
        } else {
            eval = Integer.MAX_VALUE;
            for (Node child: node.generateChildren()) {
                int evalMinMax = minimax(child, depth-1,false);
                eval = Math.min(evalMinMax, eval);
            }
        }

        return eval;
    }

    /**
     * Función para determinar el mejor movimiento dado un nodo y una profundidad
     * @param node Nodo actual del árbol de juego
     * @param depth Profundidad de búsqueda sobre el árbol de juego
     * @return El siguiente movimiento posible optando por aquel que mejora el número de fichas del jugador
     */
    public Point findBestMove(Node node, int depth) {
        int bestEval = Integer.MIN_VALUE;
        Point bestMove = null;
        ArrayList<Point> possibleMoves = BoardHelper.getAllPossibleMoves(node.board, node.playerToMove);

        for (Point move : possibleMoves) {
            int[][] newBoard = BoardHelper.getNewBoardAfterMove(node.board, move, node.playerToMove);
            Node childNode = new Node(newBoard, 3 - node.playerToMove);
            int eval = minimax(childNode, depth - 1, false);
            if (eval > bestEval) {
                bestEval = eval;
                bestMove = move;
            }
        }

        return bestMove;
    }


    /**
     * Función sobreescrita para que el jugador juegue
     * @param board Estado actual del tablero de juego
     * @return El siguiente movimiento del jugador, dado el movimiento actual
     */
    @Override
    public Point play(int[][] board) {
        Node rootNode = new Node(board, myMark);
        return findBestMove(rootNode, depth);
    }
}


/**
 * TDA para almacenar el estado del tablero de juego y el jugador actuales
 */
class Node {
    int[][] board;
    int playerToMove;

    public Node(int[][] board, int playerToMove) {
        this.board = board;
        this.playerToMove = playerToMove;
    }

    /**
     * Función generadora de nodos hijos
     * @return Lista de nodos hijos
     */
    public ArrayList<Node> generateChildren() {
        ArrayList<Node> children = new ArrayList<>();
        for (Point move : BoardHelper.getAllPossibleMoves(board, playerToMove)) {
            int[][] newBoard = BoardHelper.getNewBoardAfterMove(board, move, playerToMove);
            children.add(new Node(newBoard, 3 - playerToMove)); // Switch player
        }
        return children;
    }
}