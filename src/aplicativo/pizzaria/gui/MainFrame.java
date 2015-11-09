/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativo.pizzaria.gui;

import aplicativo.pizzaria.bo.ProdutoBO;
import aplicativo.pizzaria.bo.UserBO;
import aplicativo.pizzaria.dto.UserDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.main.Main;
import aplicativo.pizzaria.util.MensagensUtil;
import java.awt.Component;
import java.awt.Container;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eduardo
 */
public class MainFrame extends javax.swing.JFrame {

    Thread hora = null;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        String tipo = null;
        
        if (Main.getUsuarioLogado() != null) {
            try {
                tipo = UserBO.tipo(Main.getUsuarioLogado().getTipo());
            } catch (NegocioException ex) {
                ex.printStackTrace();
                MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
            }
            LbNome.setText(Main.getUsuarioLogado().getNome());
            LbTipo.setText(tipo);
            atualizarTabela();
            iniThreadHora();
        } else {
            MensagensUtil.addMsg(MainFrame.this, "Usuário não logado.");
            MainFrame.this.dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        }

    }

    public void atualizarCampos(Integer id, int tipo, String nome, String login) {
        TxtIdA.setText(String.valueOf(id));
        CBoxTipoA.setSelectedIndex(tipo);
        TxtNomeA.setText(nome);
        TxtLoginA.setText(login);
    }

    public void limparTodosCampos(Container container) {
        Component components[] = container.getComponents();
        for (Component component : components) {
            if (component instanceof JFormattedTextField) {
                JFormattedTextField field = (JFormattedTextField) component;
                field.setValue(null);
            } else if (component instanceof JTextField) {
                JTextField field = (JTextField) component;
                field.setText("");
            } else if (component instanceof Container) {
                limparTodosCampos((Container) component);
            }
        }
    }

    private void atualizarTabela() {
        UserBO listaBo = new UserBO();
        List<UserDTO> lista = new ArrayList<>();
        DefaultTableModel tbl = (DefaultTableModel) TblUser.getModel();
        try {
            lista = listaBo.listar();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (UserDTO user : lista) {
                tbl.addRow(new String[1]);
                TblUser.setValueAt(user.getId(), i, 0);
                TblUser.setValueAt(user.getNome(), i, 1);
                TblUser.setValueAt(user.getLogin(), i, 2);
                try {
                    TblUser.setValueAt(UserBO.tipo(user.getTipo()), i, 3);
                } catch (NegocioException ex) {
                    ex.printStackTrace();
                    MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
                }
                i++;
            }
        } catch (NegocioException ex) {
            ex.printStackTrace();
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    public void atualizarTabela(List<UserDTO> consulta) {
        if (consulta != null) {
            DefaultTableModel tbl = (DefaultTableModel) TblUser.getModel();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (UserDTO user : consulta) {
                tbl.addRow(new String[1]);
                TblUser.setValueAt(user.getId(), i, 0);
                TblUser.setValueAt(user.getNome(), i, 1);
                TblUser.setValueAt(user.getLogin(), i, 2);
                try {
                    TblUser.setValueAt(UserBO.tipo(user.getTipo()), i, 3);
                } catch (NegocioException ex) {
                    ex.printStackTrace();
                    MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
                }
                i++;
            }
        } else {
            atualizarTabela();
        }
    }

    public void atualizarTabelaFiltro(List<UserDTO> consulta) {
        if (consulta != null) {
            DefaultTableModel tbl = (DefaultTableModel) TblUserFiltro.getModel();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (UserDTO user : consulta) {
                tbl.addRow(new String[1]);
                TblUserFiltro.setValueAt(user.getId(), i, 0);
                TblUserFiltro.setValueAt(user.getNome(), i, 1);
                TblUserFiltro.setValueAt(user.getLogin(), i, 2);
                try {
                    TblUserFiltro.setValueAt(UserBO.tipo(user.getTipo()), i, 3);
                } catch (NegocioException ex) {
                    ex.printStackTrace();
                    MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
                }
                i++;
            }
        } else {
            atualizarTabela();
        }
    }
    
    public void listarFiltrado() {
        List<UserDTO> lista = new ArrayList<>();
        String id = TxtIdB.getText();
        String nome = TxtNomeB.getText();
        String login = TxtLoginB.getText();
        UserBO buscarBO = new UserBO();
        try {
            Integer tipo = UserBO.escolherTipo(CBoxTipoB.getSelectedItem() + "");
            lista = buscarBO.busca(id, login, nome, tipo);
            atualizarTabelaFiltro(lista);
            Thread.sleep(1000);
        } catch (NegocioException | InterruptedException ex) {
            ex.printStackTrace();
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    private void iniThreadHora() {
        hora = new Thread(new HorarioUtil());
        hora.start();
    }

    private class HorarioUtil implements Runnable {

        private SimpleDateFormat sdf = null;

        public HorarioUtil() {
            sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        }

        @Override
        public void run() {
            while (true) {
                hora();
            }
        }

        public void hora() {
            LbHora.setText(sdf.format(new Date()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TabUsuarios = new javax.swing.JTabbedPane();
        CadastroUsuario = new javax.swing.JPanel();
        LabLoginC = new javax.swing.JLabel();
        LabSenhaC = new javax.swing.JLabel();
        LabSenhaRC = new javax.swing.JLabel();
        LabTipoC = new javax.swing.JLabel();
        LabNomeC = new javax.swing.JLabel();
        TxtUserC = new javax.swing.JTextField();
        TxtSenhaC = new javax.swing.JPasswordField();
        TxtSenhaRC = new javax.swing.JPasswordField();
        CBoxTipoC = new javax.swing.JComboBox();
        TxtNomeC = new javax.swing.JTextField();
        ButCadastroC = new javax.swing.JButton();
        ListaUsuario = new javax.swing.JPanel();
        ScrollPaneTab = new javax.swing.JScrollPane();
        TblUser = new javax.swing.JTable();
        ButAtualizarL = new javax.swing.JButton();
        BuscaUsuario = new javax.swing.JPanel();
        LabIdB = new javax.swing.JLabel();
        LabLoginB = new javax.swing.JLabel();
        LabTipoB = new javax.swing.JLabel();
        LabNomeB = new javax.swing.JLabel();
        TxtIdB = new javax.swing.JTextField();
        TxtLoginB = new javax.swing.JTextField();
        CBoxTipoB = new javax.swing.JComboBox();
        TxtNomeB = new javax.swing.JTextField();
        ScrollPaneTab1 = new javax.swing.JScrollPane();
        TblUserFiltro = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        AlteraUsuario = new javax.swing.JPanel();
        LabIdA = new javax.swing.JLabel();
        LabLoginA = new javax.swing.JLabel();
        LabSenhaA = new javax.swing.JLabel();
        LabSenhaNA = new javax.swing.JLabel();
        LabSenhaNRA = new javax.swing.JLabel();
        LabTipoA = new javax.swing.JLabel();
        LabNomeA = new javax.swing.JLabel();
        TxtLoginA = new javax.swing.JTextField();
        TxtIdA = new javax.swing.JTextField();
        TxtSenhaNA = new javax.swing.JPasswordField();
        TxtSenhaN2A = new javax.swing.JPasswordField();
        CBoxTipoA = new javax.swing.JComboBox();
        TxtNomeA = new javax.swing.JTextField();
        ButExcluirA = new javax.swing.JButton();
        ButAlterarA = new javax.swing.JButton();
        TxtSenhaA = new javax.swing.JPasswordField();
        TabProdutos = new javax.swing.JTabbedPane();
        CadastroProduto = new javax.swing.JPanel();
        LabNomeP = new javax.swing.JLabel();
        TxtNomeP = new javax.swing.JTextField();
        LabTipoP = new javax.swing.JLabel();
        LabTamP = new javax.swing.JLabel();
        CBoxTipoCP = new javax.swing.JComboBox();
        CBoxTamCP = new javax.swing.JComboBox();
        LabDescP = new javax.swing.JLabel();
        TxtDescP = new javax.swing.JTextField();
        LabValorP = new javax.swing.JLabel();
        TxtValorP = new javax.swing.JTextField();
        LabRS = new javax.swing.JLabel();
        ButCadastroP = new javax.swing.JButton();
        ListaProduto = new javax.swing.JPanel();
        ScrollPaneTab5 = new javax.swing.JScrollPane();
        TblProduto = new javax.swing.JTable();
        ButAtualizarCL1 = new javax.swing.JButton();
        AlteraProduto = new javax.swing.JPanel();
        TabClientes = new javax.swing.JTabbedPane();
        CadastroCliente = new javax.swing.JPanel();
        ListaCliente = new javax.swing.JPanel();
        ScrollPaneTab4 = new javax.swing.JScrollPane();
        TblCliente = new javax.swing.JTable();
        ButAtualizarCL = new javax.swing.JButton();
        AlteraCliente = new javax.swing.JPanel();
        TabPedidos = new javax.swing.JTabbedPane();
        Novo = new javax.swing.JPanel();
        ListaPedido = new javax.swing.JPanel();
        ScrollPaneTab2 = new javax.swing.JScrollPane();
        TblPedido = new javax.swing.JTable();
        ButAtualizarPL = new javax.swing.JButton();
        AlteraPedido = new javax.swing.JPanel();
        Infos = new javax.swing.JPanel();
        Usuario = new javax.swing.JPanel();
        LbNome = new javax.swing.JLabel();
        Tipo = new javax.swing.JPanel();
        LbTipo = new javax.swing.JLabel();
        Hora = new javax.swing.JPanel();
        LbHora = new javax.swing.JLabel();
        Menu = new javax.swing.JPanel();
        ButUsuarios = new javax.swing.JButton();
        ButProdutos = new javax.swing.JButton();
        ButClientes = new javax.swing.JButton();
        ButPedidos = new javax.swing.JButton();
        Painel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        ButSair = new javax.swing.JButton();
        ButLogout = new javax.swing.JButton();
        ButLimpar = new javax.swing.JButton();

        TabUsuarios.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        TabUsuarios.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        TabUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TabUsuarios.setPreferredSize(new java.awt.Dimension(500, 600));

        LabLoginC.setText("Usuário");

        LabSenhaC.setText("Senha");

        LabSenhaRC.setText("Repita senha");

        LabTipoC.setText("Tipo");

        LabNomeC.setText("Nome");

        CBoxTipoC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "Atendente", "Pizzaiolo", "Entregador" }));

        ButCadastroC.setText("Cadastrar");
        ButCadastroC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButCadastroCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CadastroUsuarioLayout = new javax.swing.GroupLayout(CadastroUsuario);
        CadastroUsuario.setLayout(CadastroUsuarioLayout);
        CadastroUsuarioLayout.setHorizontalGroup(
            CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CadastroUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CadastroUsuarioLayout.createSequentialGroup()
                        .addComponent(LabSenhaRC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtSenhaRC, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CadastroUsuarioLayout.createSequentialGroup()
                        .addComponent(LabTipoC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CBoxTipoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroUsuarioLayout.createSequentialGroup()
                        .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabLoginC)
                            .addComponent(LabSenhaC)
                            .addComponent(LabNomeC))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(TxtUserC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                .addComponent(TxtSenhaC))
                            .addComponent(TxtNomeC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroUsuarioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButCadastroC))
        );

        CadastroUsuarioLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TxtNomeC, TxtSenhaC, TxtSenhaRC, TxtUserC});

        CadastroUsuarioLayout.setVerticalGroup(
            CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CadastroUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeC)
                    .addComponent(TxtNomeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabLoginC)
                    .addComponent(TxtUserC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabSenhaC)
                    .addComponent(TxtSenhaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabSenhaRC)
                    .addComponent(TxtSenhaRC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTipoC)
                    .addComponent(CBoxTipoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButCadastroC))
        );

        TabUsuarios.addTab("Cadastrar", CadastroUsuario);

        ScrollPaneTab.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Login", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblUserMouseClicked(evt);
            }
        });
        ScrollPaneTab.setViewportView(TblUser);

        ButAtualizarL.setText("Atualizar");
        ButAtualizarL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ListaUsuarioLayout = new javax.swing.GroupLayout(ListaUsuario);
        ListaUsuario.setLayout(ListaUsuarioLayout);
        ListaUsuarioLayout.setHorizontalGroup(
            ListaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaUsuarioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButAtualizarL))
            .addGroup(ListaUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        ListaUsuarioLayout.setVerticalGroup(
            ListaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarL))
        );

        TabUsuarios.addTab("Listar", ListaUsuario);

        LabIdB.setText("ID");

        LabLoginB.setText("Login");

        LabTipoB.setText("Tipo");

        LabNomeB.setText("Nome");

        TxtIdB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtIdBActionPerformed(evt);
            }
        });

        TxtLoginB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtLoginBActionPerformed(evt);
            }
        });

        CBoxTipoB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nenhum", "Administrador", "Atendente", "Pizzaiolo", "Entregador" }));
        CBoxTipoB.setToolTipText("");
        CBoxTipoB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBoxTipoBActionPerformed(evt);
            }
        });

        TxtNomeB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNomeBActionPerformed(evt);
            }
        });

        ScrollPaneTab1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TblUserFiltro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Login", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblUserFiltro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblUserFiltroMouseClicked(evt);
            }
        });
        ScrollPaneTab1.setViewportView(TblUserFiltro);

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BuscaUsuarioLayout = new javax.swing.GroupLayout(BuscaUsuario);
        BuscaUsuario.setLayout(BuscaUsuarioLayout);
        BuscaUsuarioLayout.setHorizontalGroup(
            BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscaUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BuscaUsuarioLayout.createSequentialGroup()
                        .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabIdB)
                            .addComponent(LabLoginB)
                            .addComponent(LabTipoB)
                            .addComponent(LabNomeB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CBoxTipoB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtLoginB, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TxtNomeB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtIdB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ScrollPaneTab1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BuscaUsuarioLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );

        BuscaUsuarioLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TxtLoginB, TxtNomeB});

        BuscaUsuarioLayout.setVerticalGroup(
            BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscaUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabIdB)
                    .addComponent(TxtIdB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTipoB)
                    .addComponent(CBoxTipoB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeB)
                    .addComponent(TxtNomeB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabLoginB)
                    .addComponent(TxtLoginB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        TabUsuarios.addTab("Buscar", BuscaUsuario);

        LabIdA.setText("ID");

        LabLoginA.setText("Login");

        LabSenhaA.setText("Senha");

        LabSenhaNA.setText("Nova senha");

        LabSenhaNRA.setText("Repita senha");

        LabTipoA.setText("Tipo");

        LabNomeA.setText("Nome");

        TxtIdA.setEditable(false);

        CBoxTipoA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "Atendente", "Pizzaiolo", "Entregador" }));

        ButExcluirA.setText("Excluir");
        ButExcluirA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButExcluirAActionPerformed(evt);
            }
        });

        ButAlterarA.setText("Alterar");
        ButAlterarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAlterarAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AlteraUsuarioLayout = new javax.swing.GroupLayout(AlteraUsuario);
        AlteraUsuario.setLayout(AlteraUsuarioLayout);
        AlteraUsuarioLayout.setHorizontalGroup(
            AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabIdA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtIdA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabTipoA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CBoxTipoA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabLoginA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtLoginA, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabNomeA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtNomeA, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabSenhaA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtSenhaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabSenhaNRA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtSenhaN2A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabSenhaNA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtSenhaNA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraUsuarioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButExcluirA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAlterarA))
        );

        AlteraUsuarioLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TxtLoginA, TxtNomeA, TxtSenhaA, TxtSenhaN2A, TxtSenhaNA});

        AlteraUsuarioLayout.setVerticalGroup(
            AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabIdA)
                    .addComponent(TxtIdA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTipoA)
                    .addComponent(CBoxTipoA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeA)
                    .addComponent(TxtNomeA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabLoginA)
                    .addComponent(TxtLoginA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabSenhaA)
                    .addComponent(TxtSenhaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabSenhaNA)
                    .addComponent(TxtSenhaNA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabSenhaNRA)
                    .addComponent(TxtSenhaN2A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButAlterarA)
                    .addComponent(ButExcluirA)))
        );

        TabUsuarios.addTab("Alterar", AlteraUsuario);

        TabProdutos.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        TabProdutos.setPreferredSize(TabUsuarios.getPreferredSize());

        LabNomeP.setText("Nome");

        LabTipoP.setText("Tipo");

        LabTamP.setText("Tamanho");

        CBoxTipoCP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escolha", "Pizza", "Bebida", "Sobremesa" }));
        CBoxTipoCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBoxTipoCPActionPerformed(evt);
            }
        });

        CBoxTamCP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escolha" }));

        LabDescP.setText("Descrição");

        LabValorP.setText("Valor");

        LabRS.setText("R$");

        ButCadastroP.setText("Cadastrar");
        ButCadastroP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButCadastroPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CadastroProdutoLayout = new javax.swing.GroupLayout(CadastroProduto);
        CadastroProduto.setLayout(CadastroProdutoLayout);
        CadastroProdutoLayout.setHorizontalGroup(
            CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CadastroProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CadastroProdutoLayout.createSequentialGroup()
                        .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabTipoP)
                            .addComponent(LabTamP)
                            .addComponent(LabNomeP))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtNomeP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBoxTipoCP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBoxTamCP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CadastroProdutoLayout.createSequentialGroup()
                        .addComponent(LabDescP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtDescP, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroProdutoLayout.createSequentialGroup()
                        .addComponent(LabValorP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabRS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtValorP, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroProdutoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButCadastroP))
        );

        CadastroProdutoLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {CBoxTamCP, CBoxTipoCP});

        CadastroProdutoLayout.setVerticalGroup(
            CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CadastroProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeP)
                    .addComponent(TxtNomeP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTipoP)
                    .addComponent(CBoxTipoCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTamP)
                    .addComponent(CBoxTamCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabDescP)
                    .addComponent(TxtDescP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabValorP)
                    .addComponent(TxtValorP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabRS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(ButCadastroP))
        );

        TabProdutos.addTab("Cadastrar", CadastroProduto);

        ScrollPaneTab5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Login", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblProdutoMouseClicked(evt);
            }
        });
        ScrollPaneTab5.setViewportView(TblProduto);

        ButAtualizarCL1.setText("Atualizar");
        ButAtualizarCL1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarCL1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ListaProdutoLayout = new javax.swing.GroupLayout(ListaProduto);
        ListaProduto.setLayout(ListaProdutoLayout);
        ListaProdutoLayout.setHorizontalGroup(
            ListaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPaneTab5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(ListaProdutoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButAtualizarCL1))
        );
        ListaProdutoLayout.setVerticalGroup(
            ListaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab5, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarCL1))
        );

        TabProdutos.addTab("Listar", ListaProduto);

        javax.swing.GroupLayout AlteraProdutoLayout = new javax.swing.GroupLayout(AlteraProduto);
        AlteraProduto.setLayout(AlteraProdutoLayout);
        AlteraProdutoLayout.setHorizontalGroup(
            AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        AlteraProdutoLayout.setVerticalGroup(
            AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        TabProdutos.addTab("Alterar", AlteraProduto);

        TabClientes.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        TabClientes.setPreferredSize(TabUsuarios.getPreferredSize());

        javax.swing.GroupLayout CadastroClienteLayout = new javax.swing.GroupLayout(CadastroCliente);
        CadastroCliente.setLayout(CadastroClienteLayout);
        CadastroClienteLayout.setHorizontalGroup(
            CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        CadastroClienteLayout.setVerticalGroup(
            CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        TabClientes.addTab("Cadastrar", CadastroCliente);

        ScrollPaneTab4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Login", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblClienteMouseClicked(evt);
            }
        });
        ScrollPaneTab4.setViewportView(TblCliente);

        ButAtualizarCL.setText("Atualizar");
        ButAtualizarCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarCLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ListaClienteLayout = new javax.swing.GroupLayout(ListaCliente);
        ListaCliente.setLayout(ListaClienteLayout);
        ListaClienteLayout.setHorizontalGroup(
            ListaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPaneTab4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(ListaClienteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButAtualizarCL))
        );
        ListaClienteLayout.setVerticalGroup(
            ListaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab4, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarCL))
        );

        TabClientes.addTab("Listar", ListaCliente);

        javax.swing.GroupLayout AlteraClienteLayout = new javax.swing.GroupLayout(AlteraCliente);
        AlteraCliente.setLayout(AlteraClienteLayout);
        AlteraClienteLayout.setHorizontalGroup(
            AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        AlteraClienteLayout.setVerticalGroup(
            AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        TabClientes.addTab("Alterar", AlteraCliente);

        TabPedidos.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        TabPedidos.setPreferredSize(TabUsuarios.getPreferredSize());

        javax.swing.GroupLayout NovoLayout = new javax.swing.GroupLayout(Novo);
        Novo.setLayout(NovoLayout);
        NovoLayout.setHorizontalGroup(
            NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 299, Short.MAX_VALUE)
        );
        NovoLayout.setVerticalGroup(
            NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        TabPedidos.addTab("Novo", Novo);

        ScrollPaneTab2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Login", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblPedidoMouseClicked(evt);
            }
        });
        ScrollPaneTab2.setViewportView(TblPedido);

        ButAtualizarPL.setText("Atualizar");
        ButAtualizarPL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarPLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ListaPedidoLayout = new javax.swing.GroupLayout(ListaPedido);
        ListaPedido.setLayout(ListaPedidoLayout);
        ListaPedidoLayout.setHorizontalGroup(
            ListaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPaneTab2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(ListaPedidoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButAtualizarPL))
        );
        ListaPedidoLayout.setVerticalGroup(
            ListaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaPedidoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab2, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarPL))
        );

        TabPedidos.addTab("Listar", ListaPedido);

        javax.swing.GroupLayout AlteraPedidoLayout = new javax.swing.GroupLayout(AlteraPedido);
        AlteraPedido.setLayout(AlteraPedidoLayout);
        AlteraPedidoLayout.setHorizontalGroup(
            AlteraPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 299, Short.MAX_VALUE)
        );
        AlteraPedidoLayout.setVerticalGroup(
            AlteraPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        TabPedidos.addTab("Alterar", AlteraPedido);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pizzaria");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        Usuario.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuário"));

        LbNome.setText("Nome");

        javax.swing.GroupLayout UsuarioLayout = new javax.swing.GroupLayout(Usuario);
        Usuario.setLayout(UsuarioLayout);
        UsuarioLayout.setHorizontalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuarioLayout.createSequentialGroup()
                .addComponent(LbNome)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        UsuarioLayout.setVerticalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LbNome)
        );

        Tipo.setBorder(javax.swing.BorderFactory.createTitledBorder("Perfil"));

        LbTipo.setText("Tipo");

        javax.swing.GroupLayout TipoLayout = new javax.swing.GroupLayout(Tipo);
        Tipo.setLayout(TipoLayout);
        TipoLayout.setHorizontalGroup(
            TipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TipoLayout.createSequentialGroup()
                .addComponent(LbTipo)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        TipoLayout.setVerticalGroup(
            TipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LbTipo, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        Hora.setBorder(javax.swing.BorderFactory.createTitledBorder("Horário"));

        LbHora.setText("DD/MM/AA hh:mm:ss");

        javax.swing.GroupLayout HoraLayout = new javax.swing.GroupLayout(Hora);
        Hora.setLayout(HoraLayout);
        HoraLayout.setHorizontalGroup(
            HoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HoraLayout.createSequentialGroup()
                .addComponent(LbHora)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HoraLayout.setVerticalGroup(
            HoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LbHora, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout InfosLayout = new javax.swing.GroupLayout(Infos);
        Infos.setLayout(InfosLayout);
        InfosLayout.setHorizontalGroup(
            InfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 243, Short.MAX_VALUE)
                .addComponent(Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        InfosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Tipo, Usuario});

        InfosLayout.setVerticalGroup(
            InfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Menu.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Menu"))));

        ButUsuarios.setText("Usuários");
        ButUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButUsuariosActionPerformed(evt);
            }
        });

        ButProdutos.setText("Produtos");
        ButProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButProdutosActionPerformed(evt);
            }
        });

        ButClientes.setText("Clientes");
        ButClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButClientesActionPerformed(evt);
            }
        });

        ButPedidos.setText("Pedidos");
        ButPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButPedidosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ButUsuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButProdutos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButClientes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButPedidos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButUsuarios)
                    .addComponent(ButProdutos)
                    .addComponent(ButClientes)
                    .addComponent(ButPedidos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Painel.setLayout(new java.awt.CardLayout());

        ButSair.setText("Sair");
        ButSair.setToolTipText("");
        ButSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButSairActionPerformed(evt);
            }
        });

        ButLogout.setText("Logout");
        ButLogout.setToolTipText("");
        ButLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButLogoutActionPerformed(evt);
            }
        });

        ButLimpar.setText("Limpar");
        ButLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButLimpar)
                .addGap(6, 6, 6)
                .addComponent(ButLogout)
                .addGap(6, 6, 6)
                .addComponent(ButSair))
            .addComponent(jSeparator1)
            .addComponent(Infos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Infos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Painel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButLimpar)
                    .addComponent(ButLogout)
                    .addComponent(ButSair)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_ButSairActionPerformed

    private void TblUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblUserMouseClicked
        if (Main.getUsuarioLogado().getTipo() < 2) {
            Integer linha = TblUser.getSelectedRow();
            TabUsuarios.setSelectedComponent(AlteraUsuario);
            try {
                atualizarCampos((Integer) TblUser.getValueAt(linha, 0),
                        UserBO.escolherTipo((String) TblUser.getValueAt(linha, 3)),
                        (String) TblUser.getValueAt(linha, 1),
                        (String) TblUser.getValueAt(linha, 2));
                /*TxtIdA.setText(String.valueOf((Integer) TblUser.getValueAt(linha, 0)));
                 CBoxTipoA.setSelectedIndex(UserBO.escolherTipo((String) TblUser.getValueAt(linha, 3)));
                 TxtNomeA.setText((String) TblUser.getValueAt(linha, 1));
                 TxtLoginA.setText((String) TblUser.getValueAt(linha, 2));*/
            } catch (NegocioException ex) {
                ex.printStackTrace();
                MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
            }
        }
    }//GEN-LAST:event_TblUserMouseClicked

    private void ButCadastroCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButCadastroCActionPerformed
        String nome = TxtNomeC.getText();
        String login = TxtUserC.getText();
        String senha = String.copyValueOf(TxtSenhaC.getPassword());
        String senhar = String.copyValueOf(TxtSenhaRC.getPassword());
        String tipo = CBoxTipoC.getSelectedItem() + "";
        UserBO cadastroBo = new UserBO();
        try {
            if (cadastroBo.cadastrar(login, nome, senha, senhar, tipo)) {
                MensagensUtil.addMsg(MainFrame.this, "Cadastro efetuado com sucesso!");
                TabUsuarios.setSelectedComponent(ListaUsuario);
                atualizarTabela();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha no cadastro.");
            }
        } catch (NegocioException ex) {
            ex.printStackTrace();
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButCadastroCActionPerformed

    private void ButAtualizarLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarLActionPerformed
        atualizarTabela();
    }//GEN-LAST:event_ButAtualizarLActionPerformed

    private void ButExcluirAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButExcluirAActionPerformed
        String id = TxtIdA.getText();
        String senha = String.copyValueOf(TxtSenhaA.getPassword());
        UserBO excluirBo = new UserBO();
        try {
            if (excluirBo.excluir(id, senha)) {
                MensagensUtil.addMsg(MainFrame.this, "Excluido com sucesso!");
                TabUsuarios.setSelectedComponent(ListaUsuario);
                atualizarTabela();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha ao excluir.");
            }
        } catch (NegocioException ex) {
            ex.printStackTrace();
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButExcluirAActionPerformed

    private void ButAlterarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAlterarAActionPerformed
        String id = TxtIdA.getText();
        String login = TxtLoginA.getText();
        String nome = TxtNomeA.getText();
        String senha = String.copyValueOf(TxtSenhaA.getPassword());
        String tipo = CBoxTipoA.getSelectedItem() + "";
        String senhan = String.copyValueOf(TxtSenhaNA.getPassword());
        String senhan2 = String.copyValueOf(TxtSenhaN2A.getPassword());
        UserBO alterarBo = new UserBO();
        try {
            if (alterarBo.alterar(id, login, nome, senha, tipo, senhan, senhan2)) {
                MensagensUtil.addMsg(MainFrame.this, "Alterado com sucesso!");
                TabUsuarios.setSelectedComponent(ListaUsuario);
                atualizarTabela();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha na alteração.");
            }
        } catch (NegocioException ex) {
            ex.printStackTrace();
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButAlterarAActionPerformed

    private void ButLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButLogoutActionPerformed
        this.dispose();
        LoginFrame login = new LoginFrame();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        Main.setUsuarioLogado(null);
    }//GEN-LAST:event_ButLogoutActionPerformed

    private void TblPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblPedidoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblPedidoMouseClicked

    private void ButAtualizarPLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarPLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButAtualizarPLActionPerformed

    private void TblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblClienteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblClienteMouseClicked

    private void ButAtualizarCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarCLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButAtualizarCLActionPerformed

    private void ButLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButLimparActionPerformed
        limparTodosCampos(Painel);
    }//GEN-LAST:event_ButLimparActionPerformed

    private void TblProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblProdutoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblProdutoMouseClicked

    private void ButAtualizarCL1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarCL1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButAtualizarCL1ActionPerformed

    private void CBoxTipoCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBoxTipoCPActionPerformed
        CBoxTamCP.removeAllItems();
        if (CBoxTipoCP.getSelectedIndex() == 0) {
            CBoxTamCP.addItem("Escolha");
        }
        if (CBoxTipoCP.getSelectedIndex() == 1) {
            CBoxTamCP.addItem("P");
            CBoxTamCP.addItem("M");
            CBoxTamCP.addItem("G");
            CBoxTamCP.addItem("F");
        }
        if (CBoxTipoCP.getSelectedIndex() == 2) {
            CBoxTamCP.addItem("Lata");
            CBoxTamCP.addItem("1L");
            CBoxTamCP.addItem("1,5L");
            CBoxTamCP.addItem("2L");
            CBoxTamCP.addItem("Copo");
            CBoxTamCP.addItem("Jarra");
        }
        if (CBoxTipoCP.getSelectedIndex() == 3) {
            CBoxTamCP.addItem("Unico");
        }
    }//GEN-LAST:event_CBoxTipoCPActionPerformed

    private void ButCadastroPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButCadastroPActionPerformed
        String nome = TxtNomeP.getText();
        String tam = CBoxTamCP.getSelectedItem() + "";
        String tipo = CBoxTipoCP.getSelectedItem() + "";
        String desc = TxtDescP.getText();
        String valor = TxtValorP.getText();
        ProdutoBO produtoBo = new ProdutoBO();
        try {
            if (produtoBo.cadastrar(nome, tam, desc, valor, tipo)) {
                MensagensUtil.addMsg(MainFrame.this, "Cadastro efetuado com sucesso!");
                TabProdutos.setSelectedComponent(ListaProduto);
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha no cadastro.");
            }
        } catch (NegocioException ex) {
            ex.printStackTrace();
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButCadastroPActionPerformed

    private void TblUserFiltroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblUserFiltroMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblUserFiltroMouseClicked

    private void TxtIdBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtIdBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_TxtIdBActionPerformed

    private void TxtNomeBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNomeBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_TxtNomeBActionPerformed

    private void TxtLoginBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtLoginBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_TxtLoginBActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void CBoxTipoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBoxTipoBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_CBoxTipoBActionPerformed

    private void ButUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButUsuariosActionPerformed
        Painel.removeAll();
        Painel.add(TabUsuarios);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_ButUsuariosActionPerformed

    private void ButProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButProdutosActionPerformed
        Painel.removeAll();
        Painel.add(TabProdutos);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_ButProdutosActionPerformed

    private void ButClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButClientesActionPerformed
        Painel.removeAll();
        Painel.add(TabClientes);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_ButClientesActionPerformed

    private void ButPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButPedidosActionPerformed
        Painel.removeAll();
        Painel.add(TabPedidos);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_ButPedidosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AlteraCliente;
    private javax.swing.JPanel AlteraPedido;
    private javax.swing.JPanel AlteraProduto;
    private javax.swing.JPanel AlteraUsuario;
    private javax.swing.JPanel BuscaUsuario;
    private javax.swing.JButton ButAlterarA;
    private javax.swing.JButton ButAtualizarCL;
    private javax.swing.JButton ButAtualizarCL1;
    private javax.swing.JButton ButAtualizarL;
    private javax.swing.JButton ButAtualizarPL;
    private javax.swing.JButton ButCadastroC;
    private javax.swing.JButton ButCadastroP;
    private javax.swing.JButton ButClientes;
    private javax.swing.JButton ButExcluirA;
    private javax.swing.JButton ButLimpar;
    private javax.swing.JButton ButLogout;
    private javax.swing.JButton ButPedidos;
    private javax.swing.JButton ButProdutos;
    private javax.swing.JButton ButSair;
    private javax.swing.JButton ButUsuarios;
    private javax.swing.JComboBox CBoxTamCP;
    private javax.swing.JComboBox CBoxTipoA;
    private javax.swing.JComboBox CBoxTipoB;
    private javax.swing.JComboBox CBoxTipoC;
    private javax.swing.JComboBox CBoxTipoCP;
    private javax.swing.JPanel CadastroCliente;
    private javax.swing.JPanel CadastroProduto;
    private javax.swing.JPanel CadastroUsuario;
    private javax.swing.JPanel Hora;
    private javax.swing.JPanel Infos;
    private javax.swing.JLabel LabDescP;
    private javax.swing.JLabel LabIdA;
    private javax.swing.JLabel LabIdB;
    private javax.swing.JLabel LabLoginA;
    private javax.swing.JLabel LabLoginB;
    private javax.swing.JLabel LabLoginC;
    private javax.swing.JLabel LabNomeA;
    private javax.swing.JLabel LabNomeB;
    private javax.swing.JLabel LabNomeC;
    private javax.swing.JLabel LabNomeP;
    private javax.swing.JLabel LabRS;
    private javax.swing.JLabel LabSenhaA;
    private javax.swing.JLabel LabSenhaC;
    private javax.swing.JLabel LabSenhaNA;
    private javax.swing.JLabel LabSenhaNRA;
    private javax.swing.JLabel LabSenhaRC;
    private javax.swing.JLabel LabTamP;
    private javax.swing.JLabel LabTipoA;
    private javax.swing.JLabel LabTipoB;
    private javax.swing.JLabel LabTipoC;
    private javax.swing.JLabel LabTipoP;
    private javax.swing.JLabel LabValorP;
    private javax.swing.JLabel LbHora;
    private javax.swing.JLabel LbNome;
    private javax.swing.JLabel LbTipo;
    private javax.swing.JPanel ListaCliente;
    private javax.swing.JPanel ListaPedido;
    private javax.swing.JPanel ListaProduto;
    private javax.swing.JPanel ListaUsuario;
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel Novo;
    private javax.swing.JPanel Painel;
    private javax.swing.JScrollPane ScrollPaneTab;
    private javax.swing.JScrollPane ScrollPaneTab1;
    private javax.swing.JScrollPane ScrollPaneTab2;
    private javax.swing.JScrollPane ScrollPaneTab4;
    private javax.swing.JScrollPane ScrollPaneTab5;
    private javax.swing.JTabbedPane TabClientes;
    private javax.swing.JTabbedPane TabPedidos;
    private javax.swing.JTabbedPane TabProdutos;
    private javax.swing.JTabbedPane TabUsuarios;
    private javax.swing.JTable TblCliente;
    private javax.swing.JTable TblPedido;
    private javax.swing.JTable TblProduto;
    private javax.swing.JTable TblUser;
    private javax.swing.JTable TblUserFiltro;
    private javax.swing.JPanel Tipo;
    private javax.swing.JTextField TxtDescP;
    private javax.swing.JTextField TxtIdA;
    private javax.swing.JTextField TxtIdB;
    private javax.swing.JTextField TxtLoginA;
    private javax.swing.JTextField TxtLoginB;
    private javax.swing.JTextField TxtNomeA;
    private javax.swing.JTextField TxtNomeB;
    private javax.swing.JTextField TxtNomeC;
    private javax.swing.JTextField TxtNomeP;
    private javax.swing.JPasswordField TxtSenhaA;
    private javax.swing.JPasswordField TxtSenhaC;
    private javax.swing.JPasswordField TxtSenhaN2A;
    private javax.swing.JPasswordField TxtSenhaNA;
    private javax.swing.JPasswordField TxtSenhaRC;
    private javax.swing.JTextField TxtUserC;
    private javax.swing.JTextField TxtValorP;
    private javax.swing.JPanel Usuario;
    private javax.swing.JButton jButton1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
