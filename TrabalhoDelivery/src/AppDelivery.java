import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

class Pizza {
    private String sabor;
    private double preco;
    private String status;

    public Pizza(String sabor, double preco) {
        this.sabor = sabor;
        this.preco = preco;
        this.status = "Em espera";
    }

    public String getSabor() {
        return sabor;
    }

    public double getPreco() {
        return preco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return sabor + " - R$" + String.format("%.2f", preco);
    }
}

class Pizzaria {
    private final List<Pizza> cardapio;
    private final List<Pizza> pedidos;

    public Pizzaria() {
        this.cardapio = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public void adicionarSabor(String sabor, double preco) {
        cardapio.add(new Pizza(sabor, preco));
    }

    public List<String> getCardapio() {
        List<String> sabores = new ArrayList<>();
        for (Pizza pizza : cardapio) {
            sabores.add(pizza.toString());
        }
        return sabores;
    }

    public void fazerPedido(String sabor) {
        for (Pizza pizza : cardapio) {
            if (pizza.getSabor().equalsIgnoreCase(sabor)) {
                pedidos.add(new Pizza(pizza.getSabor(), pizza.getPreco()));
                break;
            }
        }
    }

    public List<String> getPedidos() {
        List<String> statusPedidos = new ArrayList<>();
        for (int i = 0; i < pedidos.size(); i++) {
            Pizza pizza = pedidos.get(i);
            statusPedidos.add((i + 1) + ". " + pizza.getSabor() + " - " + pizza.getStatus());
        }
        return statusPedidos;
    }

    public void atualizarStatus(int numeroPedido, String status) {
        if (numeroPedido > 0 && numeroPedido <= pedidos.size()) {
            Pizza pizza = pedidos.get(numeroPedido - 1);
            pizza.setStatus(status);
        }
    }
}

public class AppDelivery {
    private Pizzaria pizzaria;
    private JFrame frame;

