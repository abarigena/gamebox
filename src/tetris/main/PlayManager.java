package tetris.main;

import tetris.mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //фигура
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public static int dropinterval = 60;
    public static boolean gameOver;

    //score
    int level = 1;
    int lines;
    public static int score = 0;


    public PlayManager(){
        //Фрэйм Игровой области
        left_x = (GamePanel.WIDTH/2)-(WIDTH/2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y+HEIGHT;

        MINO_START_X = left_x + (WIDTH/2)-Block.SIZE ;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x +175;
        NEXTMINO_Y = top_y + 500;

        // первая фигура
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X,MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);

        dropinterval = 60;
        gameOver = false;
        level = 1;
        lines = 0;
        score = 0;

        staticBlocks.clear();

    }
    private Mino pickMino(){
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch (i){
            case 0: mino = new Mino_L1(); break;
            case 1: mino = new Mino_L2(); break;
            case 2: mino = new Mino_Square(); break;
            case 3: mino = new Mino_T(); break;
            case 4: mino = new Mino_Bar(); break;
            case 5: mino = new Mino_Z1(); break;
            case 6: mino = new Mino_Z2(); break;
        }
        return mino;
    }

    public void update(){

        //проверка на активность  фигуры
        if(currentMino.active==false){

            //если фигура уже не активно мы её положим в массив staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            //проверка на проигрыш
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                gameOver = true;
            }

            currentMino.deactivating = false;

            //замена следующий фигуры на текущую
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X,MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);

            //проверка на линию
            checkDelete();
        }
        else {
            currentMino.update();
        }
    }
    private void checkDelete(){

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount =0;

        while(x<right_x && y<bottom_y){

            for (int i = 0; i < staticBlocks.size(); i++) {
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    //прибавляем количество блоков
                    blockCount++;
                }
            }

            x+=Block.SIZE;

            if(x == right_x){
                //если кол-во блоков 12 значит строка заполнена и ее можно удалить
                if(blockCount==12){
                    for (int i = staticBlocks.size()-1; i>-1;i--) {

                        if(staticBlocks.get(i).y==y){
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;

                    if(lines%10== 0 &&dropinterval>1){
                        level++;
                        if(dropinterval>10){
                            dropinterval-=10;
                        }else {
                            dropinterval-=1;
                        }
                    }

                    //нужно сместить блоки из масива на нуллевые
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if(staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y+=Block.SIZE;
            }
        }
        if(lineCount>0){
            int singleLineScore = 10*level;
            score += singleLineScore *lineCount;
        }
    }
    public void draw(Graphics2D g2){

        //обрисовываем игровую область
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4,top_y-4,WIDTH+8,HEIGHT+8);


        int x = right_x+100;
        int y = bottom_y-200;
        g2.drawRect(x,y,200,200);
        g2.setFont(new Font("Arial",Font.PLAIN,30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60,y+60);

        //score frame
        g2.drawRect(x,top_y,250,300);
        x+=40;
        y = top_y + 90;
        //g2.drawString("Level: "+ level,x,y); y+=70;
        //g2.drawString("Lines: "+ lines,x,y); y+=70;
        g2.drawString("Score: "+ score,x,y);

        if(currentMino!= null){
            currentMino.draw(g2);
        }
        //отрисовка следующей фигуры
        nextMino.draw(g2);

        //отрисовка статичный блоков из масива
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        //пауза
        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(gameOver){
            x = left_x+25;
            y = top_y+320;
            g2.drawString("GameOver",x,y);

        }
        if(KeyHandler.pausePressed){
            x = left_x+70;
            y = top_y+320;
            g2.drawString("Пауза",x,y);
        }

    }
}
