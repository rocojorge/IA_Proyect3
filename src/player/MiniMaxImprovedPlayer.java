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

class Node1 {
    protected int[][] board;
    protected int jugador;
    protected boolean finAl;

    public Node1(int[][] board, int playerToMove) {
        this.board = board;
        this.jugador = playerToMove;
        if (BoardHelper.isGameFinished(board))this.finAl=true;
        else this.finAl=false;

    }
    /**
     * Funcion generadora de nodos hijos
     * @return Lista de nodos hijos
     */
    public ArrayList<Node1> generateChildren() {
        ArrayList<Node1> children = new ArrayList<>();
        for (Point move : BoardHelper.getAllPossibleMoves(board, jugador)) {
            int[][] newBoard = BoardHelper.getNewBoardAfterMove(board, move, jugador);
            children.add(new Node1(newBoard, 3 - jugador)); // Cambio de jugador
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

    public int getMark(){
        return this.jugador;
    }

    public int evaluacion(int jugador_){

        int misFichas = BoardHelper.getPlayerStoneCount(board, jugador_);
        int fichasTotales = misFichas + BoardHelper.getPlayerStoneCount(board, 3-jugador_);

        int misNumMoves = BoardHelper.getAllPossibleMoves(board, jugador_).size();
        int numMovesTotales = misNumMoves + BoardHelper.getAllPossibleMoves(board, 3-jugador_).size();
        
         if(fichasTotales==0||numMovesTotales==0){
            return 10;
        }

        if(((misFichas/fichasTotales*100)>=100) && ((misNumMoves/numMovesTotales*100)>=100)){
            return 1;
        }

        if((((misFichas/fichasTotales*100)>=100) && ((misNumMoves/numMovesTotales*100)>=70)) || (((misFichas/fichasTotales*100)>=70) && ((misNumMoves/numMovesTotales*100)>=100))){
            return 2;
        }

        if(((misFichas/fichasTotales*100)>=70) && ((misNumMoves/numMovesTotales*100)>=70)){
            return 3;
        }

        if((((misFichas/fichasTotales*100)>=70) && ((misNumMoves/numMovesTotales*100)<=30)) || (((misFichas/fichasTotales*100)<=30) && ((misNumMoves/numMovesTotales*100)>=70))){
            return 4;
        }

        if(((misFichas/fichasTotales*100)>=50) && ((misNumMoves/numMovesTotales*100)>=50)){
            return 5;
        }

        if((((misFichas/fichasTotales*100)>=50) && ((misNumMoves/numMovesTotales*100)<=50)) || (((misFichas/fichasTotales*100)<=50) && ((misNumMoves/numMovesTotales*100)>=50))){
            return 6;
        }

        return 10;
    }
}
public class MiniMaxImprovedPlayer extends GamePlayer {
    private Point best_move;

    public MiniMaxImprovedPlayer (int mark, int depth){
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
        return "MinMaxImprovedCPU";
    }

    @Override
    public Point play(int[][] board) {
        ArrayList<Point> myPossibleMoves = BoardHelper.getAllPossibleMoves(board,myMark);
        Node1 Primero = new Node1(board,myMark);
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

    public int min_max(Node1 nodo, int depth, boolean maximizo ){
        if(depth==0||nodo.isfinal()){
            int to_ret=nodo.evaluacion(myMark)-nodo.evaluacion(3-myMark);
            return to_ret;
        }

        if(maximizo){
            int valor=(int)Double.NEGATIVE_INFINITY;
            ArrayList<Node1> hijos = nodo.generateChildren();
            for (int i=0;i<hijos.size()-1;i++){
                valor = Math.max(valor, min_max(hijos.get(i),depth-1,false));
            }
            return valor;
        } else {//Minimizando
            int valor=(int)Double.POSITIVE_INFINITY;
            ArrayList<Node1> hijos = nodo.generateChildren();
            for (int i=0;i<hijos.size()-1;i++){
                valor = Math.min(valor, min_max(nodo,depth-1,true));
            }
            return valor;
        }
    }
}