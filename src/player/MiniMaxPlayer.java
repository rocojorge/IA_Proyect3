/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player;

import game.*;

import java.awt.*;
import java.util.*;

/**
 *
 * @author gabri
 */

class Node {
    protected int[][] board;
    protected int jugador;
    protected boolean finAl;

    public Node(int[][] board, int playerToMove) {
        this.board = board;
        this.jugador = playerToMove;
        if (BoardHelper.isGameFinished(board))this.finAl=true;
        else this.finAl=false;

    }
    /**
     * Funcion generadora de nodos hijos
     * @return Lista de nodos hijos
     */
    public ArrayList<Node> generateChildren() {
        ArrayList<Node> children = new ArrayList<>();
        for (Point move : BoardHelper.getAllPossibleMoves(board, jugador)) {
            int[][] newBoard = BoardHelper.getNewBoardAfterMove(board, move, jugador);
            children.add(new Node(newBoard, 3 - jugador)); // Cambio de jugador
        }
        return children;
    }
    /**
     * Funcion que devuelve el estado de la partida
     * @return Estado de la partida
     */
    public boolean isfinal(){
        return this.finAl;
    }

    public int evaluacion(int jugador_){
        int fichas = BoardHelper.getPlayerStoneCount(board, jugador_);
        return fichas;
    }
}
public class MiniMaxPlayer extends GamePlayer {
    private Point best_move;

    public MiniMaxPlayer (int mark, int depth){
        super(mark, depth);
        best_move=null;
    }

    public int getMyMark(){
        return myMark;
    }
    @Override
    public boolean isUserPlayer() {
        return false;
    }

    @Override
    public String playerName() {
        return "MinMaxCPU";
    }

    @Override
    public Point play(int[][] board) {
        ArrayList<Point> myPossibleMoves = BoardHelper.getAllPossibleMoves(board,myMark);
        Node Primero = new Node(board,myMark);
        int num_move=0;
        int minimo=Integer.MAX_VALUE;
        if(myPossibleMoves.size() > 0){
            for (int i=0;i<myPossibleMoves.size();i++){
                int valor=min_max(Primero,depth,false);
                if(minimo<valor){
                    minimo=valor;
                    num_move=i;
                }
            }
            return myPossibleMoves.get(num_move);
        }else{
            return null;
        }
    }

    //Nueva Función que funciona
    public int min_max(Node nodo, int depth, boolean maximizo ){
        if(depth==0||nodo.isfinal()){
            int to_ret=nodo.evaluacion(myMark)-nodo.evaluacion(3-myMark);
            return to_ret;
        }

        if(maximizo){
            int valor=(int)Double.NEGATIVE_INFINITY;
            ArrayList<Node> hijos = nodo.generateChildren();
            for (int i=0;i<hijos.size()-1;i++){
                valor = Math.max(valor, min_max(hijos.get(i),depth-1,false));
            }
            return valor;
        }
        else{//Minimizando
            int valor=(int)Double.POSITIVE_INFINITY;
            ArrayList<Node> hijos = nodo.generateChildren();
            for (int i=0;i<hijos.size()-1;i++){
                valor = Math.min(valor, min_max(nodo,depth-1,true));
            }
            return valor;
        }

    }

    public int evaluacion(Point movimiento,int [][] board){
        int [][] newBoard = BoardHelper.getNewBoardAfterMove(board,movimiento,this.myMark);
        int points = BoardHelper.getPlayerStoneCount(newBoard, this.myMark);

        return points;
    }


}