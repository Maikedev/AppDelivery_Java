IMPORTAÇÕES

import javax.swing.*; // Importa todos os componentes da biblioteca Swing para GUI
import java.awt.*; // Importa classes básicas do AWT (Abstract Window Toolkit)
//import java.awt.event.ActionEvent; // (Comentado) Classe para eventos de ação
//import java.awt.event.ActionListener; // (Comentado) Interface para ouvintes de ação
import java.awt.event.WindowAdapter; // Classe para tratar eventos de janela
import java.awt.event.WindowEvent; // Classe que representa eventos de janela
import java.util.ArrayList; // Implementação de lista dinâmica
import java.util.List; // Interface para trabalhar com listas

CLASSE PIZZA

class Pizza { // Define a classe que representa uma pizza
    private String sabor; // Atributo: nome do sabor da pizza
    private double preco; // Atributo: valor da pizza
    private String status; // Atributo: status atual do pedido

    public Pizza(String sabor, double preco) { // Construtor da classe
        this.sabor = sabor; // Atribui o sabor recebido ao atributo
        this.preco = preco; // Atribui o preço recebido ao atributo
        this.status = "Em espera"; // Define status inicial padrão
    }

    public String getSabor() { // Método para obter o sabor
        return sabor; // Retorna o valor do atributo sabor
    }

    public double getPreco() { // Método para obter o preço
        return preco; // Retorna o valor do atributo preço
    }

    public String getStatus() { // Método para obter o status
        return status; // Retorna o valor do atributo status
    }

    public void setStatus(String status) { // Método para alterar o status
        this.status = status; // Atribui novo valor ao status
    }

    @Override // Anotação indicando sobrescrita de método
    public String toString() { // Método para representação textual
        return sabor + " - R$" + String.format("%.2f", preco); // Retorna string formatada
    }
}

CLASSE PIZZARIA

class Pizzaria { // Classe que gerencia a lógica da pizzaria
    private final List<Pizza> cardapio; // Lista de pizzas no cardápio (imutável)
    private final List<Pizza> pedidos; // Lista de pedidos realizados (imutável)

    public Pizzaria() { // Construtor
        this.cardapio = new ArrayList<>(); // Inicializa cardápio como ArrayList vazio
        this.pedidos = new ArrayList<>(); // Inicializa pedidos como ArrayList vazio
    }

    public void adicionarSabor(String sabor, double preco) { // Adiciona novo sabor
        cardapio.add(new Pizza(sabor, preco)); // Cria nova pizza e adiciona ao cardápio
    }

    public List<String> getCardapio() { // Retorna cardápio formatado
        List<String> sabores = new ArrayList<>(); // Cria lista para retorno
        for (Pizza pizza : cardapio) { // Percorre todas as pizzas
            sabores.add(pizza.toString()); // Adiciona representação textual à lista
        }
        return sabores; // Retorna lista formatada
    }

    public void fazerPedido(String sabor) { // Registra novo pedido
        for (Pizza pizza : cardapio) { // Procura sabor no cardápio
            if (pizza.getSabor().equalsIgnoreCase(sabor)) { // Comparação case-insensitive
                pedidos.add(new Pizza(pizza.getSabor(), pizza.getPreco())); // Adiciona cópia aos pedidos
                break; // Interrompe após encontrar
            }
        }
    }

    public List<String> getPedidos() { // Retorna lista de pedidos com status
        List<String> statusPedidos = new ArrayList<>(); // Cria lista para retorno
        for (int i = 0; i < pedidos.size(); i++) { // Percorre com índice
            Pizza pizza = pedidos.get(i); // Obtém pizza atual
            // Formata número, sabor e status:
            statusPedidos.add((i + 1) + ". " + pizza.getSabor() + " - " + pizza.getStatus());
        }
        return statusPedidos; // Retorna lista formatada
    }

    public void atualizarStatus(int numeroPedido, String status) { // Atualiza status
        if (numeroPedido > 0 && numeroPedido <= pedidos.size()) { // Verifica se pedido existe
            Pizza pizza = pedidos.get(numeroPedido - 1); // Obtém pedido (índice começa em 0)
            pizza.setStatus(status); // Atualiza status
        }
    }
}

CLASSE APPDELIVERY (continuação)

public class AppDelivery { // Classe principal do aplicativo
    private Pizzaria pizzaria; // Instância da lógica da pizzaria
    private JFrame frame; // Janela principal

    public AppDelivery() { // Construtor
        pizzaria = new Pizzaria(); // Inicializa pizzaria
        initialize(); // Configura interface
    }

