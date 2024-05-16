/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player;

import game.BoardHelper;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author gabri
 */

class Node2 {
    protected int[][] board;
    protected int jugador;
    protected boolean finAl;

    public Node2(int[][] board, int playerToMove) {
        this.board = board;
        this.jugador = playerToMove;
        if (BoardHelper.isGameFinished(board))this.finAl=true;
        else this.finAl=false;

    }
    /**
     * Funcion generadora de nodos hijos
     * @return Lista de nodos hijos
     */
    public ArrayList<Node2> generateChildren() {
        ArrayList<Node2> children = new ArrayList<>();
        for (Point move : BoardHelper.getAllPossibleMoves(board, jugador)) {
            int[][] newBoard = BoardHelper.getNewBoardAfterMove(board, move, jugador);
            children.add(new Node2(newBoard, 3 - jugador)); // Cambio de jugador
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
public class AlfaBetaPlayer extends GamePlayer {
    private Point best_move;

    public AlfaBetaPlayer (int mark, int depth){
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
        return "AlfaBetaCPU";
    }

    @Override
    public Point play(int[][] board) {
        ArrayList<Point> myPossibleMoves = BoardHelper.getAllPossibleMoves(board,myMark);
        Node2 Primero = new Node2(board,myMark);
        int num_move=0;
        int minimo=Integer.MAX_VALUE;
        if(myPossibleMoves.size() > 0){
            for (int i=0;i<myPossibleMoves.size();i++){
                int valor=alfa_beta(Primero, depth, (int)Double.NEGATIVE_INFINITY, (int)Double.POSITIVE_INFINITY,false);
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

    public int alfa_beta(Node2 nodo,int depth,int alfa,int beta,boolean maximizo ){
        if(depth==0||nodo.isfinal()){
            int to_ret=nodo.evaluacion(myMark);
            return to_ret;
        }

        if(maximizo){
            ArrayList<Node2> hijos = nodo.generateChildren();
            for (int i=0;i<hijos.size()-1;i++){
                alfa = Math.max(alfa, alfa_beta(hijos.get(i),depth-1,alfa,beta,false));
                if(alfa>=beta) break;
            }

            return alfa;
        }
        else{//Minimizando
            ArrayList<Node2> hijos = nodo.generateChildren();
            for (int i=0;i<hijos.size()-1;i++){
                beta = Math.min(beta, alfa_beta(nodo,depth-1,alfa,beta,true));
            }
            return beta;
        }

    }
}