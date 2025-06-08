package com.example.goodselection;

import java.util.ArrayList;
public class Player {
    int currentColumn;
    int currentY;

    public Player(int currentColumn,int currentY){
        this.currentColumn=currentColumn;
        this.currentY=currentY;
    }

    public int getCurrentColumn(){
        return currentColumn;
    }

    public void findLastColumn(ArrayList<Horizon> horizon){
        boolean moved;
        do {
            moved = false;
            for (Horizon h : horizon) {
                if (h.getFirstColumn() == currentColumn && currentY < h.getyPosition()) {
                    currentColumn = h.getSecondColumn();
                    currentY = h.getyPosition() + 1;
                    moved = true;
                    break;
                } else if (h.getSecondColumn() == currentColumn && currentY < h.getyPosition()) {
                    currentColumn = h.getFirstColumn();
                    currentY = h.getyPosition() + 1;
                    moved = true;
                    break;
                }
            }
        } while (moved);
    }
}
