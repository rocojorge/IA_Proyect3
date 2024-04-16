/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player;

import game.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gabri
 */
public class MinMaxCpu extends GamePlayer {
    Random rnd = new Random();
    
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
            return myPossibleMoves.get(rnd.nextInt(myPossibleMoves.size()));
        }else{
            return null;
        }
    }
    
    public void Min_Max(){
        int i =0;
    }
    
    public int evaluacion(Point movimiento,int [][] board){
       int [][] newBoard = BoardHelper.getNewBoardAfterMove(board,movimiento,this.myMark);
       int points = BoardHelper.getPlayerStoneCount(newBoard, this.myMark);
        
        return points;
    }
    
}
