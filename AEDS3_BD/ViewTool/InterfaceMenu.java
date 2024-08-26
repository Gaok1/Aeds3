package ViewTool;

import Tools.*;
import Structures.*;
import Store.*;
import TP4.BoyerMoore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InterfaceMenu extends JFrame {
    private static boolean Running = true;

    // Campos de texto
    private JTextField nomeTextField = new JTextField(20);
    private JTextField artistaTextField = new JTextField(20);
    private JTextField popularidadeTextField = new JTextField(20);
    private JTextField dataLancamentoTextField = new JTextField(20);
    private JTextField generoTextField = new JTextField(20);
    private JTextField dancabilidadeTextField = new JTextField(20);
    private JTextField hashTextField = new JTextField(20);
    private JTextField idTextField = new JTextField(20);
    private JTextField searchNomeTextField = new JTextField(20);
    private JTextField searchArtistaTextField = new JTextField(20);

    // Botões
    private JButton adicionarButton = new JButton("Adicionar");
    private JButton atualizarButton = new JButton("Atualizar");
    private JButton pesquisarButton = new JButton("Pesquisar");
    private JButton deletarButton = new JButton("Deletar");
    private JButton listarButton = new JButton("Listar Registros");
    private JButton MockDataButton = new JButton("Mock Data");
    private JButton listUnvalidRegistersButton = new JButton("Listar Inválidos");
    private JButton ordenarButton = new JButton("Ordenar");

    // Tabela
    private DefaultTableModel tableModel;
    private JTable table;

    public InterfaceMenu() {
        setTitle("Gerenciamento de Registros de Música");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        mainPanel.add(createFieldsPanel(), BorderLayout.NORTH);
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        addActionListeners();
        setVisible(true);
    }

    private JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Campos de Entrada"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        addField(fieldsPanel, gbc, "ID:", idTextField, 0);
        addField(fieldsPanel, gbc, "Nome:", nomeTextField, 1);
        addField(fieldsPanel, gbc, "Artista:", artistaTextField, 2);
        addField(fieldsPanel, gbc, "Popularidade:", popularidadeTextField, 3);
        addField(fieldsPanel, gbc, "Data de Lançamento:", dataLancamentoTextField, 4);
        addField(fieldsPanel, gbc, "Gênero:", generoTextField, 5);
        addField(fieldsPanel, gbc, "Dançabilidade:", dancabilidadeTextField, 6);
        addField(fieldsPanel, gbc, "Hash:", hashTextField, 7);
        addField(fieldsPanel, gbc, "Pesquisar Nome:", searchNomeTextField, 8);
        addField(fieldsPanel, gbc, "Pesquisar Artista:", searchArtistaTextField, 9);

        return fieldsPanel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField textField, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonsPanel.setBackground(Color.WHITE);

        JButton[] buttons = { adicionarButton, atualizarButton, pesquisarButton, deletarButton, listarButton, MockDataButton, listUnvalidRegistersButton, ordenarButton };
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        Color buttonColor = new Color(30, 144, 255);

        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setForeground(Color.WHITE);
            button.setBackground(buttonColor);
            buttonsPanel.add(button);
        }
        listUnvalidRegistersButton.setForeground(Color.WHITE);
        listUnvalidRegistersButton.setBackground(Color.RED);

        return buttonsPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Artista");
        tableModel.addColumn("Popularidade");
        tableModel.addColumn("Data de Lançamento");
        tableModel.addColumn("Gênero");
        tableModel.addColumn("Dançabilidade");
        tableModel.addColumn("Hash");

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registros"));
        scrollPane.setPreferredSize(new Dimension(850, 400));

        return scrollPane;
    }

    private void addActionListeners() {
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarMusica();
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarMusica();
            }
        });

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarMusica();
            }
        });

        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarMusica();
            }
        });

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarMusicas();
            }
        });

        MockDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarMockData();
            }
        });

        listUnvalidRegistersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarRegistrosInvalidos();
            }
        });

        ordenarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarMusicas();
            }
        });
    }

    private void adicionarMusica() {
        try {
            String informacoes = String.join(",", nomeTextField.getText(), artistaTextField.getText(), popularidadeTextField.getText(), dataLancamentoTextField.getText(), generoTextField.getText(), dancabilidadeTextField.getText(), hashTextField.getText());
            Logs.Details("Informações reunidas:\n" + informacoes + "\n\n");

            Arq.addRegistro(informacoes);
            limparCampos();
            listarMusicas();
        } catch (IllegalArgumentException ex) {
            Logs.Alert("Erro ao adicionar a música:\n" + ex.getMessage());
        } catch (Exception ex) {
            Logs.Alert("Erro ao adicionar a música:\n" + ex.getMessage());
        }
    }

    private void atualizarMusica() {
        String id = idTextField.getText();
        try {
            String informacoes = String.join(",", id, nomeTextField.getText(), artistaTextField.getText(), popularidadeTextField.getText(), dataLancamentoTextField.getText(), generoTextField.getText(), dancabilidadeTextField.getText(), hashTextField.getText());
            Logs.Details("Informações reunidas:\n" + informacoes + "\n\n");
            Arq.UpdateSong(Integer.parseInt(id), informacoes);
            limparCampos();
        } catch (NumberFormatException ex) {
            Logs.Alert("Erro ao atualizar a música:\nNúmero esperado " + ex.getMessage());
        }
    }

    private void pesquisarMusica() {
        String searchText = idTextField.getText().trim();
        String searchNome = searchNomeTextField.getText().trim();
        String searchArtista = searchArtistaTextField.getText().trim();

        try {
            if (!searchText.isEmpty()) {
                int ID = Integer.parseInt(searchText.replaceAll("[^0-9]", ""));
                Musica musica = null;
                if (searchText.contains("B")) {
                    Logs.Succeed("Pesquisando na B-Tree");
                    musica = Arq.searchBtree(ID);
                } else if (searchText.contains("H")) {
                    Logs.Succeed("Pesquisando em Hash");
                    musica = Arq.searchHash(ID);
                } else {
                    Logs.Succeed("Pesquisando por ID sequencial");
                    musica = Arq.FindSongID(ID);
                }

                if (musica != null) {
                    Logs.Details("Musica encontrada:\n" + musica.toString());
                    limparTabela();
                    displaySong(musica);
                } else {
                    Logs.Alert("Música não encontrada!");
                }
            } else if (!searchNome.isEmpty()) {
                searchByNome(searchNome);
            } else if (!searchArtista.isEmpty()) {
                searchByArtista(searchArtista);
            } else {
                searchByGenre(generoTextField.getText());
            }
            limparCampos();
        } catch (NumberFormatException ex) {
            Logs.Alert("Erro ao pesquisar a música:\nNúmero esperado " + ex.getMessage());
        } catch (Exception ex) {
            Logs.Alert("Erro ao pesquisar a música:\n" + ex.getMessage());
        }
    }

    private void searchByNome(String nome) {
        limparTabela();
        try{
            Musica[] musicas = BoyerMoore.FindPattern(nome);
            for (Musica musica : musicas) {
                if (musica != null) {
                    displaySong(musica);
                }
            }
        }catch (Exception e){
            Logs.Alert("Erro ao pesquisar por nome: " + e.getMessage() + '\n' + e.getClass()
            + '\n' + e.getLocalizedMessage());
        }
        
        
    }

    private void searchByArtista(String artista) {
        limparTabela();
        Musica[] musicas = BoyerMoore.FindPattern(artista);
        for (Musica musica : musicas) {
            if (musica != null) {
                displaySong(musica);
            }
        }
    }

    private void deletarMusica() {
        try {
            int ID = Integer.parseInt(idTextField.getText());
            Logs.Details("Status de deletar: " + Arq.DeleteSong(ID));
            limparCampos();
            listarMusicas();
        } catch (NumberFormatException ex) {
            Logs.Alert("Erro ao deletar a música:\nNúmero esperado " + ex.getMessage() + "\n" + ex.getClass());
        } catch (Exception ex) {
            Logs.Alert("Erro ao deletar a música:\n" + ex.getMessage() + "\n" + ex.getClass());
        }
    }

    private void listarMusicas() {
        limparTabela();
        Arq.IniciarLeituraSequencial();
        Musica musica;
        boolean flag = true;
        while (flag) {
            try {
                musica = Arq.getNextRegistro();
                if (musica != null) {
                    displaySong(musica);
                } else {
                    flag = false;
                }
            } catch (Exception e) {
                flag = false;
            }
        }
    }

    private void gerarMockData() {
        MockData mock = new MockData();
        Musica musica = mock.generateRandomData();
        Logs.Details("Mock Data gerado:\n" + musica.toString());

        nomeTextField.setText(musica.getNome());
        artistaTextField.setText(musica.getArtista());
        popularidadeTextField.setText(String.valueOf(musica.getPopularidade()));
        dataLancamentoTextField.setText(musica.getDataLancamento());
        generoTextField.setText(musica.getGenero().replace(',', ';'));
        dancabilidadeTextField.setText(String.valueOf(musica.getDancabilidade()));
        hashTextField.setText(musica.getHash());

        listarMusicas();
    }

    private void listarRegistrosInvalidos() {
        try {
            int idToRecover = Integer.parseInt(idTextField.getText());
            Arq.IniciarLeituraSequencial();
            if (idToRecover != 0) {
                if (Arq.Recover(idToRecover)) {
                    Logs.Succeed("Registro recuperado com sucesso!");
                } else {
                    Logs.KindaAlert("Registro não encontrado!");
                }
                idTextField.setText("");
                listarRegistrosInvalidos();
                return;
            }
        } catch (Exception error) {
            Logs.Details("ID não preenchido para recover.");
        }

        limparTabela();
        Arq.IniciarLeituraSequencial();
        Musica musica;
        while ((musica = Arq.getInvalidRegister(null)) != null) {
            displaySong(musica);
        }
    }

    private void limparCampos() {
        idTextField.setText("");
        nomeTextField.setText("");
        artistaTextField.setText("");
        popularidadeTextField.setText("");
        dataLancamentoTextField.setText("");
        generoTextField.setText("");
        dancabilidadeTextField.setText("");
        hashTextField.setText("");
        searchNomeTextField.setText("");
        searchArtistaTextField.setText("");
    }

    private void limparTabela() {
        tableModel.setRowCount(0);
    }

    private void displaySong(Musica musica) {
        tableModel.addRow(new Object[] {
                musica.getId(),
                musica.getNome(),
                musica.getArtista(),
                musica.getPopularidade(),
                musica.getDataLancamento(),
                musica.getGenero(),
                musica.getDancabilidade(),
                musica.getHash()
        });
    }

    public static void iniciar() {
        Logs.Succeed("=================== Programa Iniciado ===================");

        SwingUtilities.invokeLater(InterfaceMenu::new);

        Logs.Succeed("Interface gráfica iniciada com sucesso e Operando!");
        Logs.KindaAlert("Coloque H ou B antes do ID para pesquisar na Hash ou B-Tree");
        while (Running) {
        }
    }

    public boolean fechar() {
        try {
            dispose();
            Running = false;
            Logs.Succeed("Janela fechada com sucesso!");
            return true;
        } catch (Exception e) {
            Logs.Alert("Erro ao fechar a janela:\nErro: " + e.getMessage());
            return false;
        }
    }

    private void searchByGenre(String genre) {
        limparTabela();
        ArrayList<MetaIndice> musicas = IndiceInvertido.searchGenre(genre);
        ArrayList<Musica> musicasEncontradas = new ArrayList<>();
        for (MetaIndice metaIndice : musicas) {
            Musica musica = Arq.getByIndice(metaIndice.getPosicao());
            if (musica != null) {
                musicasEncontradas.add(musica);
            }
        }
        for (Musica musica : musicasEncontradas) {
            if (musica != null) {
                displaySong(musica);
            }
        }
    }
}
