package com.example.goodselection;

import java.io.Serializable;

class Horizon<T> implements Comparable<T>, Serializable {
    int firstColumn;
    int secondColumn;
    int yPosition;

    public Horizon(int firstColumn, int secondColumn, int yPosition) {
        this.firstColumn = firstColumn;
        this.secondColumn = secondColumn;
        this.yPosition = yPosition;
    }

    public int getFirstColumn(){
        return firstColumn;
    }
    public int getSecondColumn(){
        return secondColumn;
    }
    public int getyPosition() {
        return yPosition;
    }

    @Override
    public int compareTo(T o) {
        Horizon<T> comp = (Horizon<T>) o;
        return Integer.compare(this.yPosition, comp.yPosition);
    }
}