package br.com.andev.automacao.balanca.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.andev.automacao.balanca.SerialComm;
import br.com.andev.automacao.balanca.dao.PedidoPesoDao;
import br.com.andev.automacao.balanca.dao.PedidoPesoDaoJpa;
import br.com.andev.automacao.balanca.dao.PersistenceManager;
import br.com.andev.automacao.balanca.model.PedidoPeso;

public class AutomacaoBalancaUI {

	private JFrame janela;

	private JPanel painelPrincipal;
	
	private JLabel jlNumPedido, jlNumNotaFiscal, jlOperador, jlPeso;
	private JTextField jtfNumPedido, jtfNumNotaFiscal, jtfOperador;
	private JTextField jtfPeso;
	
	private JButton botaoGravar, botaoSair, botaoConectar;
	
	private PedidoPesoDao pedidoPesoDao;
	
	private CustomThread customThread;
	
	private boolean portaConectada;

	public static void main(String[] args) {
		
		 EventQueue.invokeLater(new Runnable() {
			 public void run() {
	        	try {
	        		AutomacaoBalancaUI automacaoBalancaUI = new AutomacaoBalancaUI();
	        		automacaoBalancaUI.montaTela();
	        		automacaoBalancaUI.conectaPortaSerial();
	        		
	        	} catch (Exception e) {
	        		
	        	}
	         }
		 });
		
	}

	public void montaTela() {
		preparaJanela();
		preparaPainelPrincipal();
		preparaCampos();
		preparaBotaoGravar();
//		preparaBotaoConectar();
		mostraJanela();
	}
	
	private void limpaCampos() {
		jtfNumPedido.setText(null);
		jtfNumNotaFiscal.setText(null);
		jtfOperador.setText(null);
		jtfPeso.setText(null);
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
		jtfPeso.setName("peso");
		adiciona(jtfPeso, 130, 91, 300, 25);
	}

	private void preparaBotaoGravar() {
		botaoGravar = new JButton("Gravar");
		botaoGravar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PedidoPeso pedidoPeso = new PedidoPeso();
				pedidoPeso.setNumPedido(Long.parseLong(jtfNumPedido.getText()));
				pedidoPeso.setNumNotaFiscal(Long.parseLong(jtfNumNotaFiscal.getText()));
				pedidoPeso.setPeso(jtfPeso.getText().trim());
				pedidoPeso.setUnidade("KG");
				pedidoPeso.setOperador(jtfOperador.getText().trim());
				pedidoPeso.setDataRegistro(new Date());
				
				EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
				em.getTransaction().begin();
				
				pedidoPesoDao = new PedidoPesoDaoJpa(em);
				
				pedidoPesoDao.salvar(pedidoPeso);
				em.getTransaction().commit();
				em.close();
				//PersistenceManager.INSTANCE.close();
				
				limpaCampos();
				JOptionPane.showMessageDialog(janela, "Informações gravadas com sucesso!");
				System.out.println("Gravando...");

			}
		});

		adiciona(botaoGravar, 130, 121, 100, 25);
	}
	
	private void conectaPortaSerial() throws Exception {
		SerialComm serialComm = new SerialComm();
		serialComm.connect("/dev/ttyUSB02");
//		serialComm.connect("COM1");
		customThread = new CustomThread(jtfPeso, serialComm.getInputStream());
		customThread.start();
	}
	
	private void preparaBotaoConectar() {
		botaoConectar = new JButton("Conectar");
		botaoConectar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					
					SerialComm serialComm = new SerialComm();
					
					if (!portaConectada) {
						
						serialComm.connect("/dev/ttyUSB02");
//						serialComm.connect("COM1");
						
						customThread = new CustomThread(jtfPeso, serialComm.getInputStream());
						customThread.start();
						
						portaConectada = true;
						
						botaoConectar.setText("Desconectar");
					} else {
						
						//serialComm.getInputStream().close();
//						serialComm.disconnect();
//						
//						portaConectada = false;
//						
//						botaoConectar.setText("Conectar");
					}
					
					
					
				} catch (Exception e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
		
//		adiciona(botaoConectar, 130, 160, 100, 25);
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

class CustomThread extends Thread {
	
	private JTextField tField;
	
	private InputStream in;
	
	private String dataRead = new String();
	
	public CustomThread(JTextField tField, InputStream in) {
		this.tField = tField;
		this.in = in;
	}
	
	@Override
	public void run() {
		
		byte[] buffer = new byte[1024];
        int len = -1;
        try {
        	
            while ((len = this.in.read(buffer)) > -1 ) {
            	if (in.available() > 0) {
            		dataRead = "";
            	}
            	
            	dataRead += new String(buffer,0,len);
            	
            	EventQueue.invokeLater(new Runnable() {
            		
            		public void run() {
                        tField.setText(dataRead.trim());
                    }
                });
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }  
		
	}
	
}
