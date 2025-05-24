package pingPong;

import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Bola {
    public int x;
    public int y;
    public int larguraBola;
    public int alturaBola;
    public double direcaox;
    public double direcaoy;
    public double velocidade=5.5;

    public Bola(int x, int y) {
        this.x = x;
        this.y = y;
        this.larguraBola=10;
        this.alturaBola=10;

        int angulo = new Random().nextInt(80);
        direcaox = Math.cos(Math.toRadians(angulo));
        direcaoy = Math.sin(Math.toRadians(angulo));
    }

    public void desenhar(Graphics grafico){
        grafico.setColor(new Color(255, 255, 255));
        grafico.fillRect(x, y, larguraBola, alturaBola);
    }

    public void atualizar(){
        if (y+(direcaoy *velocidade)+alturaBola>=Pong.altura){
            direcaoy*=-1;
        }
        else if(y+(direcaoy*velocidade)<0){
            direcaoy*=-1;
        }

        Rectangle bola = new Rectangle((int)(x+(direcaox*velocidade)),(int)(y+(direcaoy*velocidade)), larguraBola, alturaBola );
        Rectangle jogador = new Rectangle(Pong.jogador.x, Pong.jogador.y, Pong.jogador.larguraJogador, Pong.jogador.alturaJogador);
        Rectangle inimigo = new Rectangle(Pong.inimigo.x, Pong.inimigo.y, Pong.inimigo.larguraInimigo, Pong.inimigo.alturaInimigo);

        if(bola.intersects(jogador)){
            int angulo = new Random().nextInt(80);
            direcaox = Math.cos(Math.toRadians(angulo));
            direcaoy = Math.sin(Math.toRadians(angulo));
            if (direcaox <0){
                direcaox*=-1;
            }
        }else if(bola.intersects(inimigo)){
            int angulo = new Random().nextInt(80);
            direcaox = Math.cos(Math.toRadians(angulo));
            direcaoy = Math.sin(Math.toRadians(angulo));
            if (direcaox>0){
                direcaox*=-1;
            }
        }

        x += direcaox * velocidade;
        y += direcaoy * velocidade;
    }

    public void resetDirection() {
    }
}
