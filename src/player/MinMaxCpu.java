/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player;

import game.*;

import java.awt.*;
import java.util.*;
import java.util.Random;

/**
 *
 * @author gabri
 */
public class MinMaxCpu extends GamePlayer {
    Random rnd = new Random();
     private ArrayList<Integer> Lista;
    
    public MinMaxCpu (int mark, int depth){
        super(mark, depth);
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
        return "MinMaxPlayer";
    }

    @Override
    public Point play(int[][] board) {
        ArrayList<Point> myPossibleMoves = BoardHelper.getAllPossibleMoves(board,myMark);

        if(myPossibleMoves.size() > 0){
            return Min_Max(myPossibleMoves,board);
        }else{
            return null;
        }
    }
    
    public Point Min_Max(ArrayList<Point> movimientos, int[][] tablero_actual){
        int enemymark=0;
        if( myMark==1){
            enemymark=2;
        }
        else enemymark=1;
        
        ArrayList<int [][]> tableros= new ArrayList<>();
        ArrayList<int [][]> tableros2= new ArrayList<>() ;
        ArrayList<Point> movimientos2 = new ArrayList<>();
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
    
}
