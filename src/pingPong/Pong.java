package pingPong;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*;

public class Pong extends Canvas implements Runnable, KeyListener {
    public static int largura = 450;
    public static int altura = 400;
    public static Jogador jogador;
    public static Inimigo inimigo;
    public static Bola bola;
    public static int pontosJogador = 0;
    public static int pontosInimigo = 0;
    public static boolean jogoAtivo = true;
    private static long tempoDeFim = 0;
    private boolean running = false;

    public Pong() {
        this.setPreferredSize(new Dimension(largura, altura));
        this.addKeyListener(this);
        jogador = new Jogador(15, 150);
        inimigo = new Inimigo(425, 150);
        bola = new Bola(largura/2, altura/2);
        this.requestFocusInWindow();
    }

    public synchronized void startGame() {
        if (running) return;
        running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / 60.0;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                atualizar();
                delta--;
            }

            desenhar();
        }
    }

    public void desenhar() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, largura, altura);

        g.setColor(Color.WHITE);
        g.drawRect(0, 0, largura-1, altura-1);

        jogador.desenhar(g);
        inimigo.desenhar(g);
        bola.desenhar(g);

        g.drawString("Jogador: " + pontosJogador, 50, 30);
        g.drawString("Inimigo: " + pontosInimigo, largura-150, 30);

        if (!jogoAtivo) {
            g.setColor(pontosJogador == 5 ? Color.GREEN : Color.RED);
            g.drawString(pontosJogador == 5 ? "Você ganhou!" : "Você perdeu!",
                    largura/2-60, altura/6);
        }

        g.dispose();
        bs.show();
    }

    private void atualizar() {
        if (!jogoAtivo) {
            if (System.currentTimeMillis() - tempoDeFim >= 3000) {
                reiniciarJogoCompleto();
            }
            return;
        }

        jogador.atualizar();
        inimigo.atualizar();
        bola.atualizar();

        if (bola.x < 0) {
            pontosInimigo++;
            reiniciarPosicoes();
        } else if (bola.x > largura) {
            pontosJogador++;
            reiniciarPosicoes();
        }

        if (pontosJogador >= 5 || pontosInimigo >= 5) {
            jogoAtivo = false;
            tempoDeFim = System.currentTimeMillis();
        }
    }

    private void reiniciarPosicoes() {
        bola.x = largura/2;
        bola.y = altura/2;
        bola.resetDirection();

        jogador.y = altura/2 - jogador.alturaJogador/2;
        inimigo.y = altura/2 - inimigo.alturaInimigo/2;
    }

    private void reiniciarJogoCompleto() {
        pontosJogador = 0;
        pontosInimigo = 0;
        reiniciarPosicoes();
        jogoAtivo = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            jogador.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            jogador.down = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            jogador.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            jogador.down = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
