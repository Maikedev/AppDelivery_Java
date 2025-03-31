package pingPong;

import java.awt.Graphics;
import java.awt.Color;

public class Jogador {
    public boolean up;
    public boolean down;
    public int x;
    public int y;
    public int larguraJogador;
    public int alturaJogador;

    public Jogador(int x, int y) {
        this.x = x;
        this.y = y;
        this.larguraJogador = 10;
        this.alturaJogador = 100;
    }

    public void desenhar(Graphics grafico){
        grafico.setColor(new Color(255,255,255));
        grafico.fillRect(x, y, larguraJogador, alturaJogador);
    }

    public void atualizar(){
        if (up){
            y=y-4;
        } else if(down){
            y=y+4;
        }

        if (y+alturaJogador>Pong.altura){
            y=Pong.altura - alturaJogador;
        }else if(y<0){
            y=0;
        }
    }



}