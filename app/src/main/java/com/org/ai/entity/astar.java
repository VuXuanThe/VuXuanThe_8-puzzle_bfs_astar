package com.org.ai.entity;

public class astar implements Cloneable{
    public String now;
    public astar parent;
    public int cost,depth;

    public astar(String now, astar parent, int cost, int depth) {
        this.now = now;
        this.parent = parent;
        this.cost = cost;
        this.depth = depth;
    }

    public int getDepth(){
        return this.depth+1;
    }

}
