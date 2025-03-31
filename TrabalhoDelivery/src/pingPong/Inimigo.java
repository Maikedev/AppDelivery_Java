package pingPong;

import java.awt.Graphics;
import java.awt.Color;

public class Inimigo {
    public int x;
    public int y;
    public int larguraInimigo;
    public int alturaInimigo;

    public Inimigo(int x, int y) {
        this.x = x;
        this.y = y;
        this.larguraInimigo = 10;
        this.alturaInimigo = 100;
    }

    public void desenhar(Graphics grafico){
        grafico.setColor(new Color(255, 255, 255));
        grafico.fillRect(x, y, larguraInimigo, alturaInimigo);
    }

    public void atualizar() {
        y += (Pong.bola.y - y - alturaInimigo / 2) * 0.1;

        if (y < 0) {
            y = 0;
        } else if (y + alturaInimigo > Pong.altura) {
            y = Pong.altura - alturaInimigo;
        }
    }
}