    private void initialize() { // Inicializa interface gráfica
        frame = new JFrame("Pizzaria Delivery"); // Cria janela com título
        frame.setSize(400, 300); // Define largura=400, altura=300
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha aplicação ao encerrar
        frame.setLayout(new BorderLayout()); // Define gerenciador de layout

        JPanel panelMenu = new JPanel(); // Cria painel para botões
        panelMenu.setLayout(new GridLayout(2, 1)); // 2 linhas, 1 coluna

        JButton btnTrabalhador = new JButton("Área do Trabalhador"); // Botão trabalhador
        JButton btnUsuario = new JButton("Área do Usuário"); // Botão usuário

        // Define ações para os botões (usando lambda):
        btnTrabalhador.addActionListener(e -> abrirMenuTrabalhador());
        btnUsuario.addActionListener(e -> abrirMenuUsuario());

        panelMenu.add(btnTrabalhador); // Adiciona botão ao painel
        panelMenu.add(btnUsuario); // Adiciona botão ao painel

        frame.add(panelMenu, BorderLayout.CENTER); // Adiciona painel ao centro
        frame.setVisible(true); // Torna a janela visível
    }

    private void abrirMenuTrabalhador() { // Área administrativa
        JFrame frameTrabalhador = new JFrame("Área do Trabalhador"); // Nova janela
        frameTrabalhador.setSize(400, 300); // Tamanho
        frameTrabalhador.setLayout(new BorderLayout()); // Layout

        JPanel panelTrabalhador = new JPanel(); // Painel para botões
        panelTrabalhador.setLayout(new GridLayout(3, 1)); // 3 linhas

        // Cria botões:
        JButton btnAdicionarSabor = new JButton("Adicionar Sabor");
        JButton btnAtualizarStatus = new JButton("Atualizar Status do Pedido");
        JButton btnVoltar = new JButton("Voltar");

        // Configura ação do botão Adicionar Sabor:
        btnAdicionarSabor.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(2, 2)); // Painel para formulário
            panel.add(new JLabel("Sabor:")); // Rótulo
            JTextField saborField = new JTextField(); // Campo de texto
            panel.add(saborField);
            panel.add(new JLabel("Preço:"));
            JTextField precoField = new JTextField();
            panel.add(precoField);

