package com.org.ai.util;

import android.content.Context;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.org.ai.R;

public class Util {
    public static int findCost(String WinState, String now){
        int cost=0;
        for (int i = 0; i < 9; i++) {
            int a1=Integer.parseInt(now.substring(i,i+1));
            int a2=Integer.parseInt(WinState.substring(i,i+1));
            System.out.println();
            if(a1!=a2){
                cost++;
            }
        }
        return cost;
    }

    public static void setImage(Context context, Button btn, int num){
        if(num == 1){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.on));
        }
        else if(num == 2){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.tw));
        }
        else if(num == 3){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.thr));
        }
        else if(num == 4){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.fo));
        }
        else if(num == 5){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.fi));
        }
        else if(num == 6){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.si));
        }
        else if(num == 7){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.se));
        }
        else if(num == 8){
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.ei));
        }
        else{
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.ni));
        }
    }
}