    public AppDelivery() {
        pizzaria = new Pizzaria();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Pizzaria Delivery");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(2, 1));

        JButton btnTrabalhador = new JButton("Área do Trabalhador");
        JButton btnUsuario = new JButton("Área do Usuário");

        btnTrabalhador.addActionListener(e -> abrirMenuTrabalhador());
        btnUsuario.addActionListener(e -> abrirMenuUsuario());

        panelMenu.add(btnTrabalhador);
        panelMenu.add(btnUsuario);

        frame.add(panelMenu, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void abrirMenuTrabalhador() {
        JFrame frameTrabalhador = new JFrame("Área do Trabalhador");
        frameTrabalhador.setSize(400, 300);
        frameTrabalhador.setLayout(new BorderLayout());

        JPanel panelTrabalhador = new JPanel();
        panelTrabalhador.setLayout(new GridLayout(3, 1));

        JButton btnAdicionarSabor = new JButton("Adicionar Sabor");
        JButton btnAtualizarStatus = new JButton("Atualizar Status do Pedido");
        JButton btnVoltar = new JButton("Voltar");

        btnAdicionarSabor.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Sabor:"));
            JTextField saborField = new JTextField();
            panel.add(saborField);
            panel.add(new JLabel("Preço:"));
            JTextField precoField = new JTextField();
            panel.add(precoField);

            int result = JOptionPane.showConfirmDialog(frameTrabalhador, panel,
                    "Adicionar Novo Sabor", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String sabor = saborField.getText();
                    double preco = Double.parseDouble(precoField.getText());
                    if (!sabor.isEmpty() && preco > 0) {
                        pizzaria.adicionarSabor(sabor, preco);
                        JOptionPane.showMessageDialog(frameTrabalhador,
                                "Sabor adicionado: " + sabor + " - R$" + String.format("%.2f", preco));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frameTrabalhador,
                            "Preço inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAtualizarStatus.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frameTrabalhador, "Digite o número do pedido:");
            if (input != null && !input.isEmpty()) {
                try {
                    int numeroPedido = Integer.parseInt(input);
                    String novoStatus = JOptionPane.showInputDialog(frameTrabalhador, "Digite o novo status:");
                    if (novoStatus != null && !novoStatus.isEmpty()) {
                        pizzaria.atualizarStatus(numeroPedido, novoStatus);
                        JOptionPane.showMessageDialog(frameTrabalhador, "Status atualizado.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frameTrabalhador,
                            "Número de pedido inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVoltar.addActionListener(e -> frameTrabalhador.dispose());

        panelTrabalhador.add(btnAdicionarSabor);
        panelTrabalhador.add(btnAtualizarStatus);
        panelTrabalhador.add(btnVoltar);

        frameTrabalhador.add(panelTrabalhador, BorderLayout.CENTER);
        frameTrabalhador.setVisible(true);
    }

    private void abrirMenuUsuario() {
        JFrame frameUsuario = new JFrame("Área do Usuário");
        frameUsuario.setSize(400, 300);
        frameUsuario.setLayout(new BorderLayout());

        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new GridLayout(4, 1));

        JButton btnVerCardapio = new JButton("Ver Cardápio");
        JButton btnFazerPedido = new JButton("Fazer Pedido");
        JButton btnAcompanharPedidos = new JButton("Acompanhar Pedidos");
        JButton btnVoltar = new JButton("Voltar");

        btnVerCardapio.addActionListener(e -> {
            List<String> cardapio = pizzaria.getCardapio();
            StringBuilder cardapioStr = new StringBuilder("Cardápio:\n");
            for (String item : cardapio) {
                cardapioStr.append("- ").append(item).append("\n");
            }
            JOptionPane.showMessageDialog(frameUsuario, cardapioStr.toString());
        });

        btnFazerPedido.addActionListener(e -> {
            List<String> cardapio = pizzaria.getCardapio();
            if (cardapio.isEmpty()) {
                JOptionPane.showMessageDialog(frameUsuario, "Cardápio vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String saborPedido = (String) JOptionPane.showInputDialog(frameUsuario,
                    "Escolha o sabor da pizza:", "Fazer Pedido",
                    JOptionPane.PLAIN_MESSAGE, null,
                    cardapio.toArray(), cardapio.get(0));

            if (saborPedido != null && !saborPedido.isEmpty()) {
                String sabor = saborPedido.split(" - ")[0];
                pizzaria.fazerPedido(sabor);

                Object[] options = {"Sim, quero jogar!", "Não, obrigado"};
                int escolha = JOptionPane.showOptionDialog(frameUsuario,
                        "Pedido realizado: " + sabor + "\nEnquanto sua pizza é preparada, deseja jogar ping pong?",
                        "Jogo de Ping Pong",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (escolha == JOptionPane.YES_OPTION) {
                    iniciarJogoPingPong();
                } else {
                    JOptionPane.showMessageDialog(frameUsuario,
                            "Seu pedido está sendo preparado!\nStatus: " +
                                    pizzaria.getPedidos().get(pizzaria.getPedidos().size()-1));
                }
            }
        });

        btnAcompanharPedidos.addActionListener(e -> {
            List<String> pedidos = pizzaria.getPedidos();
            StringBuilder pedidosStr = new StringBuilder("Status dos Pedidos:\n");
            for (String pedido : pedidos) {
                pedidosStr.append(pedido).append("\n");
            }
            JOptionPane.showMessageDialog(frameUsuario, pedidosStr.toString());
        });

        btnVoltar.addActionListener(e -> frameUsuario.dispose());

        panelUsuario.add(btnVerCardapio);
        panelUsuario.add(btnFazerPedido);
        panelUsuario.add(btnAcompanharPedidos);
        panelUsuario.add(btnVoltar);

        frameUsuario.add(panelUsuario, BorderLayout.CENTER);
        frameUsuario.setVisible(true);
    }

    private void mostrarStatusPedido() {
        if (!pizzaria.getPedidos().isEmpty()) {
            String ultimoPedido = pizzaria.getPedidos().get(pizzaria.getPedidos().size()-1);
            JOptionPane.showMessageDialog(frame,
                    "Status do seu pedido:\n" + ultimoPedido,
                    "Acompanhamento de Pedido", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void iniciarJogoPingPong() {
        try {
            JFrame gameFrame = new JFrame("Ping Pong - Enquanto sua pizza é preparada");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            pingPong.Pong game = new pingPong.Pong();
            gameFrame.add(game);
            gameFrame.pack();
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setResizable(false);
            gameFrame.setVisible(true);

            gameFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    JOptionPane.showMessageDialog(frame,
                            "Obrigado por jogar! Seu pedido continua sendo preparado.",
                            "Pedido em Andamento", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            game.startGame();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Erro ao iniciar o jogo: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppDelivery());
    }
}