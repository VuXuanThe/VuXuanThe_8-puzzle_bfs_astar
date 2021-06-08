package com.org.ai.entity;

public class bfs implements Cloneable{

    public bfs(String now, bfs parent) {
        this.now = now;
        this.parent = parent;
    }
    public String now;
    public bfs parent;
}
