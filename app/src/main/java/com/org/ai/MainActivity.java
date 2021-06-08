package com.org.ai;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.org.ai.databinding.ActivityMainBinding;
import com.org.ai.entity.ButtonPuzzle;
import com.org.ai.entity.astar;
import com.org.ai.entity.bfs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import static com.org.ai.SplashActivity.ASTAR;
import static com.org.ai.SplashActivity.BFS;
import static com.org.ai.controller.ControlAstar.downAstar;
import static com.org.ai.controller.ControlAstar.leftAstar;
import static com.org.ai.controller.ControlAstar.rightAstar;
import static com.org.ai.controller.ControlAstar.upAstar;
import static com.org.ai.controller.ControlBFS.down;
import static com.org.ai.controller.ControlBFS.left;
import static com.org.ai.controller.ControlBFS.right;
import static com.org.ai.controller.ControlBFS.up;
import static com.org.ai.util.Util.findCost;
import static com.org.ai.util.Util.setImage;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    String TAG="myApp";
    TextView langkah, langkahke;
    Button btnNext,btnPrev ;

    ArrayList<ButtonPuzzle> listPuzzle;
    int stateNow=9;
    int jalan=0;
    int step=0;

    //winstate
    String WinState="123456789";
    ArrayList<String> stateJawaban=new ArrayList<>();
    int stateJawabanNow=-1;

    // BFS
    Queue<bfs> queueBFSOpen = new LinkedList<>();
    Stack<String> stackBFSClose = new Stack<>();
    ArrayList<bfs> lBfs = new ArrayList<>();

    //A*
    PriorityQueue<astar> pkAsterOpen = new PriorityQueue<astar>(99999,new CompareAstar());
    Stack<String> stackAstarClose= new Stack<>();

    Button restart;
    TextView proses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    public void gantiTextButton(String text){
        for (int i = 0; i < listPuzzle.size(); i++) {
            String potong =text.substring(i,i+1);
            listPuzzle.get(i).getBtn().setText(potong);
            setImage(this, listPuzzle.get(i).getBtn(), Integer.parseInt(potong));
            if(potong.equals("9")){
                listPuzzle.get(i).getBtn().setText("");
            }
        }
    }



    public void init(){
        btnPrev = binding.btnPref;
        btnNext = binding.btnNext;
        proses  = findViewById(R.id.idProses);
        langkah = findViewById(R.id.tvtLangkah);
        langkahke =findViewById(R.id.tvlangkahke);

        binding.btnBack.setOnClickListener(view -> onBackPressed());

        btnPrev.setOnClickListener(view -> {
            if(stateJawabanNow>0){
                stateJawabanNow--;
                step--;
                langkahke.setText("Bước: "+step);
                gantiTextButton(stateJawaban.get(stateJawabanNow));
            }
        });

        btnNext.setOnClickListener(view -> {
            if(stateJawabanNow < stateJawaban.size()-1){
                stateJawabanNow++;
                step++;
                langkahke.setText("Bước: "+step);
                gantiTextButton(stateJawaban.get(stateJawabanNow));
            }
        });
        listPuzzle= new ArrayList<>();
        restart = findViewById(R.id.btnRestart);
        addButtons(listPuzzle);

        for (int i = 0; i< listPuzzle.size();i++) {
            Button btnClick = listPuzzle.get(i).getBtn();
            btnClick.setOnClickListener(view -> {
                Button btn=(Button)view;
                int lokasi = (int)btn.getTag();
                if(stateNow-3==lokasi){//swap atas
                    swap(btn,listPuzzle.get(stateNow).getBtn());
                }
                else if(stateNow+3==lokasi){//swap bawah
                    swap(btn,listPuzzle.get(stateNow).getBtn());
                }
                else if(stateNow+1==lokasi&&(stateNow+1)%3!=0){//swap kanan
                    swap(btn,listPuzzle.get(stateNow).getBtn());
                }
                else if(stateNow-1==lokasi&&(stateNow)%3!=0){//swap kiri
                    swap(btn,listPuzzle.get(stateNow).getBtn());
                }

            });
        }
        binding.btnStart.setOnClickListener(view -> {
            binding.layoutControl.setVisibility(View.VISIBLE);
            if(getIntent().hasExtra(BFS)){
                initBFS();
            }

            if(getIntent().hasExtra(ASTAR)){
                initAStar();
            }
        });

        binding.btnRestart.setOnClickListener(view -> {
            binding.layoutControl.setVisibility(View.GONE);
            restart();
        });
        random();
    }
    boolean win = false;

    public void initAStar() {
        proses.setText("Thuật toán sử dụng: A*");
        pkAsterOpen.clear();;
        stackAstarClose.clear();

        pkAsterOpen.add(new astar(getState(),null,9999,0));
        stateJawaban.clear();
        win=false;
        jalan=0;
        step=0;
        String x="";
        while(!pkAsterOpen.isEmpty()&&!win){
            x=pkAsterOpen.peek().now;
            stackAstarClose.add(x);
            astar parent = pkAsterOpen.peek();
            pkAsterOpen.remove(parent);
            int pos = x.indexOf("9");

            jalan++;
            if(x.equals(WinState)){
                stateJawabanNow=0;
                astar lastAster = parent;
                ArrayList<astar> jawaban = new ArrayList<>();
                while(lastAster.parent!=null){
                    jawaban.add(lastAster);
                    lastAster=lastAster.parent;
                }


                stateJawaban.add(lastAster.now);
                for (int i = jawaban.size()-1; i >=0 ; i--) {
                    stateJawaban.add(jawaban.get(i).now);
                }
                stateNow=8;

                //langkah.setText("Banyak langkah: "+jalan);
                win=true;
            }else{
                String temp="";
                astar parentAdd= (astar) clone(parent);
                temp = downAstar(pkAsterOpen, stackAstarClose, x, pos);
                if (!(temp.equals("-1"))){
                    pkAsterOpen.add(new astar(temp,parentAdd,findCost(WinState, temp),parentAdd.getDepth()));
                }
                temp = upAstar(pkAsterOpen, stackAstarClose, x, pos);
                if (!(temp.equals("-1"))){
                    pkAsterOpen.add(new astar(temp,parentAdd,findCost(WinState, temp),parentAdd.getDepth()));
                }

                temp = leftAstar(pkAsterOpen, stackAstarClose, x, pos);
                if (!(temp.equals("-1"))){
                    pkAsterOpen.add(new astar(temp,parentAdd,findCost(WinState, temp),parentAdd.getDepth()));
                }

                temp = rightAstar(pkAsterOpen, stackAstarClose, x, pos);
                if (!(temp.equals("-1"))){
                    pkAsterOpen.add(new astar(temp,parentAdd,findCost(WinState, temp),parentAdd.getDepth()));
                }
            }


        }
    }

    public void initBFS() {
        proses.setText("Thuật toán sử dụng: BFS");
        queueBFSOpen.clear();
        stackBFSClose.clear();
        lBfs.clear();
        queueBFSOpen.add(new bfs(getState(),null));
        lBfs.add(new bfs(getState(),null));
        stateJawaban.clear();
        win=false;
        String x="";
        jalan=0;
        step=0;
        int ind=0;
        while(!queueBFSOpen.isEmpty()&&!win){
            x=queueBFSOpen.peek().now;

            stackBFSClose.add(x);
            bfs open = queueBFSOpen.peek();
            bfs parents = open;
            ind++;
            if(ind<10){
                System.out.println(x);
                System.out.println(parents);
            }
            queueBFSOpen.remove(open);
            int pos = x.indexOf("9");

            jalan++;

            if(x.equals(WinState)){
                stateJawabanNow=0;
                bfs lastBfs=open;
                ArrayList<bfs> jawaban = new ArrayList<>();
                while(lastBfs.parent!=null){
                    jawaban.add(lastBfs);
                    lastBfs=lastBfs.parent;
                }


                stateJawaban.add(lastBfs.now);
                for (int i = jawaban.size()-1; i >=0 ; i--) {
                    stateJawaban.add(jawaban.get(i).now);
                }
                stateNow=8;
                win=true;

            }else{
                String temp="";
                temp = down(queueBFSOpen, stackBFSClose, x, pos);
                bfs parentAdd= (bfs) clone(parents);
                if (!(temp.equals("-1"))){
                    queueBFSOpen.add(new bfs(temp,parentAdd));
                    lBfs.add(new bfs(temp,parentAdd));
                }
                temp = up(queueBFSOpen, stackBFSClose, x, pos);
                if (!(temp.equals("-1"))){
                    queueBFSOpen.add(new bfs(temp,parentAdd));
                    lBfs.add(new bfs(temp,parentAdd));
                }

                temp = left(queueBFSOpen, stackBFSClose, x, pos);
                if (!(temp.equals("-1"))){
                    queueBFSOpen.add(new bfs(temp,parentAdd));
                    lBfs.add(new bfs(temp,parentAdd));
                }

                temp = right(queueBFSOpen, stackBFSClose, x, pos);
                if (!(temp.equals("-1"))){
                    queueBFSOpen.add(new bfs(temp,parentAdd));
                    lBfs.add(new bfs(temp,parentAdd));
                }
            }


        }
    }

    private Object clone(Object parents) {
        return parents;
    }

    public String getState(){
        StringBuilder stateNow= new StringBuilder();
        for (ButtonPuzzle btn:listPuzzle) {
            stateNow.append(btn.getBtn().getText().toString());
            if(btn.getBtn().getText().toString().equals("")){
                stateNow.append(9);
            }
        }
        return stateNow.toString();
    }

    public void restart() {
        init();
    }

    public void swap(Button b1,Button b2){
        String tmp=b2.getText().toString();
        Drawable drawable = b2.getBackground();
        b2.setText(b1.getText());
        b2.setBackground(b1.getBackground());
        b1.setText(tmp);
        b1.setBackground(drawable);
        stateNow=(int)b1.getTag();
    }

    public void random(){
        for (int i = 0; i < listPuzzle.size()-1; i++) {
            Button btn = listPuzzle.get(i).getBtn();
            btn.setText((i+1)+"");
            listPuzzle.get(i).setLokasi(i);
            int lokasi = listPuzzle.get(i).getLokasi();
            btn.setTag(lokasi);
        }

        Button btn = listPuzzle.get(8).getBtn();
        btn.setText("");
        swap(listPuzzle.get(4).getBtn(),listPuzzle.get(7).getBtn());
        swap(listPuzzle.get(4).getBtn(),listPuzzle.get(8).getBtn());
        stateNow = 4;
    }

    public void addButtons(ArrayList<ButtonPuzzle> list){
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b1),0,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b2),1,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b3),2,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b4),3,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b5),4,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b6),5,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b7),6,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b8),7,0));
        list.add(new ButtonPuzzle((Button)findViewById(R.id.b9),8,0));
    }

    class CompareAstar implements Comparator<astar> {
        @Override
        public int compare(astar a,astar b) {
            int asli=a.cost+a.depth;
            int baru =b.cost+b.depth;
            if (asli < baru)
                return -1;
            else if (asli > baru)
                return 1;
            return 0;
        }

    }
}