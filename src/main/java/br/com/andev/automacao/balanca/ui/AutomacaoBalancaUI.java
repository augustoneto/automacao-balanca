package br.com.andev.automacao.balanca.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.andev.automacao.balanca.dao.PedidoPesoDao;
import br.com.andev.automacao.balanca.dao.PedidoPesoDaoJpa;
import br.com.andev.automacao.balanca.dao.PersistenceManager;
import br.com.andev.automacao.balanca.model.PedidoPeso;

public class AutomacaoBalancaUI {

	private JFrame janela;

	private JPanel painelPrincipal;
	
	private JLabel jlNumPedido, jlNumNotaFiscal, jlOperador, jlPeso;
	private JTextField jtfNumPedido, jtfNumNotaFiscal, jtfOperador, jtfPeso;
	
	private JButton botaoGravar, botaoSair;
	
	private PedidoPesoDao pedidoPesoDao;
	

	public static void main(String[] args) {
		new AutomacaoBalancaUI().montaTela();
	}

	public void montaTela() {
		preparaJanela();
		preparaPainelPrincipal();
		preparaCampos();
		preparaBotaoGravar();
		mostraJanela();
	}

	private void preparaJanela() {
		janela = new JFrame("Automação Balança");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void mostraJanela() {
		janela.pack();
		janela.setSize(540,540);
		janela.setVisible(true);
	}

	private void preparaPainelPrincipal() {
		painelPrincipal = new JPanel();
		painelPrincipal.setLayout(null);
		janela.add(painelPrincipal);
	}
	
	private void preparaCampos() {
		jlNumPedido = new JLabel("Num Pedido");
		adiciona(jlNumPedido, 10, 1, 130, 25);
		
		jtfNumPedido = new JTextField(20);
		adiciona(jtfNumPedido, 130, 1, 300, 25);
		
		jlNumNotaFiscal = new JLabel("Num Nota Fiscal");
		adiciona(jlNumNotaFiscal, 10, 31, 130, 25);

		jtfNumNotaFiscal = new JTextField(20);
		adiciona(jtfNumNotaFiscal, 130, 31, 300, 25);
		
		jlOperador = new JLabel("Operador");
		adiciona(jlOperador, 10, 61, 130, 25);

		jtfOperador = new JTextField(20);
		adiciona(jtfOperador, 130, 61, 300, 25);
		
		jlPeso = new JLabel("Peso");
		adiciona(jlPeso, 10, 91, 130, 25);

		jtfPeso = new JTextField(20);
		adiciona(jtfPeso, 130, 91, 300, 25);
	}

	private void preparaBotaoGravar() {
		botaoGravar = new JButton("Gravar");
		botaoGravar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PedidoPeso pedidoPeso = new PedidoPeso();
				pedidoPeso.setNumPedido(Long.parseLong(jtfNumPedido.getText()));
				pedidoPeso.setNumNotaFiscal(Long.parseLong(jtfNumNotaFiscal.getText()));
				pedidoPeso.setPeso(Double.parseDouble(jtfPeso.getText()));
				pedidoPeso.setUnidade("KG");
				pedidoPeso.setOperador(jtfOperador.getText());
				pedidoPeso.setDataRegistro(new Date());
				
				EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
				em.getTransaction().begin();
				
				pedidoPesoDao = new PedidoPesoDaoJpa(em);
				
				pedidoPesoDao.salvar(pedidoPeso);
				em.getTransaction().commit();
				em.close();
				PersistenceManager.INSTANCE.close();
				System.out.println("Gravando...");

			}
		});

		adiciona(botaoGravar, 130, 121, 100, 25);
	}

	private void preparaBotaoSair() {
		botaoSair = new JButton("Sair");
		botaoSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		painelPrincipal.add(botaoSair);
		
	}
	
	private void adiciona(Component component, int nColuna, int nLinha, int nLargura, int nAltura) {
		painelPrincipal.add(component);
		component.setBounds(nColuna, nLinha, nLargura, nAltura);
	}

}
