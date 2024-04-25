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
       //int mis_puntos=BoardHelper.getPlayerStoneCount(board, myMark);

        if(myPossibleMoves.size() > 0){
            return Min_Max(myPossibleMoves,board, depth);
        }else{
            return null;
        }
    }
    
    //No funciona
    public Point Min_Max(ArrayList<Point> movimientos, int[][] tablero_actual){
        int enemymark=3-myMark;
        
        ArrayList<int [][]> tableros= new ArrayList<>();
        int movimiento_ret=0;
        
        for (int i=0;i<movimientos.size()-1;i++){
            tableros.add(BoardHelper.getNewBoardAfterMove(tablero_actual, movimientos.get(i), myMark));
        }
        int puntos_yo=-9999;
        for (int i=0;i<tableros.size()-1;i++){
            ArrayList<Point> movimientos_enemigo = BoardHelper.getAllPossibleMoves(tableros.get(i),enemymark );
            for (int j=0;j<movimientos_enemigo.size()-1;j++){         
                int [][] tablero_enemigo = BoardHelper.getNewBoardAfterMove(tableros.get(i),movimientos_enemigo.get(j),enemymark);
                ArrayList<Point> movimientos_2 = BoardHelper.getAllPossibleMoves(tablero_enemigo, myMark);
                for (int k=0;k<movimientos_2.size()-1;k++){
                    int[][] tablero_2= BoardHelper.getNewBoardAfterMove(tablero_enemigo, movimientos_2.get(k), myMark);
                    int puntos_ahora=BoardHelper.getPlayerStoneCount(tablero_2, myMark);
                    if(puntos_ahora>=puntos_yo){
                        puntos_yo=puntos_ahora;
                        movimiento_ret=i;                        
                    }
                }
            } 
        }
        return movimientos.get(movimiento_ret); 
    }
            
    public int evaluacion(Point movimiento,int [][] board){
       int [][] newBoard = BoardHelper.getNewBoardAfterMove(board,movimiento,this.myMark);
       int points = BoardHelper.getPlayerStoneCount(newBoard, this.myMark);
        
        return points;
    }   
    
    public void Generacion_01(int i, ArrayList<Point> movimientos_enemigo, ArrayList<int [][]> tableros, int enemymark) {
    movimientos_enemigo = BoardHelper.getAllPossibleMoves(tableros.get(i),enemymark );
    }
    
    public void Generacion_02(int i, int j, int [][] tablero_enemigo, ArrayList<int [][]> tableros, ArrayList<Point> movimientos_enemigo, int enemymark, ArrayList<Point> movimientos_2, int myMark){
    tablero_enemigo = BoardHelper.getNewBoardAfterMove(tableros.get(i),movimientos_enemigo.get(j),enemymark);
    movimientos_2 = BoardHelper.getAllPossibleMoves(tablero_enemigo, myMark);
    }
    
    public void Generacion_03(int i, int k, int[][] tablero_2, int [][] tablero_enemigo, ArrayList<Point> movimientos_2, int myMark, int puntos_ahora, int puntos_yo, int movimiento_ret) {
    tablero_2 = BoardHelper.getNewBoardAfterMove(tablero_enemigo, movimientos_2.get(k), myMark);
    puntos_ahora = BoardHelper.getPlayerStoneCount(tablero_2, myMark);
    if (puntos_ahora >= puntos_yo) {
        puntos_yo = puntos_ahora;
        movimiento_ret = i;
    }
}
    
    public Point Min_Max(ArrayList<Point> movimientos, int[][] tablero_actual, int depth){
    int enemymark=3-myMark;

    ArrayList<int [][]> tableros= new ArrayList<>();
    int movimiento_ret=0;
    
    for (int i=0;i<movimientos.size()-1;i++){
        tableros.add(BoardHelper.getNewBoardAfterMove(tablero_actual, movimientos.get(i), myMark));
    }
    int puntos_yo=-9999;

    ArrayList<Point> movimientos_enemigo= new ArrayList<>();
    int [][] tablero_enemigo= new int [8][8];
    ArrayList<Point> movimientos_2= new ArrayList<>();
    int[][] tablero_2=new int [8][8];
    int puntos_ahora=0;

    for(int l=0;l<depth;l++){
        for(int i=0;i<movimientos.size()-1;i++){
            if(l%2==0){int lol=0;}
            else{
                if(l%3==0){int lol=0;}
                else{
                    Generacion_01(i, movimientos_enemigo, tableros, myMark);
                }
            }
            for(int j=0;j<movimientos_enemigo.size()-1;j++){
                if(l%2==0){
                    Generacion_02(i, j, tablero_enemigo, tableros, movimientos_enemigo, myMark, movimientos_2, enemymark);
                }
                for(int k=0;k<movimientos_2.size()-1;k++){
                    if(l%2==0){int lol=0;}
                    else{
                        if(l%3==0){
                            Generacion_03(i, k, tablero_2, tablero_enemigo, movimientos_2, enemymark, puntos_ahora, puntos_yo, movimiento_ret);
                        }
                    }
                }
            }
        }
    }
    
    return movimientos.get(movimiento_ret);
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
