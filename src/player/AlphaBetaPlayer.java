/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player;

import game.*;

import java.awt.*;
import java.util.*;
import java.math.*;

/**
 *
 * @author gabri
 */

class Node {
    protected int[][] board;
    protected int jugador;
    protected boolean finAl;
    protected int[][] board_padre;

    public Node(int[][] board, int playerToMove) {
        this.board = board;
        this.jugador = playerToMove;
        if (BoardHelper.isGameFinished(board))this.finAl=true;
        else this.finAl=false;
       
    }
    
    public Node(int[][] board, int playerToMove, int[][] board_father){
        this.board = board;
        this.jugador = playerToMove;
        if (BoardHelper.isGameFinished(board))this.finAl=true;
        else this.finAl=false;
        this.board_padre= board_father;
    }
    /**
     * Funcion generadora de nodos hijos
     * @return Lista de nodos hijos
     */
    public ArrayList<Node> generateChildren() {
        ArrayList<Node> children = new ArrayList<>();
        for (Point move : BoardHelper.getAllPossibleMoves(board, jugador)) {
            int[][] newBoard = BoardHelper.getNewBoardAfterMove(board, move, jugador);
            children.add(new Node(newBoard, 3 - jugador,this.board)); // Cambio de jugador
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
        int to_ret=0;
        int fichas = BoardHelper.getPlayerStoneCount(board, jugador_);//FIchas yo
        int fichas1= BoardHelper.getPlayerStoneCount(board_padre, 3-jugador_); //FIchas adverdario antes
        int fichas2=BoardHelper.getPlayerStoneCount(board, 3-jugador_); //Adversario
        if (fichas1<=fichas2)to_ret+=100;
        to_ret=+(fichas-fichas2)*10;
        to_ret+=fichas;
        //ArrayList<Point> movimientos_antes=BoardHelper.getAllPossibleMoves(board_padre, 3-jugador_);
        ArrayList<Point> movimientos_ahora=BoardHelper.getAllPossibleMoves(board, 3-jugador_);
        ArrayList<Point> movimientos_ahora_yo=BoardHelper.getAllPossibleMoves(board, jugador_);
        ArrayList<Point> movimientos_antes_yo=BoardHelper.getAllPossibleMoves(board_padre, jugador_);
        if(movimientos_ahora.size()==0) to_ret+=1000;
        if(movimientos_ahora_yo.size()==0) to_ret-=2000;
        to_ret=-(movimientos_antes_yo.size()-movimientos_ahora_yo.size());
        
        
        
        
        
        return to_ret;//Heurística, apoyando tener mas fichas y el robo de movimientos.
    }
}
public class AlphaBetaPlayer extends GamePlayer {
    private Point best_move;
    
    public AlphaBetaPlayer (int mark, int depth){
        super(mark, depth);
        best_move=null;
    }
    
    @Override
    public int getMyMark(){
        return myMark;
    }
    @Override
    public boolean isUserPlayer() {
        return false;
    }

    @Override
    public String playerName() {
        return "AlphaBetaXDCPU";
    }

    @Override
    public Point play(int[][] board) {
        ArrayList<Point> myPossibleMoves = BoardHelper.getAllPossibleMoves(board,myMark);
        Node Primero = new Node(board,myMark,board);
        int num_move=0;
        int valor=-9999;
        

        if(myPossibleMoves.size() > 0){
            for (int i=0;i<myPossibleMoves.size();i++){
            int valor_n=alfa_beta(Primero,depth,-99999,99999,true);
            if(valor<valor_n){
                valor=valor_n;
                num_move=i;
            }
        }
        return myPossibleMoves.get(num_move);
        }else{
            return null;
        }
    }
    
    //Nueva Función que funciona
   public int min_max(Node nodo,int depth,boolean maximizo ){
       if(depth==0||nodo.isfinal()){
           int to_ret=nodo.evaluacion(myMark);
           
           return to_ret;
       }
       
       if(maximizo){
           int valor=(-9999999);
           ArrayList<Node> hijos = nodo.generateChildren();
           for (int i=0;i<hijos.size()-1;i++){
               valor = Math.max(valor, min_max(hijos.get(i),depth-1,false));               
           }
           return valor;
       }
       else{//Minimizando
           int valor=(9999999);
           ArrayList<Node> hijos = nodo.generateChildren();
           for (int i=0;i<hijos.size()-1;i++){
               valor = Math.min(valor, min_max(nodo,depth-1,true));               
           }
           return valor;   
       }
       
   }
   
   public int alfa_beta(Node nodo,int depth,int alfa,int beta,boolean maximizo ){
       if(depth==0||nodo.isfinal()){
           int to_ret=nodo.evaluacion(myMark);
           
           return to_ret;
       }
       
       if(maximizo){
           
           ArrayList<Node> hijos = nodo.generateChildren();
           for (int i=0;i<hijos.size()-1;i++){
               alfa = Math.max(alfa, alfa_beta(hijos.get(i),depth-1,alfa,beta,false));  
               if(alfa>=beta) break;
           }
           
           return alfa;
       }
       else{//Minimizando
           ArrayList<Node> hijos = nodo.generateChildren();
           for (int i=0;i<hijos.size()-1;i++){
               beta = Math.min(beta, alfa_beta(nodo,depth-1,alfa,beta,true));               
           }
           return beta;   
       }
       
   }
   
   
            
    public int evaluacion(Point movimiento,int [][] board){
       int [][] newBoard = BoardHelper.getNewBoardAfterMove(board,movimiento,this.myMark);
       int points = BoardHelper.getPlayerStoneCount(newBoard, this.myMark);
        
        return points;
    }   
    
      
}
