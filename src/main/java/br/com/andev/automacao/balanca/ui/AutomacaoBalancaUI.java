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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.com.andev.automacao.balanca.SerialComm;
import br.com.andev.automacao.balanca.ThreadCounter;
import br.com.andev.automacao.balanca.dao.PedidoPesoDao;
import br.com.andev.automacao.balanca.dao.PedidoPesoDaoJpa;
import br.com.andev.automacao.balanca.dao.PersistenceManager;
import br.com.andev.automacao.balanca.model.PedidoPeso;

public class AutomacaoBalancaUI /*implements Runnable*/ {

	private JFrame janela;

	private JPanel painelPrincipal;
	
	private JLabel jlNumPedido, jlNumNotaFiscal, jlOperador, jlPeso;
	private JTextField jtfNumPedido, jtfNumNotaFiscal, jtfOperador;
	private JTextField jtfPeso;
	
	private JButton botaoGravar, botaoSair, botaoConectar;
	
	private PedidoPesoDao pedidoPesoDao;
	
	private InputStream in;
	private String dadoLido = new String();
	
	private CustomThread customThread;
	
	private boolean portaConectada;
	
	public String getDadoLido() {
		return dadoLido;
	}
	
	public void setDadoLido(String dadoLido) {
		this.dadoLido = dadoLido;
	}

	public static void main(String[] args) {
//		try {
//			
//			SerialComm serialComm = new SerialComm();
////			serialComm.connect("/dev/ttyUSB02");
//			serialComm.connect("COM3");
//			
//			(new Thread(new AutomacaoBalancaUI(serialComm.getInputStream()))).start();
//			
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		 EventQueue.invokeLater(new Runnable()
	        {
	            public void run()
	            {
	                new AutomacaoBalancaUI().montaTela();
	            }
	        });
		
	}
	
	public AutomacaoBalancaUI() {}
	
	public AutomacaoBalancaUI(InputStream in) {
		this.in = in;
		montaTela();
		
		Component[] components = painelPrincipal.getComponents();
		
		for (Component component : components) {
			System.out.println(component.getName());
		}
	}

	public void montaTela() {
		preparaJanela();
		preparaPainelPrincipal();
		preparaCampos();
		preparaBotaoGravar();
		preparaBotaoConectar();
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

		jtfPeso = new JTextField("9999999", 20);
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
	
	private void preparaBotaoConectar() {
		botaoConectar = new JButton("Conectar");
		botaoConectar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					
					SerialComm serialComm = new SerialComm();
					
					if (!portaConectada) {
						
//						serialComm.connect("/dev/ttyUSB02");
						serialComm.connect("COM1");
						
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
		
		adiciona(botaoConectar, 130, 160, 100, 25);
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
	
//	public void run() {
//        byte[] buffer = new byte[1024];
//        int len = -1;
//        try {
//            while ((len = this.in.read(buffer)) > -1 ) {
//                //System.out.print(new String(buffer,0,len));
//                setDadoLido(new String(buffer,0,len));
//                System.out.print(getDadoLido());
//                atualizaPeso(getDadoLido());
//                
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }  
//        
//    }
//	
//	public void atualizaPeso(final String peso) {
//		Runnable atualizaPeso = new Runnable() {
//		     public void run() {
//		    	 System.out.println("Atualizando Peso: " + peso);
//		    	 jtfPeso.setText(peso);
//		     }
//		 };
//
//		 SwingUtilities.invokeLater(atualizaPeso);
//	}

}

class CustomThread extends Thread {
	
	private JTextField tField;
	
	private InputStream in;
	
	private String dataRead;
	
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
            	this.dataRead = new String(buffer,0,len);
                System.out.print(new String(buffer,0,len));
                
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        tField.setText(dataRead);
                    }
                });
                
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }  
		
	}
	
}
