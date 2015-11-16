/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativo.pizzaria.gui;

import aplicativo.pizzaria.bo.ClienteBO;
import aplicativo.pizzaria.bo.PedidoBO;
import aplicativo.pizzaria.bo.ProdutoBO;
import aplicativo.pizzaria.bo.UserBO;
import aplicativo.pizzaria.dao.ItemDAO;
import aplicativo.pizzaria.dto.ClienteDTO;
import aplicativo.pizzaria.dto.EnderecoDTO;
import aplicativo.pizzaria.dto.ItemPedido;
import aplicativo.pizzaria.dto.PedidoDTO;
import aplicativo.pizzaria.dto.ProdutoDTO;
import aplicativo.pizzaria.dto.UserDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import aplicativo.pizzaria.main.Main;
import aplicativo.pizzaria.util.MensagensUtil;
import java.awt.Component;
import java.awt.Container;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eduardo
 */
public class MainFrame extends javax.swing.JFrame {

    Thread hora = null;
    ClienteDTO clientePedindo = new ClienteDTO();
    List<ItemPedido> itensPedidos = new ArrayList<>();

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
                MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
            }
            LbNome.setText(Main.getUsuarioLogado().getNome());
            LbTipo.setText(tipo);
            MainFrame.this.atualizarTabelaUser();
            MainFrame.this.atualizarTabelaProduto();
            MainFrame.this.atualizarTabelaCliente();
            MainFrame.this.atualizarTabelaBuscarCliente();
            MainFrame.this.atualizarTabelaBuscaProduto();
            MainFrame.this.atualizarTabelaPedPend();
            MainFrame.this.atualizarTabelaPedFin();
            MainFrame.this.atualizarTabelaEntPend();
            MainFrame.this.atualizarTabelaEntFin();
            if (Main.getUsuarioLogado().getTipo() == 1) {
                Menu.remove(MenuUsuarios);
                Menu.remove(MenuClientes);
                Menu.remove(MenuProdutos);
                Menu.remove(MenuEntreg);
                Menu.remove(MenuPizza);
                Menu.repaint();
                Menu.validate();
            } else if (Main.getUsuarioLogado().getTipo() == 2) {
                Menu.remove(MenuUsuarios);
                Menu.remove(MenuClientes);
                Menu.remove(MenuProdutos);
                Menu.remove(MenuAtend);
                Menu.remove(MenuEntreg);
                Menu.repaint();
                Menu.validate();
            } else if (Main.getUsuarioLogado().getTipo() == 3) {
                Menu.remove(MenuUsuarios);
                Menu.remove(MenuClientes);
                Menu.remove(MenuProdutos);
                Menu.remove(MenuPizza);
                Menu.remove(MenuAtend);
                Menu.repaint();
                Menu.validate();
            }
            iniThreadHora();
            Painel.removeAll();
        } else {
            MensagensUtil.addMsg(MainFrame.this, "Usuário não logado.");
            MainFrame.this.dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        }

    }

    public void atualizarTxtUsuario(Integer id) {
        TxtIdA.setText(String.valueOf(id));
    }

    public void atualizarTxtCliente(Integer id) {
        TxtIdAC.setText(String.valueOf(id));
    }

    public void atualizarTxtProduto(Integer id) {
        TxtIdPA.setText(String.valueOf(id));
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

    private void atualizarTabelaUser() {
        UserBO listaBo = new UserBO();
        List<UserDTO> lista;
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
                    MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
                }
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    public void atualizarTabelaUser(List<UserDTO> consulta) {
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
                    MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
                }
                i++;
            }
        } else {
            MainFrame.this.atualizarTabelaUser();
        }
    }

    private void atualizarTabelaProduto() {
        ProdutoBO listaBo = new ProdutoBO();
        List<ProdutoDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblProduto.getModel();
        try {
            lista = listaBo.listar();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ProdutoDTO produto : lista) {
                tbl.addRow(new String[1]);
                TblProduto.setValueAt(produto.getId(), i, 0);
                TblProduto.setValueAt(produto.getNome(), i, 1);
                TblProduto.setValueAt(produto.getTipo(), i, 2);
                TblProduto.setValueAt(produto.getTamanho(), i, 3);
                TblProduto.setValueAt(produto.getValor(), i, 4);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    private void atualizarTabelaBuscaProduto() {
        ProdutoBO listaBo = new ProdutoBO();
        List<ProdutoDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblBuscaProduto.getModel();
        try {
            lista = listaBo.listar();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ProdutoDTO produto : lista) {
                tbl.addRow(new String[1]);
                TblBuscaProduto.setValueAt(produto.getId(), i, 0);
                TblBuscaProduto.setValueAt(produto.getNome(), i, 1);
                TblBuscaProduto.setValueAt(produto.getTipo(), i, 2);
                TblBuscaProduto.setValueAt(produto.getTamanho(), i, 3);
                TblBuscaProduto.setValueAt(produto.getValor(), i, 4);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    private void atualizarTabelaBuscaProduto(List<ProdutoDTO> lista) {
        if (lista != null) {
            DefaultTableModel tbl = (DefaultTableModel) TblBuscaProduto.getModel();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ProdutoDTO produto : lista) {
                tbl.addRow(new String[1]);
                TblBuscaProduto.setValueAt(produto.getId(), i, 0);
                TblBuscaProduto.setValueAt(produto.getNome(), i, 1);
                TblBuscaProduto.setValueAt(produto.getTipo(), i, 2);
                TblBuscaProduto.setValueAt(produto.getTamanho(), i, 3);
                TblBuscaProduto.setValueAt(produto.getValor(), i, 4);
                i++;
            }
        } else {
            atualizarTabelaBuscaProduto();
        }
    }

    private void atualizarTabelaItem(List<ItemPedido> lista) {
        if (lista != null) {
            DefaultTableModel tbl = (DefaultTableModel) TblItens.getModel();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ItemPedido item : lista) {
                tbl.addRow(new String[1]);
                TblItens.setValueAt(item.getProduto().getNome(), i, 0);
                TblItens.setValueAt(item.getProduto().getTipo(), i, 1);
                TblItens.setValueAt(item.getProduto().getTamanho(), i, 2);
                TblItens.setValueAt(item.getProduto().getValor(), i, 3);
                TblItens.setValueAt(item.getQuantidade(), i, 4);
                i++;
            }
        } else {
            atualizarTabelaBuscaProduto();
        }
    }

    public void atualizarTabelaFUser(List<UserDTO> consulta) {
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
                    MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
                }
                i++;
            }
        } else {
            MainFrame.this.atualizarTabelaUser();
        }
    }

    private void atualizarTabelaBuscarCliente() {
        ClienteBO clienteBo = new ClienteBO();
        List<ClienteDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblBuscaCliente.getModel();
        try {
            lista = clienteBo.listar();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ClienteDTO cliente : lista) {
                tbl.addRow(new String[1]);
                TblBuscaCliente.setValueAt(cliente.getId(), i, 0);
                TblBuscaCliente.setValueAt(cliente.getNome(), i, 1);
                TblBuscaCliente.setValueAt(cliente.getCpf(), i, 2);
                TblBuscaCliente.setValueAt(cliente.getNumero(), i, 3);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    public void atualizarTabelaBuscarCliente(List<ClienteDTO> consulta) {
        if (consulta != null) {
            DefaultTableModel tbl = (DefaultTableModel) TblBuscaCliente.getModel();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ClienteDTO cliente : consulta) {
                tbl.addRow(new String[1]);
                TblBuscaCliente.setValueAt(cliente.getId(), i, 0);
                TblBuscaCliente.setValueAt(cliente.getNome(), i, 1);
                TblBuscaCliente.setValueAt(cliente.getCpf(), i, 2);
                TblBuscaCliente.setValueAt(cliente.getNumero(), i, 3);
                i++;
            }
        } else {
            MainFrame.this.atualizarTabelaBuscarCliente();
        }
    }

    public void atualizarTabelaItensPedidos(List<ItemPedido> lista) {
        if (lista != null) {
            DefaultTableModel tbl = (DefaultTableModel) TblItensPend.getModel();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ItemPedido pedido : lista) {
                tbl.addRow(new String[1]);
                TblItensPend.setValueAt(pedido.getProduto().getNome(), i, 0);
                TblItensPend.setValueAt(pedido.getQuantidade(), i, 1);
                i++;
            }
        } else {

        }
    }

    private void atualizarTabelaPedPend() {
        PedidoBO pedBo = new PedidoBO();
        List<PedidoDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblPedidosPend.getModel();
        try {
            lista = pedBo.listarPendentes();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (PedidoDTO ped : lista) {
                tbl.addRow(new String[1]);
                TblPedidosPend.setValueAt(ped.getId(), i, 0);
                TblPedidosPend.setValueAt(ped.getData(), i, 1);
                TblPedidosPend.setValueAt(ped.getEstado(), i, 2);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    private void atualizarTabelaEntPend() {
        PedidoBO pedBo = new PedidoBO();
        List<PedidoDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblEntregasPend.getModel();
        try {
            lista = pedBo.listarFinalizados();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (PedidoDTO ped : lista) {
                tbl.addRow(new String[1]);
                TblEntregasPend.setValueAt(ped.getId(), i, 0);
                TblEntregasPend.setValueAt(ped.getData(), i, 1);
                TblEntregasPend.setValueAt(ped.getEstado(), i, 2);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    private void atualizarTabelaEntFin() {
        PedidoBO pedBo = new PedidoBO();
        List<PedidoDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblPedidosEnt.getModel();
        try {
            lista = pedBo.listarEntregues();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (PedidoDTO ped : lista) {
                tbl.addRow(new String[1]);
                TblPedidosEnt.setValueAt(ped.getId(), i, 0);
                TblPedidosEnt.setValueAt(ped.getData(), i, 1);
                TblPedidosEnt.setValueAt(ped.getEstado(), i, 2);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    private void atualizarTabelaPedFin() {
        PedidoBO pedBo = new PedidoBO();
        List<PedidoDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblPedidosFina.getModel();
        try {
            lista = pedBo.listarFinalizados();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (PedidoDTO ped : lista) {
                tbl.addRow(new String[1]);
                TblPedidosFina.setValueAt(ped.getId(), i, 0);
                TblPedidosFina.setValueAt(ped.getData(), i, 1);
                TblPedidosFina.setValueAt(ped.getEstado(), i, 2);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    private void atualizarTabelaCliente() {
        ClienteBO clienteBo = new ClienteBO();
        List<ClienteDTO> lista;
        DefaultTableModel tbl = (DefaultTableModel) TblCliente.getModel();
        try {
            lista = clienteBo.listar();
            while (tbl.getRowCount() > 0) {
                tbl.removeRow(0);
            }
            int i = 0;
            for (ClienteDTO cliente : lista) {
                tbl.addRow(new String[1]);
                TblCliente.setValueAt(cliente.getId(), i, 0);
                TblCliente.setValueAt(cliente.getNome(), i, 1);
                TblCliente.setValueAt(cliente.getCpf(), i, 2);
                TblCliente.setValueAt(cliente.getNumero(), i, 3);
                TblCliente.setValueAt(cliente.getEndereco().getRua(), i, 4);
                TblCliente.setValueAt(cliente.getEndereco().getBairro(), i, 5);
                TblCliente.setValueAt(cliente.getEndereco().getCidade(), i, 6);
                i++;
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        }
    }

    public void listarFiltrado() {
        List<UserDTO> lista;
        String id = TxtIdB.getText();
        String nome = TxtNomeB.getText();
        String login = TxtLoginB.getText();
        UserBO buscarBO = new UserBO();
        try {
            Integer tipo = UserBO.escolherTipo(CBoxTipoB.getSelectedItem() + "");
            lista = buscarBO.busca(id, login, nome, tipo);
            atualizarTabelaFUser(lista);
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

    private void logout() {
        this.dispose();
        LoginFrame login = new LoginFrame();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        Main.setUsuarioLogado(null);
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
                MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
            }
        }

        public String getHora() {
            return sdf + "";
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

        Infos = new javax.swing.JPanel();
        Usuario = new javax.swing.JPanel();
        LbNome = new javax.swing.JLabel();
        Tipo = new javax.swing.JPanel();
        LbTipo = new javax.swing.JLabel();
        Hora = new javax.swing.JPanel();
        LbHora = new javax.swing.JLabel();
        Menu = new javax.swing.JPanel();
        MenuUsuarios = new javax.swing.JButton();
        MenuProdutos = new javax.swing.JButton();
        MenuClientes = new javax.swing.JButton();
        MenuAtend = new javax.swing.JButton();
        MenuPizza = new javax.swing.JButton();
        MenuEntreg = new javax.swing.JButton();
        Painel = new javax.swing.JPanel();
        AbasUsuarios = new javax.swing.JTabbedPane();
        CadastroUsuario = new javax.swing.JPanel();
        LabLoginC = new javax.swing.JLabel();
        LabSenhaC = new javax.swing.JLabel();
        LabSenhaRC = new javax.swing.JLabel();
        LabTipoC = new javax.swing.JLabel();
        LabNomeC = new javax.swing.JLabel();
        TxtUserC = new javax.swing.JTextField();
        TxtSenhaC = new javax.swing.JPasswordField();
        TxtSenhaRC = new javax.swing.JPasswordField();
        TxtNomeC = new javax.swing.JTextField();
        CBoxTipoC = new javax.swing.JComboBox();
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
        AbasProdutos = new javax.swing.JTabbedPane();
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
        TxtValorPA = new javax.swing.JTextField();
        LabRSA = new javax.swing.JLabel();
        LabNomePA = new javax.swing.JLabel();
        TxtNomePA = new javax.swing.JTextField();
        LabTipoPA = new javax.swing.JLabel();
        LabTamPA = new javax.swing.JLabel();
        CBoxTipoCPA = new javax.swing.JComboBox();
        CBoxTamCPA = new javax.swing.JComboBox();
        LabDescPA = new javax.swing.JLabel();
        TxtDescPA = new javax.swing.JTextField();
        LabValorPA = new javax.swing.JLabel();
        LabIdPA = new javax.swing.JLabel();
        TxtIdPA = new javax.swing.JTextField();
        ButExcluirP = new javax.swing.JButton();
        ButAlterarP = new javax.swing.JButton();
        AbasClientes = new javax.swing.JTabbedPane();
        CadastroCliente = new javax.swing.JPanel();
        LabCpfCC = new javax.swing.JLabel();
        LabNumCC = new javax.swing.JLabel();
        LabNomeCC = new javax.swing.JLabel();
        LabCpfCC1 = new javax.swing.JLabel();
        LabNumCC1 = new javax.swing.JLabel();
        LabCIdade = new javax.swing.JLabel();
        TxtCpfCC = new javax.swing.JTextField();
        TxtNomeCC = new javax.swing.JTextField();
        TxtRuaCC = new javax.swing.JTextField();
        TxtCidadeCC = new javax.swing.JTextField();
        CadastrarCC = new javax.swing.JButton();
        TxtBairroCC = new javax.swing.JTextField();
        TxtNumCC = new javax.swing.JTextField();
        ListaCliente = new javax.swing.JPanel();
        ScrollPaneTab4 = new javax.swing.JScrollPane();
        TblCliente = new javax.swing.JTable();
        ButAtualizarCL = new javax.swing.JButton();
        AlteraCliente = new javax.swing.JPanel();
        TxtCpfCC4 = new javax.swing.JTextField();
        TxtNumCC2 = new javax.swing.JPasswordField();
        LabNumCC2 = new javax.swing.JLabel();
        LabCpfCC4 = new javax.swing.JLabel();
        TxtCpfCC5 = new javax.swing.JTextField();
        LabNumCC3 = new javax.swing.JLabel();
        LabCpfCC5 = new javax.swing.JLabel();
        LabNomeCC1 = new javax.swing.JLabel();
        LabCpfCC6 = new javax.swing.JLabel();
        TxtCpfCC6 = new javax.swing.JTextField();
        TxtCpfCC7 = new javax.swing.JTextField();
        TxtNumCC3 = new javax.swing.JPasswordField();
        TxtNomeCC1 = new javax.swing.JTextField();
        LabCpfCC7 = new javax.swing.JLabel();
        LabNomeCC2 = new javax.swing.JLabel();
        TxtIdAC = new javax.swing.JTextField();
        ButExcluirAC = new javax.swing.JButton();
        ButAlterarAC = new javax.swing.JButton();
        AbasAtendente = new javax.swing.JTabbedPane();
        CadastrarPedido = new javax.swing.JPanel();
        LabNomeP1 = new javax.swing.JLabel();
        LabTipoP1 = new javax.swing.JLabel();
        LabCpfCC2 = new javax.swing.JLabel();
        LabCpfCC3 = new javax.swing.JLabel();
        LabNumCC4 = new javax.swing.JLabel();
        LabNomeCC3 = new javax.swing.JLabel();
        LabNumCC5 = new javax.swing.JLabel();
        LabCIdade1 = new javax.swing.JLabel();
        BUTBuscarCliente = new javax.swing.JButton();
        BUTCadastrarCliente = new javax.swing.JButton();
        TXTNomeBC = new javax.swing.JTextField();
        TXTNumBC = new javax.swing.JTextField();
        TXTNomeACC = new javax.swing.JTextField();
        TXTRuaACC = new javax.swing.JTextField();
        TXTCidadeACC = new javax.swing.JTextField();
        TXTBairroACC = new javax.swing.JTextField();
        TXTNumACC = new javax.swing.JTextField();
        TXTCpfACC = new javax.swing.JTextField();
        ScrollPaneTab6 = new javax.swing.JScrollPane();
        TblBuscaCliente = new javax.swing.JTable();
        CompCadaPedi = new javax.swing.JPanel();
        LabTipoP2 = new javax.swing.JLabel();
        LabTamP1 = new javax.swing.JLabel();
        LabNome1 = new javax.swing.JLabel();
        BoxTipoAP = new javax.swing.JComboBox();
        BoxTamAP = new javax.swing.JComboBox();
        TxtNomeAP = new javax.swing.JTextField();
        ScrollPaneTab9 = new javax.swing.JScrollPane();
        TblBuscaProduto = new javax.swing.JTable();
        LabNome2 = new javax.swing.JLabel();
        Cliente = new javax.swing.JPanel();
        NomeCliente = new javax.swing.JLabel();
        Numero = new javax.swing.JPanel();
        NumCliente = new javax.swing.JLabel();
        TxtQuantidadeItem = new javax.swing.JTextField();
        LabNome3 = new javax.swing.JLabel();
        ButAtualizarPL2 = new javax.swing.JButton();
        ButAtualizarPL4 = new javax.swing.JButton();
        ButAtualizarPL3 = new javax.swing.JButton();
        ScrollPaneTab11 = new javax.swing.JScrollPane();
        TblItens = new javax.swing.JTable();
        ButAtualizarPL5 = new javax.swing.JButton();
        ButAtualizarPL6 = new javax.swing.JButton();
        AbasPizzaiolo = new javax.swing.JTabbedPane();
        Pedidos = new javax.swing.JPanel();
        LabPP = new javax.swing.JLabel();
        BUTFinalizar = new javax.swing.JButton();
        ScrollPaneTab7 = new javax.swing.JScrollPane();
        TblItensPend = new javax.swing.JTable();
        ScrollPaneTab8 = new javax.swing.JScrollPane();
        TblPedidosFina = new javax.swing.JTable();
        LabNomeP3 = new javax.swing.JLabel();
        ScrollPaneTab10 = new javax.swing.JScrollPane();
        TblPedidosPend = new javax.swing.JTable();
        LabNomeP4 = new javax.swing.JLabel();
        BUTFinalizar1 = new javax.swing.JButton();
        AbasEntregador = new javax.swing.JTabbedPane();
        Entregas = new javax.swing.JPanel();
        LabPP1 = new javax.swing.JLabel();
        BUTFinalizar2 = new javax.swing.JButton();
        ScrollPaneTab13 = new javax.swing.JScrollPane();
        TblPedidosEnt = new javax.swing.JTable();
        LabNomeP5 = new javax.swing.JLabel();
        ScrollPaneTab14 = new javax.swing.JScrollPane();
        TblEntregasPend = new javax.swing.JTable();
        BUTFinalizar3 = new javax.swing.JButton();
        TxtRuaCli = new javax.swing.JTextField();
        TxtBairroCli = new javax.swing.JTextField();
        TxtCidadeCli = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TxtNomeCli = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        ButSair = new javax.swing.JButton();
        ButLogout = new javax.swing.JButton();
        ButLimpar = new javax.swing.JButton();
        ButLimpar1 = new javax.swing.JButton();

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
                .addContainerGap()
                .addComponent(LbNome)
                .addContainerGap(93, Short.MAX_VALUE))
        );
        UsuarioLayout.setVerticalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LbNome, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        Tipo.setBorder(javax.swing.BorderFactory.createTitledBorder("Perfil"));

        LbTipo.setText("Tipo");

        javax.swing.GroupLayout TipoLayout = new javax.swing.GroupLayout(Tipo);
        Tipo.setLayout(TipoLayout);
        TipoLayout.setHorizontalGroup(
            TipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TipoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LbTipo)
                .addContainerGap(100, Short.MAX_VALUE))
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
                .addContainerGap()
                .addComponent(LbHora)
                .addContainerGap(39, Short.MAX_VALUE))
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
                .addGap(6, 6, 6)
                .addComponent(Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        InfosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Tipo, Usuario});

        InfosLayout.setVerticalGroup(
            InfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(InfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Usuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Tipo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Hora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Menu.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Menu"))));

        MenuUsuarios.setText("Usuários");
        MenuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuUsuariosActionPerformed(evt);
            }
        });

        MenuProdutos.setText("Produtos");
        MenuProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuProdutosActionPerformed(evt);
            }
        });

        MenuClientes.setText("Clientes");
        MenuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuClientesActionPerformed(evt);
            }
        });

        MenuAtend.setText("Atendente");
        MenuAtend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAtendActionPerformed(evt);
            }
        });

        MenuPizza.setText("Pizzaiolo");
        MenuPizza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuPizzaActionPerformed(evt);
            }
        });

        MenuEntreg.setText("Entregador");
        MenuEntreg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEntregActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addComponent(MenuUsuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MenuProdutos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MenuClientes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 290, Short.MAX_VALUE)
                .addComponent(MenuAtend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MenuPizza)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MenuEntreg))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(MenuProdutos)
                .addComponent(MenuUsuarios)
                .addComponent(MenuClientes)
                .addComponent(MenuAtend)
                .addComponent(MenuPizza)
                .addComponent(MenuEntreg))
        );

        Painel.setLayout(new java.awt.CardLayout());

        AbasUsuarios.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        AbasUsuarios.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        AbasUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AbasUsuarios.setPreferredSize(new java.awt.Dimension(500, 600));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 480, Short.MAX_VALUE)
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
                        .addGroup(CadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TxtUserC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(TxtSenhaC, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TxtNomeC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroUsuarioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButCadastroC))
        );
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

        AbasUsuarios.addTab("Cadastrar", CadastroUsuario);

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
                .addComponent(ScrollPaneTab, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
                .addContainerGap())
        );
        ListaUsuarioLayout.setVerticalGroup(
            ListaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarL))
        );

        AbasUsuarios.addTab("Listar", ListaUsuario);

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 486, Short.MAX_VALUE)
                        .addGroup(BuscaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CBoxTipoB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtNomeB, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(TxtLoginB)
                            .addComponent(TxtIdB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ScrollPaneTab1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BuscaUsuarioLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
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
                .addComponent(ScrollPaneTab1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );

        AbasUsuarios.addTab("Buscar", BuscaUsuario);

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
                        .addComponent(TxtIdA, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraUsuarioLayout.createSequentialGroup()
                        .addComponent(LabTipoA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CBoxTipoA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraUsuarioLayout.createSequentialGroup()
                        .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabLoginA)
                            .addComponent(LabNomeA)
                            .addComponent(LabSenhaA)
                            .addComponent(LabSenhaNA)
                            .addComponent(LabSenhaNRA))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 450, Short.MAX_VALUE)
                        .addGroup(AlteraUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TxtSenhaN2A, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtSenhaNA, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtNomeA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(TxtLoginA, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtSenhaA))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraUsuarioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButExcluirA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAlterarA))
        );
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

        AbasUsuarios.addTab("Alterar", AlteraUsuario);

        Painel.add(AbasUsuarios, "card2");

        AbasProdutos.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        AbasProdutos.setPreferredSize(new java.awt.Dimension(500, 600));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 469, Short.MAX_VALUE)
                        .addGroup(CadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CBoxTipoCP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBoxTamCP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtNomeP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CadastroProdutoLayout.createSequentialGroup()
                        .addComponent(LabDescP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtDescP, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 442, Short.MAX_VALUE)
                .addComponent(ButCadastroP))
        );

        AbasProdutos.addTab("Cadastrar", CadastroProduto);

        ScrollPaneTab5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Tipo", "Tamanho", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
            .addComponent(ScrollPaneTab5, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
            .addGroup(ListaProdutoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButAtualizarCL1))
        );
        ListaProdutoLayout.setVerticalGroup(
            ListaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab5, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarCL1))
        );

        AbasProdutos.addTab("Listar", ListaProduto);

        LabRSA.setText("R$");

        LabNomePA.setText("Nome");

        LabTipoPA.setText("Tipo");

        LabTamPA.setText("Tamanho");

        CBoxTipoCPA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escolha", "Pizza", "Bebida", "Sobremesa" }));
        CBoxTipoCPA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBoxTipoCPAActionPerformed(evt);
            }
        });

        CBoxTamCPA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escolha" }));

        LabDescPA.setText("Descrição");

        LabValorPA.setText("Valor");

        LabIdPA.setText("ID");

        TxtIdPA.setEditable(false);

        ButExcluirP.setText("Excluir");
        ButExcluirP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButExcluirPActionPerformed(evt);
            }
        });

        ButAlterarP.setText("Alterar");
        ButAlterarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAlterarPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AlteraProdutoLayout = new javax.swing.GroupLayout(AlteraProduto);
        AlteraProduto.setLayout(AlteraProdutoLayout);
        AlteraProdutoLayout.setHorizontalGroup(
            AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlteraProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AlteraProdutoLayout.createSequentialGroup()
                        .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabTipoPA)
                            .addComponent(LabTamPA)
                            .addComponent(LabNomePA))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CBoxTipoCPA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtNomePA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBoxTamCPA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(AlteraProdutoLayout.createSequentialGroup()
                        .addComponent(LabDescPA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 312, Short.MAX_VALUE)
                        .addComponent(TxtDescPA, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraProdutoLayout.createSequentialGroup()
                        .addComponent(LabValorPA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabRSA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtValorPA, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraProdutoLayout.createSequentialGroup()
                        .addComponent(LabIdPA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtIdPA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraProdutoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButExcluirP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAlterarP))
        );
        AlteraProdutoLayout.setVerticalGroup(
            AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlteraProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabIdPA)
                    .addComponent(TxtIdPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomePA)
                    .addComponent(TxtNomePA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTipoPA)
                    .addComponent(CBoxTipoCPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTamPA)
                    .addComponent(CBoxTamCPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabDescPA)
                    .addComponent(TxtDescPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabValorPA)
                    .addComponent(TxtValorPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabRSA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 412, Short.MAX_VALUE)
                .addGroup(AlteraProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButAlterarP)
                    .addComponent(ButExcluirP)))
        );

        AbasProdutos.addTab("Alterar", AlteraProduto);

        Painel.add(AbasProdutos, "card3");

        AbasClientes.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        AbasClientes.setPreferredSize(new java.awt.Dimension(500, 600));

        LabCpfCC.setText("CPF");

        LabNumCC.setText("Número");

        LabNomeCC.setText("Nome");

        LabCpfCC1.setText("Rua");

        LabNumCC1.setText("Bairro");

        LabCIdade.setText("Cidade");

        CadastrarCC.setText("Cadastrar");
        CadastrarCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CadastrarCCActionPerformed(evt);
            }
        });

        TxtBairroCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBairroCCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CadastroClienteLayout = new javax.swing.GroupLayout(CadastroCliente);
        CadastroCliente.setLayout(CadastroClienteLayout);
        CadastroClienteLayout.setHorizontalGroup(
            CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroClienteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(CadastrarCC))
            .addGroup(CadastroClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CadastroClienteLayout.createSequentialGroup()
                        .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabCpfCC)
                            .addComponent(LabNumCC)
                            .addComponent(LabNomeCC))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtNomeCC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtNumCC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                            .addComponent(TxtCpfCC, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastroClienteLayout.createSequentialGroup()
                        .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabCpfCC1)
                            .addComponent(LabNumCC1)
                            .addComponent(LabCIdade))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtRuaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtCidadeCC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtBairroCC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        CadastroClienteLayout.setVerticalGroup(
            CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CadastroClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeCC)
                    .addComponent(TxtNomeCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC)
                    .addComponent(TxtCpfCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNumCC)
                    .addComponent(TxtNumCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC1)
                    .addComponent(TxtRuaCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNumCC1)
                    .addComponent(TxtBairroCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastroClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCIdade)
                    .addComponent(TxtCidadeCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 420, Short.MAX_VALUE)
                .addComponent(CadastrarCC))
        );

        AbasClientes.addTab("Cadastrar", CadastroCliente);

        ScrollPaneTab4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Cpf", "Número", "Rua", "Bairro", "Cidade"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
            .addComponent(ScrollPaneTab4, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
            .addGroup(ListaClienteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButAtualizarCL))
        );
        ListaClienteLayout.setVerticalGroup(
            ListaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListaClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPaneTab4, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarCL))
        );

        AbasClientes.addTab("Listar", ListaCliente);

        LabNumCC2.setText("Bairro");

        LabCpfCC4.setText("CPF");

        LabNumCC3.setText("Número");

        LabCpfCC5.setText("Cidade");

        LabNomeCC1.setText("Nome");

        LabCpfCC6.setText("Complemento");

        LabCpfCC7.setText("Rua");

        LabNomeCC2.setText("ID");

        TxtIdAC.setEditable(false);

        ButExcluirAC.setText("Excluir");
        ButExcluirAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButExcluirACActionPerformed(evt);
            }
        });

        ButAlterarAC.setText("Alterar");
        ButAlterarAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAlterarACActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AlteraClienteLayout = new javax.swing.GroupLayout(AlteraCliente);
        AlteraCliente.setLayout(AlteraClienteLayout);
        AlteraClienteLayout.setHorizontalGroup(
            AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlteraClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AlteraClienteLayout.createSequentialGroup()
                        .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabCpfCC4)
                            .addComponent(LabNumCC3)
                            .addComponent(LabNomeCC1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 476, Short.MAX_VALUE)
                        .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtNomeCC1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(TxtCpfCC6, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(TxtNumCC3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(AlteraClienteLayout.createSequentialGroup()
                        .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabCpfCC7)
                            .addComponent(LabNumCC2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TxtCpfCC4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TxtNumCC2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraClienteLayout.createSequentialGroup()
                        .addComponent(LabCpfCC5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtCpfCC5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraClienteLayout.createSequentialGroup()
                        .addComponent(LabCpfCC6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtCpfCC7, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AlteraClienteLayout.createSequentialGroup()
                        .addComponent(LabNomeCC2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtIdAC, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AlteraClienteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ButExcluirAC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAlterarAC))
        );
        AlteraClienteLayout.setVerticalGroup(
            AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlteraClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeCC2)
                    .addComponent(TxtIdAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeCC1)
                    .addComponent(TxtNomeCC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC4)
                    .addComponent(TxtCpfCC6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNumCC3)
                    .addComponent(TxtNumCC3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC7)
                    .addComponent(TxtCpfCC4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNumCC2)
                    .addComponent(TxtNumCC2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC5)
                    .addComponent(TxtCpfCC5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC6)
                    .addComponent(TxtCpfCC7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 368, Short.MAX_VALUE)
                .addGroup(AlteraClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButAlterarAC)
                    .addComponent(ButExcluirAC)))
        );

        AbasClientes.addTab("Alterar", AlteraCliente);

        Painel.add(AbasClientes, "card4");

        AbasAtendente.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        AbasAtendente.setPreferredSize(new java.awt.Dimension(500, 600));

        LabNomeP1.setText("Nome");

        LabTipoP1.setText("Número");

        LabCpfCC2.setText("CPF");

        LabCpfCC3.setText("Rua");

        LabNumCC4.setText("Número");

        LabNomeCC3.setText("Nome");

        LabNumCC5.setText("Bairro");

        LabCIdade1.setText("Cidade");

        BUTBuscarCliente.setText("Buscar");
        BUTBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUTBuscarClienteActionPerformed(evt);
            }
        });

        BUTCadastrarCliente.setText("Cadastrar Cliente");
        BUTCadastrarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUTCadastrarClienteActionPerformed(evt);
            }
        });

        TXTBairroACC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTBairroACCActionPerformed(evt);
            }
        });

        TXTNumACC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTNumACCActionPerformed(evt);
            }
        });

        ScrollPaneTab6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblBuscaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Cpf", "Número"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        TblBuscaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblBuscaClienteMouseClicked(evt);
            }
        });
        ScrollPaneTab6.setViewportView(TblBuscaCliente);

        javax.swing.GroupLayout CadastrarPedidoLayout = new javax.swing.GroupLayout(CadastrarPedido);
        CadastrarPedido.setLayout(CadastrarPedidoLayout);
        CadastrarPedidoLayout.setHorizontalGroup(
            CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CadastrarPedidoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CadastrarPedidoLayout.createSequentialGroup()
                        .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabTipoP1)
                            .addComponent(LabNomeP1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TXTNomeBC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(TXTNumBC, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(ScrollPaneTab6, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addGroup(CadastrarPedidoLayout.createSequentialGroup()
                        .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabCpfCC2)
                            .addComponent(LabNumCC4)
                            .addComponent(LabNomeCC3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXTNomeACC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXTNumACC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(TXTCpfACC, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastrarPedidoLayout.createSequentialGroup()
                        .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabCpfCC3)
                            .addComponent(LabNumCC5)
                            .addComponent(LabCIdade1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXTRuaACC, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXTCidadeACC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(TXTBairroACC, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CadastrarPedidoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BUTBuscarCliente, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BUTCadastrarCliente, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );

        CadastrarPedidoLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TXTCpfACC, TXTNumACC});

        CadastrarPedidoLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TXTBairroACC, TXTCidadeACC, TXTNomeACC});

        CadastrarPedidoLayout.setVerticalGroup(
            CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CadastrarPedidoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeP1)
                    .addComponent(TXTNomeBC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTipoP1)
                    .addComponent(TXTNumBC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BUTBuscarCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeCC3)
                    .addComponent(TXTNomeACC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC2)
                    .addComponent(TXTCpfACC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNumCC4)
                    .addComponent(TXTNumACC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCpfCC3)
                    .addComponent(TXTRuaACC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNumCC5)
                    .addComponent(TXTBairroACC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CadastrarPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabCIdade1)
                    .addComponent(TXTCidadeACC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BUTCadastrarCliente)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        AbasAtendente.addTab("Buscar Cliente", CadastrarPedido);

        LabTipoP2.setText("Tipo");

        LabTamP1.setText("Tamanho");

        LabNome1.setText("Nome");

        BoxTipoAP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escolha", "Pizza", "Bebida", "Sobremesa" }));
        BoxTipoAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BoxTipoAPActionPerformed(evt);
            }
        });

        BoxTamAP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escolha" }));

        ScrollPaneTab9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblBuscaProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Tipo", "Tamanho", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblBuscaProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblBuscaProdutoMouseClicked(evt);
            }
        });
        ScrollPaneTab9.setViewportView(TblBuscaProduto);

        LabNome2.setText("Itens");

        Cliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        NomeCliente.setText("Nome");

        javax.swing.GroupLayout ClienteLayout = new javax.swing.GroupLayout(Cliente);
        Cliente.setLayout(ClienteLayout);
        ClienteLayout.setHorizontalGroup(
            ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NomeCliente)
                .addContainerGap(199, Short.MAX_VALUE))
        );
        ClienteLayout.setVerticalGroup(
            ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClienteLayout.createSequentialGroup()
                .addComponent(NomeCliente)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        Numero.setBorder(javax.swing.BorderFactory.createTitledBorder("Número"));

        NumCliente.setText("Número");

        javax.swing.GroupLayout NumeroLayout = new javax.swing.GroupLayout(Numero);
        Numero.setLayout(NumeroLayout);
        NumeroLayout.setHorizontalGroup(
            NumeroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NumeroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NumCliente)
                .addContainerGap(199, Short.MAX_VALUE))
        );
        NumeroLayout.setVerticalGroup(
            NumeroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NumeroLayout.createSequentialGroup()
                .addComponent(NumCliente)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        LabNome3.setText("Quantidade");

        ButAtualizarPL2.setText("Cadastrar Pedido");
        ButAtualizarPL2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarPL2ActionPerformed(evt);
            }
        });

        ButAtualizarPL4.setText("Buscar");
        ButAtualizarPL4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarPL4ActionPerformed(evt);
            }
        });

        ButAtualizarPL3.setText("Deletar Item");
        ButAtualizarPL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarPL3ActionPerformed(evt);
            }
        });

        ScrollPaneTab11.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblItens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Tipo", "Tamanho", "Valor Unitário", "Quantidade"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblItens.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblItensMouseClicked(evt);
            }
        });
        ScrollPaneTab11.setViewportView(TblItens);

        ButAtualizarPL5.setText("Adicionar Item");
        ButAtualizarPL5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarPL5ActionPerformed(evt);
            }
        });

        ButAtualizarPL6.setText("Deletar Lista");
        ButAtualizarPL6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButAtualizarPL6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CompCadaPediLayout = new javax.swing.GroupLayout(CompCadaPedi);
        CompCadaPedi.setLayout(CompCadaPediLayout);
        CompCadaPediLayout.setHorizontalGroup(
            CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CompCadaPediLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CompCadaPediLayout.createSequentialGroup()
                        .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabTipoP2)
                            .addComponent(LabTamP1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 555, Short.MAX_VALUE)
                        .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BoxTipoAP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BoxTamAP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CompCadaPediLayout.createSequentialGroup()
                        .addComponent(LabNome1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtNomeAP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ScrollPaneTab9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CompCadaPediLayout.createSequentialGroup()
                        .addComponent(ButAtualizarPL3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ButAtualizarPL6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButAtualizarPL2))
                    .addComponent(ScrollPaneTab11)
                    .addGroup(CompCadaPediLayout.createSequentialGroup()
                        .addComponent(LabNome2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabNome3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtQuantidadeItem, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButAtualizarPL5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CompCadaPediLayout.createSequentialGroup()
                        .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Numero, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButAtualizarPL4, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );

        CompCadaPediLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BoxTamAP, BoxTipoAP});

        CompCadaPediLayout.setVerticalGroup(
            CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CompCadaPediLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Numero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTipoP2)
                    .addComponent(BoxTipoAP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabTamP1)
                    .addComponent(BoxTamAP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNome1)
                    .addComponent(TxtNomeAP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButAtualizarPL4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNome2)
                    .addComponent(ButAtualizarPL5)
                    .addComponent(LabNome3)
                    .addComponent(TxtQuantidadeItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab11, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CompCadaPediLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButAtualizarPL2)
                    .addComponent(ButAtualizarPL3)
                    .addComponent(ButAtualizarPL6))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        AbasAtendente.addTab("Cadastrar Pedido", CompCadaPedi);

        Painel.add(AbasAtendente, "card5");

        AbasPizzaiolo.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        AbasPizzaiolo.setPreferredSize(new java.awt.Dimension(500, 600));

        LabPP.setText("Pedidos Finalizados");

        BUTFinalizar.setText("Finalizar Pedido");
        BUTFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUTFinalizarActionPerformed(evt);
            }
        });

        ScrollPaneTab7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblItensPend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Produto", "Quantidade"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblItensPend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblItensPendMouseClicked(evt);
            }
        });
        ScrollPaneTab7.setViewportView(TblItensPend);

        ScrollPaneTab8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblPedidosFina.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Hora", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblPedidosFina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblPedidosFinaMouseClicked(evt);
            }
        });
        ScrollPaneTab8.setViewportView(TblPedidosFina);

        LabNomeP3.setText("Pedidos Pendentes");

        ScrollPaneTab10.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblPedidosPend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Hora", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblPedidosPend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblPedidosPendMouseClicked(evt);
            }
        });
        ScrollPaneTab10.setViewportView(TblPedidosPend);

        LabNomeP4.setText("Itens do Pedido");

        BUTFinalizar1.setText("Atualizar");
        BUTFinalizar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUTFinalizar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PedidosLayout = new javax.swing.GroupLayout(Pedidos);
        Pedidos.setLayout(PedidosLayout);
        PedidosLayout.setHorizontalGroup(
            PedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollPaneTab8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                    .addComponent(ScrollPaneTab7)
                    .addComponent(ScrollPaneTab10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                    .addGroup(PedidosLayout.createSequentialGroup()
                        .addComponent(LabPP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BUTFinalizar))
                    .addGroup(PedidosLayout.createSequentialGroup()
                        .addComponent(LabNomeP4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PedidosLayout.createSequentialGroup()
                        .addComponent(LabNomeP3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BUTFinalizar1)))
                .addContainerGap())
        );
        PedidosLayout.setVerticalGroup(
            PedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeP3)
                    .addComponent(BUTFinalizar1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab10, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabNomeP4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab7, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BUTFinalizar)
                    .addComponent(LabPP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        AbasPizzaiolo.addTab("Pedidos", Pedidos);

        Painel.add(AbasPizzaiolo, "card5");

        AbasEntregador.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        AbasEntregador.setPreferredSize(new java.awt.Dimension(500, 600));

        LabPP1.setText("Pedidos Entregues");

        BUTFinalizar2.setText("Finalizar Entrega");
        BUTFinalizar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUTFinalizar2ActionPerformed(evt);
            }
        });

        ScrollPaneTab13.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblPedidosEnt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Hora", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblPedidosEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblPedidosEntMouseClicked(evt);
            }
        });
        ScrollPaneTab13.setViewportView(TblPedidosEnt);

        LabNomeP5.setText("Entregas Pendentes");

        ScrollPaneTab14.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TblEntregasPend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Hora", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblEntregasPend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblEntregasPendMouseClicked(evt);
            }
        });
        ScrollPaneTab14.setViewportView(TblEntregasPend);

        BUTFinalizar3.setText("Atualizar");
        BUTFinalizar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUTFinalizar3ActionPerformed(evt);
            }
        });

        TxtRuaCli.setEditable(false);

        TxtBairroCli.setEditable(false);

        TxtCidadeCli.setEditable(false);

        jLabel1.setText("Rua");

        jLabel2.setText("Bairro");

        jLabel3.setText("Cidade");

        TxtNomeCli.setEditable(false);

        jLabel4.setText("Cliente");

        javax.swing.GroupLayout EntregasLayout = new javax.swing.GroupLayout(Entregas);
        Entregas.setLayout(EntregasLayout);
        EntregasLayout.setHorizontalGroup(
            EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollPaneTab13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                    .addComponent(ScrollPaneTab14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                    .addGroup(EntregasLayout.createSequentialGroup()
                        .addComponent(LabPP1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BUTFinalizar2))
                    .addGroup(EntregasLayout.createSequentialGroup()
                        .addComponent(LabNomeP5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BUTFinalizar3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EntregasLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtCidadeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EntregasLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtRuaCli, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EntregasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtBairroCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EntregasLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        EntregasLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TxtBairroCli, TxtCidadeCli, TxtRuaCli});

        EntregasLayout.setVerticalGroup(
            EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabNomeP5)
                    .addComponent(BUTFinalizar3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab14, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtRuaCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtBairroCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtCidadeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(EntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BUTFinalizar2)
                    .addComponent(LabPP1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPaneTab13, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        AbasEntregador.addTab("Pedidos", Entregas);

        Painel.add(AbasEntregador, "card5");

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

        ButLimpar1.setText("Configurações Conta");
        ButLimpar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButLimpar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(Infos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(ButLimpar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButLimpar)
                .addGap(6, 6, 6)
                .addComponent(ButLogout)
                .addGap(6, 6, 6)
                .addComponent(ButSair))
            .addComponent(Painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Infos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Painel, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButLimpar)
                        .addComponent(ButLimpar1))
                    .addComponent(ButLogout)
                    .addComponent(ButSair)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_ButSairActionPerformed

    private void ButLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButLogoutActionPerformed
        logout();
    }//GEN-LAST:event_ButLogoutActionPerformed

    private void ButLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButLimparActionPerformed
        limparTodosCampos(Painel);
    }//GEN-LAST:event_ButLimparActionPerformed

    private void MenuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuUsuariosActionPerformed
        Painel.removeAll();
        Painel.add(AbasUsuarios);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_MenuUsuariosActionPerformed

    private void MenuProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuProdutosActionPerformed
        Painel.removeAll();
        Painel.add(AbasProdutos);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_MenuProdutosActionPerformed

    private void MenuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuClientesActionPerformed
        Painel.removeAll();
        Painel.add(AbasClientes);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_MenuClientesActionPerformed

    private void ButLimpar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButLimpar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButLimpar1ActionPerformed

    private void MenuAtendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAtendActionPerformed
        Painel.removeAll();
        Painel.add(AbasAtendente);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_MenuAtendActionPerformed

    private void MenuPizzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuPizzaActionPerformed
        Painel.removeAll();
        Painel.add(AbasPizzaiolo);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_MenuPizzaActionPerformed

    private void MenuEntregActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEntregActionPerformed
        Painel.removeAll();
        Painel.add(AbasEntregador);
        Painel.repaint();
        Painel.validate();
    }//GEN-LAST:event_MenuEntregActionPerformed

    private void ButAtualizarPL2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarPL2ActionPerformed
        PedidoBO pedBo = new PedidoBO();
        try {
            if (clientePedindo.getId() != null) {
                if(itensPedidos.size()>0){
                    pedBo.cadastrar(null, "Pendente", clientePedindo, itensPedidos);
                itensPedidos = new ArrayList<>();
                clientePedindo = new ClienteDTO();
                atualizarTabelaItem(itensPedidos);
                NomeCliente.setText("");
                NumCliente.setText("");
                } else {
                    MensagensUtil.addMsg(this, "Selecione os produtos.");
                }
            } else {
                MensagensUtil.addMsg(this, "Selecione um cliente.");
            }
        } catch (NegocioException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ButAtualizarPL2ActionPerformed

    private void TblBuscaProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblBuscaProdutoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblBuscaProdutoMouseClicked

    private void BoxTipoAPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BoxTipoAPActionPerformed
        BoxTamAP.removeAllItems();
        if (BoxTipoAP.getSelectedIndex() == 0) {
            BoxTamAP.addItem("Escolha Tipo");
        }
        if (BoxTipoAP.getSelectedIndex() == 1) {
            BoxTamAP.addItem("P");
            BoxTamAP.addItem("M");
            BoxTamAP.addItem("G");
            BoxTamAP.addItem("F");
        }
        if (BoxTipoAP.getSelectedIndex() == 2) {
            BoxTamAP.addItem("Lata");
            BoxTamAP.addItem("1L");
            BoxTamAP.addItem("1,5L");
            BoxTamAP.addItem("2L");
            BoxTamAP.addItem("Copo");
            BoxTamAP.addItem("Jarra");
        }
        if (BoxTipoAP.getSelectedIndex() == 3) {
            BoxTamAP.addItem("Unico");
        }
    }//GEN-LAST:event_BoxTipoAPActionPerformed

    private void BUTCadastrarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUTCadastrarClienteActionPerformed
        String nome = TXTNomeACC.getText();
        String cpf = TXTCpfACC.getText();
        String num = TXTNumACC.getText();
        String rua = TXTRuaACC.getText();
        String bairro = TXTBairroACC.getText();
        String cidade = TXTCidadeACC.getText();
        ClienteBO clienteBo = new ClienteBO();
        EnderecoDTO end = new EnderecoDTO(rua, bairro, cidade);
        try {
            if (clienteBo.cadastrar(nome, cpf, num, end)) {
                MensagensUtil.addMsg(MainFrame.this, "Cadastro efetuado com sucesso!");
                atualizarTabelaBuscarCliente(null);
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha no cadastro.");
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
            atualizarTabelaBuscarCliente(null);
        }
    }//GEN-LAST:event_BUTCadastrarClienteActionPerformed

    private void TXTBairroACCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTBairroACCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTBairroACCActionPerformed

    private void TXTNumACCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTNumACCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTNumACCActionPerformed

    private void BUTBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUTBuscarClienteActionPerformed
        List<ClienteDTO> lista;
        String nome = TXTNomeBC.getText();
        String num = TXTNumBC.getText();
        ClienteBO buscarBO = new ClienteBO();
        lista = buscarBO.buscar(nome, num);
        atualizarTabelaBuscarCliente(lista);
    }//GEN-LAST:event_BUTBuscarClienteActionPerformed

    private void TblBuscaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblBuscaClienteMouseClicked
        Integer linha = TblBuscaCliente.getSelectedRow();
        AbasAtendente.setSelectedComponent(CompCadaPedi);
        clientePedindo = new ClienteBO().buscarPorID((Integer) TblBuscaCliente.getValueAt(linha, 0));
        NomeCliente.setText(clientePedindo.getNome());
        NumCliente.setText(clientePedindo.getNumero());
    }//GEN-LAST:event_TblBuscaClienteMouseClicked

    private void ButAtualizarPL4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarPL4ActionPerformed
        List<ProdutoDTO> lista;
        String tipo = null;
        String tam = null;
        if (BoxTipoAP.getSelectedIndex() > 0) {
            tipo = BoxTipoAP.getSelectedItem() + "";
            tam = BoxTamAP.getSelectedItem() + "";
        }
        String nome = TxtNomeAP.getText();
        ProdutoBO buscarBO = new ProdutoBO();
        try {
            lista = buscarBO.busca(null, nome, tam, tipo);
            atualizarTabelaBuscaProduto(lista);
        } catch (NegocioException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ButAtualizarPL4ActionPerformed

    private void ButAtualizarPL3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarPL3ActionPerformed
        int linha = TblItens.getSelectedRow();
        if (linha != -1) {
            itensPedidos.remove(linha);
            atualizarTabelaItem(itensPedidos);
        } else {
            MensagensUtil.addMsg(this, "Selecione um item na lista.");
        }

    }//GEN-LAST:event_ButAtualizarPL3ActionPerformed

    private void TblItensMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblItensMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblItensMouseClicked

    private void ButAtualizarPL5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarPL5ActionPerformed
        Integer linha = TblBuscaProduto.getSelectedRow();
        ItemPedido item = new ItemPedido(new ProdutoBO().buscarPorID((Integer) TblBuscaProduto.getValueAt(linha, 0)), null, Integer.parseInt(TxtQuantidadeItem.getText()));
        itensPedidos.add(item);
        atualizarTabelaItem(itensPedidos);
    }//GEN-LAST:event_ButAtualizarPL5ActionPerformed

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
                AbasUsuarios.setSelectedComponent(ListaUsuario);
                atualizarTabelaUser();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha no cadastro.");
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButCadastroCActionPerformed

    private void TblUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblUserMouseClicked
        Integer linha = TblUser.getSelectedRow();
        AbasUsuarios.setSelectedComponent(AlteraUsuario);
        atualizarTxtUsuario((Integer) TblUser.getValueAt(linha, 0));
    }//GEN-LAST:event_TblUserMouseClicked

    private void ButAtualizarLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarLActionPerformed
        atualizarTabelaUser();
    }//GEN-LAST:event_ButAtualizarLActionPerformed

    private void TxtIdBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtIdBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_TxtIdBActionPerformed

    private void TxtLoginBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtLoginBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_TxtLoginBActionPerformed

    private void CBoxTipoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBoxTipoBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_CBoxTipoBActionPerformed

    private void TxtNomeBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNomeBActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_TxtNomeBActionPerformed

    private void TblUserFiltroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblUserFiltroMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblUserFiltroMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        listarFiltrado();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ButExcluirAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButExcluirAActionPerformed
        String id = TxtIdA.getText();
        String senha = String.copyValueOf(TxtSenhaA.getPassword());
        UserBO excluirBo = new UserBO();
        try {
            if (excluirBo.excluir(id, senha)) {
                MensagensUtil.addMsg(MainFrame.this, "Excluido com sucesso!");
                AbasUsuarios.setSelectedComponent(ListaUsuario);
                atualizarTabelaUser();
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
                AbasUsuarios.setSelectedComponent(ListaUsuario);
                atualizarTabelaUser();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha na alteração.");
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButAlterarAActionPerformed

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
                AbasProdutos.setSelectedComponent(ListaProduto);
                atualizarTabelaProduto();
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

    private void TblProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblProdutoMouseClicked
        Integer linha = TblProduto.getSelectedRow();
        AbasProdutos.setSelectedComponent(AlteraProduto);
        atualizarTxtProduto((Integer) TblProduto.getValueAt(linha, 0));
    }//GEN-LAST:event_TblProdutoMouseClicked

    private void ButAtualizarCL1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarCL1ActionPerformed
        atualizarTabelaProduto();
    }//GEN-LAST:event_ButAtualizarCL1ActionPerformed

    private void CBoxTipoCPAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBoxTipoCPAActionPerformed
        CBoxTamCPA.removeAllItems();
        if (CBoxTipoCPA.getSelectedIndex() == 0) {
            CBoxTamCPA.addItem("Escolha");
        }
        if (CBoxTipoCPA.getSelectedIndex() == 1) {
            CBoxTamCPA.addItem("P");
            CBoxTamCPA.addItem("M");
            CBoxTamCPA.addItem("G");
            CBoxTamCPA.addItem("F");
        }
        if (CBoxTipoCPA.getSelectedIndex() == 2) {
            CBoxTamCPA.addItem("Lata");
            CBoxTamCPA.addItem("1L");
            CBoxTamCPA.addItem("1,5L");
            CBoxTamCPA.addItem("2L");
            CBoxTamCPA.addItem("Copo");
            CBoxTamCPA.addItem("Jarra");
        }
        if (CBoxTipoCPA.getSelectedIndex() == 3) {
            CBoxTamCPA.addItem("Unico");
        }
    }//GEN-LAST:event_CBoxTipoCPAActionPerformed

    private void ButExcluirPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButExcluirPActionPerformed
        String id = TxtIdPA.getText();
        ProdutoBO excluirBo = new ProdutoBO();
        try {
            if (excluirBo.excluir(id)) {
                MensagensUtil.addMsg(MainFrame.this, "Excluido com sucesso!");
                AbasProdutos.setSelectedComponent(ListaProduto);
                atualizarTabelaUser();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha ao excluir.");
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButExcluirPActionPerformed

    private void ButAlterarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAlterarPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButAlterarPActionPerformed

    private void CadastrarCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CadastrarCCActionPerformed
        String nome = TxtNomeCC.getText();
        String cpf = TxtCpfCC.getText();
        String num = TxtNumCC.getText();
        String rua = TxtRuaCC.getText();
        String bairro = TxtBairroCC.getText();
        String cidade = TxtCidadeCC.getText();
        ClienteBO clienteBo = new ClienteBO();
        EnderecoDTO end = new EnderecoDTO(rua, bairro, cidade);
        try {
            if (clienteBo.cadastrar(nome, cpf, num, end)) {
                MensagensUtil.addMsg(MainFrame.this, "Cadastro efetuado com sucesso!");
                AbasClientes.setSelectedComponent(ListaCliente);
                atualizarTabelaCliente();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha no cadastro.");
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_CadastrarCCActionPerformed

    private void TxtBairroCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBairroCCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBairroCCActionPerformed

    private void TblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblClienteMouseClicked
        Integer linha = TblCliente.getSelectedRow();
        AbasClientes.setSelectedComponent(AlteraCliente);
        atualizarTxtCliente((Integer) TblCliente.getValueAt(linha, 0));
    }//GEN-LAST:event_TblClienteMouseClicked

    private void ButAtualizarCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarCLActionPerformed
        atualizarTabelaCliente();
    }//GEN-LAST:event_ButAtualizarCLActionPerformed

    private void ButExcluirACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButExcluirACActionPerformed
        String id = TxtIdAC.getText();
        ClienteBO excluirBo = new ClienteBO();
        try {
            if (excluirBo.excluir(id)) {
                MensagensUtil.addMsg(MainFrame.this, "Excluido com sucesso!");
                AbasClientes.setSelectedComponent(ListaCliente);
                atualizarTabelaCliente();
            } else {
                MensagensUtil.addMsg(MainFrame.this, "Falha ao excluir.");
            }
        } catch (NegocioException ex) {
            MensagensUtil.addMsg(MainFrame.this, ex.getMessage());
        } finally {
            limparTodosCampos(rootPane);
        }
    }//GEN-LAST:event_ButExcluirACActionPerformed

    private void ButAlterarACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAlterarACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButAlterarACActionPerformed

    private void ButAtualizarPL6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAtualizarPL6ActionPerformed
        itensPedidos = new ArrayList<>();
        atualizarTabelaItem(itensPedidos);
    }//GEN-LAST:event_ButAtualizarPL6ActionPerformed

    private void BUTFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUTFinalizarActionPerformed
        Integer linha = TblPedidosPend.getSelectedRow();
        PedidoBO ped = new PedidoBO();
        ped.finalizar((Integer) TblPedidosPend.getValueAt(linha, 0));
        atualizarTabelaPedFin();
        atualizarTabelaPedPend();
    }//GEN-LAST:event_BUTFinalizarActionPerformed

    private void TblItensPendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblItensPendMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblItensPendMouseClicked

    private void TblPedidosFinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblPedidosFinaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblPedidosFinaMouseClicked

    private void TblPedidosPendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblPedidosPendMouseClicked
        Integer linha = TblPedidosPend.getSelectedRow();
        List<ItemPedido> lista;
        ItemDAO itemDao = new ItemDAO();
        try {
            lista = itemDao.buscarPorIdPedido((Integer) TblPedidosPend.getValueAt(linha, 0));
            atualizarTabelaItensPedidos(lista);
        } catch (PersistenciaException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TblPedidosPendMouseClicked

    private void BUTFinalizar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUTFinalizar1ActionPerformed
        atualizarTabelaPedPend();
    }//GEN-LAST:event_BUTFinalizar1ActionPerformed

    private void BUTFinalizar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUTFinalizar2ActionPerformed
        Integer linha = TblEntregasPend.getSelectedRow();
        PedidoBO ped = new PedidoBO();
        ped.entregar((Integer) TblEntregasPend.getValueAt(linha, 0));
        atualizarTabelaEntPend();
        atualizarTabelaEntFin();
    }//GEN-LAST:event_BUTFinalizar2ActionPerformed

    private void TblPedidosEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblPedidosEntMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblPedidosEntMouseClicked

    private void TblEntregasPendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblEntregasPendMouseClicked
        Integer linha = TblEntregasPend.getSelectedRow();
        PedidoBO ped = new PedidoBO();
        PedidoDTO pedido = ped.buscarPorID((Integer) TblEntregasPend.getValueAt(linha, 0));
        TxtNomeCli.setText(pedido.getCliente().getNome());
        TxtRuaCli.setText(pedido.getCliente().getEndereco().getRua());
        TxtBairroCli.setText(pedido.getCliente().getEndereco().getBairro());
        TxtCidadeCli.setText(pedido.getCliente().getEndereco().getCidade());
    }//GEN-LAST:event_TblEntregasPendMouseClicked

    private void BUTFinalizar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BUTFinalizar3ActionPerformed
        atualizarTabelaEntPend();
    }//GEN-LAST:event_BUTFinalizar3ActionPerformed

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
    private javax.swing.JTabbedPane AbasAtendente;
    private javax.swing.JTabbedPane AbasClientes;
    private javax.swing.JTabbedPane AbasEntregador;
    private javax.swing.JTabbedPane AbasPizzaiolo;
    private javax.swing.JTabbedPane AbasProdutos;
    private javax.swing.JTabbedPane AbasUsuarios;
    private javax.swing.JPanel AlteraCliente;
    private javax.swing.JPanel AlteraProduto;
    private javax.swing.JPanel AlteraUsuario;
    private javax.swing.JButton BUTBuscarCliente;
    private javax.swing.JButton BUTCadastrarCliente;
    private javax.swing.JButton BUTFinalizar;
    private javax.swing.JButton BUTFinalizar1;
    private javax.swing.JButton BUTFinalizar2;
    private javax.swing.JButton BUTFinalizar3;
    private javax.swing.JComboBox BoxTamAP;
    private javax.swing.JComboBox BoxTipoAP;
    private javax.swing.JPanel BuscaUsuario;
    private javax.swing.JButton ButAlterarA;
    private javax.swing.JButton ButAlterarAC;
    private javax.swing.JButton ButAlterarP;
    private javax.swing.JButton ButAtualizarCL;
    private javax.swing.JButton ButAtualizarCL1;
    private javax.swing.JButton ButAtualizarL;
    private javax.swing.JButton ButAtualizarPL2;
    private javax.swing.JButton ButAtualizarPL3;
    private javax.swing.JButton ButAtualizarPL4;
    private javax.swing.JButton ButAtualizarPL5;
    private javax.swing.JButton ButAtualizarPL6;
    private javax.swing.JButton ButCadastroC;
    private javax.swing.JButton ButCadastroP;
    private javax.swing.JButton ButExcluirA;
    private javax.swing.JButton ButExcluirAC;
    private javax.swing.JButton ButExcluirP;
    private javax.swing.JButton ButLimpar;
    private javax.swing.JButton ButLimpar1;
    private javax.swing.JButton ButLogout;
    private javax.swing.JButton ButSair;
    private javax.swing.JComboBox CBoxTamCP;
    private javax.swing.JComboBox CBoxTamCPA;
    private javax.swing.JComboBox CBoxTipoA;
    private javax.swing.JComboBox CBoxTipoB;
    private javax.swing.JComboBox CBoxTipoC;
    private javax.swing.JComboBox CBoxTipoCP;
    private javax.swing.JComboBox CBoxTipoCPA;
    private javax.swing.JButton CadastrarCC;
    private javax.swing.JPanel CadastrarPedido;
    private javax.swing.JPanel CadastroCliente;
    private javax.swing.JPanel CadastroProduto;
    private javax.swing.JPanel CadastroUsuario;
    private javax.swing.JPanel Cliente;
    private javax.swing.JPanel CompCadaPedi;
    private javax.swing.JPanel Entregas;
    private javax.swing.JPanel Hora;
    private javax.swing.JPanel Infos;
    private javax.swing.JLabel LabCIdade;
    private javax.swing.JLabel LabCIdade1;
    private javax.swing.JLabel LabCpfCC;
    private javax.swing.JLabel LabCpfCC1;
    private javax.swing.JLabel LabCpfCC2;
    private javax.swing.JLabel LabCpfCC3;
    private javax.swing.JLabel LabCpfCC4;
    private javax.swing.JLabel LabCpfCC5;
    private javax.swing.JLabel LabCpfCC6;
    private javax.swing.JLabel LabCpfCC7;
    private javax.swing.JLabel LabDescP;
    private javax.swing.JLabel LabDescPA;
    private javax.swing.JLabel LabIdA;
    private javax.swing.JLabel LabIdB;
    private javax.swing.JLabel LabIdPA;
    private javax.swing.JLabel LabLoginA;
    private javax.swing.JLabel LabLoginB;
    private javax.swing.JLabel LabLoginC;
    private javax.swing.JLabel LabNome1;
    private javax.swing.JLabel LabNome2;
    private javax.swing.JLabel LabNome3;
    private javax.swing.JLabel LabNomeA;
    private javax.swing.JLabel LabNomeB;
    private javax.swing.JLabel LabNomeC;
    private javax.swing.JLabel LabNomeCC;
    private javax.swing.JLabel LabNomeCC1;
    private javax.swing.JLabel LabNomeCC2;
    private javax.swing.JLabel LabNomeCC3;
    private javax.swing.JLabel LabNomeP;
    private javax.swing.JLabel LabNomeP1;
    private javax.swing.JLabel LabNomeP3;
    private javax.swing.JLabel LabNomeP4;
    private javax.swing.JLabel LabNomeP5;
    private javax.swing.JLabel LabNomePA;
    private javax.swing.JLabel LabNumCC;
    private javax.swing.JLabel LabNumCC1;
    private javax.swing.JLabel LabNumCC2;
    private javax.swing.JLabel LabNumCC3;
    private javax.swing.JLabel LabNumCC4;
    private javax.swing.JLabel LabNumCC5;
    private javax.swing.JLabel LabPP;
    private javax.swing.JLabel LabPP1;
    private javax.swing.JLabel LabRS;
    private javax.swing.JLabel LabRSA;
    private javax.swing.JLabel LabSenhaA;
    private javax.swing.JLabel LabSenhaC;
    private javax.swing.JLabel LabSenhaNA;
    private javax.swing.JLabel LabSenhaNRA;
    private javax.swing.JLabel LabSenhaRC;
    private javax.swing.JLabel LabTamP;
    private javax.swing.JLabel LabTamP1;
    private javax.swing.JLabel LabTamPA;
    private javax.swing.JLabel LabTipoA;
    private javax.swing.JLabel LabTipoB;
    private javax.swing.JLabel LabTipoC;
    private javax.swing.JLabel LabTipoP;
    private javax.swing.JLabel LabTipoP1;
    private javax.swing.JLabel LabTipoP2;
    private javax.swing.JLabel LabTipoPA;
    private javax.swing.JLabel LabValorP;
    private javax.swing.JLabel LabValorPA;
    private javax.swing.JLabel LbHora;
    private javax.swing.JLabel LbNome;
    private javax.swing.JLabel LbTipo;
    private javax.swing.JPanel ListaCliente;
    private javax.swing.JPanel ListaProduto;
    private javax.swing.JPanel ListaUsuario;
    private javax.swing.JPanel Menu;
    private javax.swing.JButton MenuAtend;
    private javax.swing.JButton MenuClientes;
    private javax.swing.JButton MenuEntreg;
    private javax.swing.JButton MenuPizza;
    private javax.swing.JButton MenuProdutos;
    private javax.swing.JButton MenuUsuarios;
    private javax.swing.JLabel NomeCliente;
    private javax.swing.JLabel NumCliente;
    private javax.swing.JPanel Numero;
    private javax.swing.JPanel Painel;
    private javax.swing.JPanel Pedidos;
    private javax.swing.JScrollPane ScrollPaneTab;
    private javax.swing.JScrollPane ScrollPaneTab1;
    private javax.swing.JScrollPane ScrollPaneTab10;
    private javax.swing.JScrollPane ScrollPaneTab11;
    private javax.swing.JScrollPane ScrollPaneTab13;
    private javax.swing.JScrollPane ScrollPaneTab14;
    private javax.swing.JScrollPane ScrollPaneTab4;
    private javax.swing.JScrollPane ScrollPaneTab5;
    private javax.swing.JScrollPane ScrollPaneTab6;
    private javax.swing.JScrollPane ScrollPaneTab7;
    private javax.swing.JScrollPane ScrollPaneTab8;
    private javax.swing.JScrollPane ScrollPaneTab9;
    private javax.swing.JTextField TXTBairroACC;
    private javax.swing.JTextField TXTCidadeACC;
    private javax.swing.JTextField TXTCpfACC;
    private javax.swing.JTextField TXTNomeACC;
    private javax.swing.JTextField TXTNomeBC;
    private javax.swing.JTextField TXTNumACC;
    private javax.swing.JTextField TXTNumBC;
    private javax.swing.JTextField TXTRuaACC;
    private javax.swing.JTable TblBuscaCliente;
    private javax.swing.JTable TblBuscaProduto;
    private javax.swing.JTable TblCliente;
    private javax.swing.JTable TblEntregasPend;
    private javax.swing.JTable TblItens;
    private javax.swing.JTable TblItensPend;
    private javax.swing.JTable TblPedidosEnt;
    private javax.swing.JTable TblPedidosFina;
    private javax.swing.JTable TblPedidosPend;
    private javax.swing.JTable TblProduto;
    private javax.swing.JTable TblUser;
    private javax.swing.JTable TblUserFiltro;
    private javax.swing.JPanel Tipo;
    private javax.swing.JTextField TxtBairroCC;
    private javax.swing.JTextField TxtBairroCli;
    private javax.swing.JTextField TxtCidadeCC;
    private javax.swing.JTextField TxtCidadeCli;
    private javax.swing.JTextField TxtCpfCC;
    private javax.swing.JTextField TxtCpfCC4;
    private javax.swing.JTextField TxtCpfCC5;
    private javax.swing.JTextField TxtCpfCC6;
    private javax.swing.JTextField TxtCpfCC7;
    private javax.swing.JTextField TxtDescP;
    private javax.swing.JTextField TxtDescPA;
    private javax.swing.JTextField TxtIdA;
    private javax.swing.JTextField TxtIdAC;
    private javax.swing.JTextField TxtIdB;
    private javax.swing.JTextField TxtIdPA;
    private javax.swing.JTextField TxtLoginA;
    private javax.swing.JTextField TxtLoginB;
    private javax.swing.JTextField TxtNomeA;
    private javax.swing.JTextField TxtNomeAP;
    private javax.swing.JTextField TxtNomeB;
    private javax.swing.JTextField TxtNomeC;
    private javax.swing.JTextField TxtNomeCC;
    private javax.swing.JTextField TxtNomeCC1;
    private javax.swing.JTextField TxtNomeCli;
    private javax.swing.JTextField TxtNomeP;
    private javax.swing.JTextField TxtNomePA;
    private javax.swing.JTextField TxtNumCC;
    private javax.swing.JPasswordField TxtNumCC2;
    private javax.swing.JPasswordField TxtNumCC3;
    private javax.swing.JTextField TxtQuantidadeItem;
    private javax.swing.JTextField TxtRuaCC;
    private javax.swing.JTextField TxtRuaCli;
    private javax.swing.JPasswordField TxtSenhaA;
    private javax.swing.JPasswordField TxtSenhaC;
    private javax.swing.JPasswordField TxtSenhaN2A;
    private javax.swing.JPasswordField TxtSenhaNA;
    private javax.swing.JPasswordField TxtSenhaRC;
    private javax.swing.JTextField TxtUserC;
    private javax.swing.JTextField TxtValorP;
    private javax.swing.JTextField TxtValorPA;
    private javax.swing.JPanel Usuario;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