            // Diálogo de confirmação:
            int result = JOptionPane.showConfirmDialog(frameTrabalhador, panel,
                    "Adicionar Novo Sabor", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) { // Se usuário confirmou
                try {
                    String sabor = saborField.getText(); // Obtém texto
                    double preco = Double.parseDouble(precoField.getText()); // Converte para número
                    if (!sabor.isEmpty() && preco > 0) { // Valida dados
                        pizzaria.adicionarSabor(sabor, preco); // Adiciona ao cardápio
                        JOptionPane.showMessageDialog(frameTrabalhador, // Feedback
                                "Sabor adicionado: " + sabor + " - R$" + String.format("%.2f", preco));
                    }
                } catch (NumberFormatException ex) { // Trata erro numérico
                    JOptionPane.showMessageDialog(frameTrabalhador,
                            "Preço inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Configura ação do botão Atualizar Status:
        btnAtualizarStatus.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frameTrabalhador, "Digite o número do pedido:");
            if (input != null && !input.isEmpty()) { // Se usuário digitou algo
                try {
                    int numeroPedido = Integer.parseInt(input); // Converte para número
                    String novoStatus = JOptionPane.showInputDialog(frameTrabalhador, "Digite o novo status:");
                    if (novoStatus != null && !novoStatus.isEmpty()) { // Se digitou status
                        pizzaria.atualizarStatus(numeroPedido, novoStatus); // Atualiza
                        JOptionPane.showMessageDialog(frameTrabalhador, "Status atualizado.");
                    }
                } catch (NumberFormatException ex) { // Trata erro numérico
                    JOptionPane.showMessageDialog(frameTrabalhador,
                            "Número de pedido inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Configura ação do botão Voltar:
        btnVoltar.addActionListener(e -> frameTrabalhador.dispose()); // Fecha janela

        // Adiciona botões ao painel:
        panelTrabalhador.add(btnAdicionarSabor);
        panelTrabalhador.add(btnAtualizarStatus);
        panelTrabalhador.add(btnVoltar);

        frameTrabalhador.add(panelTrabalhador, BorderLayout.CENTER); // Adiciona painel
        frameTrabalhador.setVisible(true); // Mostra janela
    }

    private void abrirMenuUsuario() { // Área do cliente
        JFrame frameUsuario = new JFrame("Área do Usuário"); // Nova janela
        frameUsuario.setSize(400, 300); // Tamanho
        frameUsuario.setLayout(new BorderLayout()); // Layout

        JPanel panelUsuario = new JPanel(); // Painel para botões
        panelUsuario.setLayout(new GridLayout(4, 1)); // 4 linhas

        // Cria botões:
        JButton btnVerCardapio = new JButton("Ver Cardápio");
        JButton btnFazerPedido = new JButton("Fazer Pedido");
        JButton btnAcompanharPedidos = new JButton("Acompanhar Pedidos");
        JButton btnVoltar = new JButton("Voltar");

        // Configura ação do botão Ver Cardápio:
        btnVerCardapio.addActionListener(e -> {
            List<String> cardapio = pizzaria.getCardapio(); // Obtém cardápio
            StringBuilder cardapioStr = new StringBuilder("Cardápio:\n"); // Cabeçalho
            for (String item : cardapio) { // Percorre itens
                cardapioStr.append("- ").append(item).append("\n"); // Formata lista
            }
            JOptionPane.showMessageDialog(frameUsuario, cardapioStr.toString()); // Exibe
        });

        // Configura ação do botão Fazer Pedido:
        btnFazerPedido.addActionListener(e -> {
            List<String> cardapio = pizzaria.getCardapio(); // Obtém cardápio
            if (cardapio.isEmpty()) { // Verifica se está vazio
                JOptionPane.showMessageDialog(frameUsuario, "Cardápio vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Diálogo para selecionar sabor:
            String saborPedido = (String) JOptionPane.showInputDialog(frameUsuario,
                    "Escolha o sabor da pizza:", "Fazer Pedido",
                    JOptionPane.PLAIN_MESSAGE, null,
                    cardapio.toArray(), cardapio.get(0));

            if (saborPedido != null && !saborPedido.isEmpty()) { // Se selecionou
                String sabor = saborPedido.split(" - ")[0]; // Extrai nome do sabor
                pizzaria.fazerPedido(sabor); // Registra pedido

                // Oferece jogo enquanto espera:
                Object[] options = {"Sim, quero jogar!", "Não, obrigado"};
                int escolha = JOptionPane.showOptionDialog(frameUsuario,
                        "Pedido realizado: " + sabor + "\nEnquanto sua pizza é preparada, deseja jogar ping pong?",
                        "Jogo de Ping Pong",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (escolha == JOptionPane.YES_OPTION) { // Se quer jogar
                    iniciarJogoPingPong(); // Inicia jogo
                } else { // Se não quer
                    JOptionPane.showMessageDialog(frameUsuario, // Mostra status
                            "Seu pedido está sendo preparado!\nStatus: " +
                                    pizzaria.getPedidos().get(pizzaria.getPedidos().size()-1));
                }
            }
        });

        // Configura ação do botão Acompanhar Pedidos:
        btnAcompanharPedidos.addActionListener(e -> {
            List<String> pedidos = pizzaria.getPedidos(); // Obtém pedidos
            StringBuilder pedidosStr = new StringBuilder("Status dos Pedidos:\n"); // Cabeçalho
            for (String pedido : pedidos) { // Percorre pedidos
                pedidosStr.append(pedido).append("\n"); // Formata lista
            }
            JOptionPane.showMessageDialog(frameUsuario, pedidosStr.toString()); // Exibe
        });

        // Configura ação do botão Voltar:
        btnVoltar.addActionListener(e -> frameUsuario.dispose()); // Fecha janela

        // Adiciona botões ao painel:
        panelUsuario.add(btnVerCardapio);
        panelUsuario.add(btnFazerPedido);
        panelUsuario.add(btnAcompanharPedidos);
        panelUsuario.add(btnVoltar);

        frameUsuario.add(panelUsuario, BorderLayout.CENTER); // Adiciona painel
        frameUsuario.setVisible(true); // Mostra janela
    }

    private void mostrarStatusPedido() { // Mostra status do último pedido
        if (!pizzaria.getPedidos().isEmpty()) { // Se há pedidos
            String ultimoPedido = pizzaria.getPedidos().get(pizzaria.getPedidos().size()-1); // Obtém último
            JOptionPane.showMessageDialog(frame, // Exibe diálogo
                    "Status do seu pedido:\n" + ultimoPedido,
                    "Acompanhamento de Pedido", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void iniciarJogoPingPong() { // Inicia jogo de ping pong
        try {
            JFrame gameFrame = new JFrame("Ping Pong - Enquanto sua pizza é preparada"); // Janela do jogo
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportamento ao fechar

            pingPong.Pong game = new pingPong.Pong(); // Instância do jogo
            gameFrame.add(game); // Adiciona jogo à janela
            gameFrame.pack(); // Ajusta tamanho
            gameFrame.setLocationRelativeTo(null); // Centraliza
            gameFrame.setResizable(false); // Tamanho fixo
            gameFrame.setVisible(true); // Mostra janela

            // Configura ação ao fechar janela:
            gameFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    JOptionPane.showMessageDialog(frame, // Mensagem ao sair
                            "Obrigado por jogar! Seu pedido continua sendo preparado.",
                            "Pedido em Andamento", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            game.startGame(); // Inicia o jogo
        } catch (Exception e) { // Trata erros
            JOptionPane.showMessageDialog(frame,
                    "Erro ao iniciar o jogo: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) { // Ponto de entrada
        SwingUtilities.invokeLater(() -> new AppDelivery()); // Inicia aplicação na thread de GUI
    }
}
