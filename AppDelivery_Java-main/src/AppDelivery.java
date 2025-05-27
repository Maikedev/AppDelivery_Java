import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

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
    private List<Pizza> cardapio = new ArrayList<>();
    private List<Pizza> pedidos = new ArrayList<>();

    public Pizzaria() {
        carregarDadosDoBanco();
    }

    private void carregarDadosDoBanco() {
        carregarSabores();
        carregarPedidos();
    }

    private void carregarSabores() {
        String sql = "SELECT nome, preco FROM sabores";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cardapio.add(new Pizza(
                        rs.getString("nome"),
                        rs.getDouble("preco")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarPedidos() {
        String sql = "SELECT s.nome, s.preco, p.status FROM pedidos p " +
                "JOIN sabores s ON p.sabor_id = s.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pizza pizza = new Pizza(
                        rs.getString("nome"),
                        rs.getDouble("preco")
                );
                pizza.setStatus(rs.getString("status"));
                pedidos.add(pizza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adicionarSabor(String sabor, double preco) {
        Pizza novaPizza = new Pizza(sabor, preco);
        cardapio.add(novaPizza);
        salvarSaborNoBanco(novaPizza);
    }

    private void salvarSaborNoBanco(Pizza pizza) {
        String sql = "INSERT INTO sabores (nome, preco) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pizza.getSabor());
            stmt.setDouble(2, pizza.getPreco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fazerPedido(String sabor) {
        for (Pizza pizza : cardapio) {
            if (pizza.getSabor().equalsIgnoreCase(sabor)) {
                Pizza novoPedido = new Pizza(pizza.getSabor(), pizza.getPreco());
                pedidos.add(novoPedido);
                salvarPedidoNoBanco(novoPedido);
                break;
            }
        }
    }

    private void salvarPedidoNoBanco(Pizza pizza) {
        String sql = "INSERT INTO pedidos (sabor_id, status) VALUES " +
                "((SELECT id FROM sabores WHERE nome = ?), ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pizza.getSabor());
            stmt.setString(2, pizza.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removerPedido(int numeroPedido) {
    if (numeroPedido > 0 && numeroPedido <= pedidos.size()) {
        Pizza pizza = pedidos.get(numeroPedido - 1);
        try {
            String sql = "DELETE FROM pedidos WHERE id = ("
                       + "SELECT p.id FROM (SELECT id FROM pedidos WHERE sabor_id = "
                       + "(SELECT id FROM sabores WHERE nome = ?) LIMIT 1) p)";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, pizza.getSabor());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    pedidos.remove(numeroPedido - 1);
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    return false;
    }

    public List<String> getCardapio() {
        List<String> sabores = new ArrayList<>();
        for (Pizza pizza : cardapio) {
            sabores.add(pizza.toString());
        }
        return sabores;
    }

    public List<Pizza> getPedidosList() {
    return pedidos;
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
        frameTrabalhador.setSize(400, 400);
        frameTrabalhador.setLayout(new BorderLayout());

        JPanel panelTrabalhador = new JPanel();
        panelTrabalhador.setLayout(new GridLayout(5, 1));

        JButton btnAdicionarSabor = new JButton("Adicionar Sabor");
        JButton btnAcompanharPedidos = new JButton("Acompanhar Pedidos");
        JButton btnAtualizarStatus = new JButton("Atualizar Status do Pedido");
        JButton btnExcluirPedido = new JButton("Excluir Pedido");
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

        btnAcompanharPedidos.addActionListener(e -> {
            List<String> pedidos = pizzaria.getPedidos();
            StringBuilder pedidosStr = new StringBuilder("Status dos Pedidos:\n");
            for (String pedido : pedidos) {
                pedidosStr.append(pedido).append("\n");
            }
            JOptionPane.showMessageDialog(frameTrabalhador, pedidosStr.toString());
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

        btnExcluirPedido.addActionListener(e -> {
            List<Pizza> pedidosList = pizzaria.getPedidosList();
            List<String> pedidosExibicao = pizzaria.getPedidos();
            
            if (pedidosList.isEmpty()) {
                JOptionPane.showMessageDialog(frameTrabalhador, 
                    "Não há pedidos para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String input = JOptionPane.showInputDialog(frameTrabalhador,
                "Pedidos atuais:\n" + String.join("\n", pedidosExibicao) + 
                "\n\nDigite o número do pedido a ser excluído:");

            if (input != null && !input.isEmpty()) {
                try {
                    int numeroPedido = Integer.parseInt(input);
                    if (numeroPedido > 0 && numeroPedido <= pedidosList.size()) {
                        if (pizzaria.removerPedido(numeroPedido)) {
                            JOptionPane.showMessageDialog(frameTrabalhador, 
                                "Pedido #" + numeroPedido + " removido com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(frameTrabalhador, 
                                "Falha ao remover pedido.", 
                                "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frameTrabalhador, 
                            "Número fora do intervalo válido (1-" + pedidosList.size() + ")", 
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frameTrabalhador, 
                        "Digite apenas números!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            });

        btnVoltar.addActionListener(e -> frameTrabalhador.dispose());

        panelTrabalhador.add(btnAdicionarSabor);
        panelTrabalhador.add(btnAcompanharPedidos);
        panelTrabalhador.add(btnAtualizarStatus);
        panelTrabalhador.add(btnExcluirPedido);
        panelTrabalhador.add(btnVoltar);

        frameTrabalhador.add(panelTrabalhador, BorderLayout.CENTER);
        frameTrabalhador.setVisible(true);
    }

    private void abrirMenuUsuario() {
        JFrame frameUsuario = new JFrame("Área do Usuário");
        frameUsuario.setSize(400, 300);
        frameUsuario.setLayout(new BorderLayout());

        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new GridLayout(3, 1));

        JButton btnVerCardapio = new JButton("Ver Cardápio");
        JButton btnFazerPedido = new JButton("Fazer Pedido");
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



        btnVoltar.addActionListener(e -> frameUsuario.dispose());

        panelUsuario.add(btnVerCardapio);
        panelUsuario.add(btnFazerPedido);
        panelUsuario.add(btnVoltar);

        frameUsuario.add(panelUsuario, BorderLayout.CENTER);
        frameUsuario.setVisible(true);
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