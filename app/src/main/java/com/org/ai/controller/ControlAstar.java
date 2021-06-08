package com.org.ai.controller;

import com.org.ai.entity.astar;

import java.util.PriorityQueue;
import java.util.Stack;

public class ControlAstar {
    public static String upAstar(PriorityQueue<astar> pkAstarOpen, Stack<String> stackAstarClose, String s, int p) {
        String str = s;
        if (!(p < 3)) {
            char a = str.charAt(p - 3);
            String newS = str.substring(0, p) + a + str.substring(p + 1);
            str = newS.substring(0, (p - 3)) + '9' + newS.substring(p - 2);
        }
        // Eliminates child of X if its on OPEN or CLOSED
        if (!pkAstarOpen.contains(str)  && !stackAstarClose.contains(str))
            return str;
        else
            return "-1";
    }

    /*
     * MOVEMENT DOWN
     */
    public static String downAstar(PriorityQueue<astar> pkAstarOpen, Stack<String> stackAstarClose, String s, int p) {
        String str = s;
        if (!(p > 5)) {
            char a = str.charAt(p + 3);
            String newS = str.substring(0, p) + a + str.substring(p + 1);
            str = newS.substring(0, (p + 3)) + '9' + newS.substring(p + 4);
        }

        // Eliminates child of X if its on OPEN or CLOSED
        if (!pkAstarOpen.contains(str) && !stackAstarClose.contains(str))
            return str;
        else
            return "-1";
    }

    /*
     * MOVEMENT LEFT
     */
    public static String leftAstar(PriorityQueue<astar> pkAstarOpen, Stack<String> stackAstarClose, String s, int p) {
        String str = s;
        if (p%3!=0) {
            char a = str.charAt(p - 1);
            String newS = str.substring(0, p) + a + str.substring(p + 1);
            str = newS.substring(0, (p - 1)) + '9' + newS.substring(p);
        }
        // Eliminates child of X if its on OPEN or CLOSED
        if (!pkAstarOpen.contains(str) && !stackAstarClose.contains(str))
            return str;
        else
            return "-1";
    }

    /*
     * MOVEMENT RIGHT
     */
    public static String rightAstar(PriorityQueue<astar> pkAstarOpen, Stack<String> stackAstarClose,String s, int p) {
        String str = s;
        if (p != 2 && p != 5 && p != 8) {
            char a = str.charAt(p + 1);
            String newS = str.substring(0, p) + a + str.substring(p + 1);
            str = newS.substring(0, (p + 1)) + '9' + newS.substring(p + 2);
        }
        // Eliminates child of X if its on OPEN or CLOSED
        if (!pkAstarOpen.contains(str) && !stackAstarClose.contains(str))
            return str;
        else
            return "-1";
    }
}