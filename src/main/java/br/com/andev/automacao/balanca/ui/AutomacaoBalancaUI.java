package br.com.andev.automacao.balanca.ui;

import gnu.io.NoSuchPortException;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import br.com.andev.automacao.balanca.ReadPort;
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
	
	private JButton botaoGravar, botaoSair, botaoConectar, botaoPorta;
	
	private PedidoPesoDao pedidoPesoDao;
	
	private CustomThread customThread;
	
	private boolean portaConectada;

	public static void main(String[] args) {
		
		setUIFont(new FontUIResource(new Font("Arial", 0, 20)));
		 
		 EventQueue.invokeLater(new Runnable() {
			 public void run() {
				AutomacaoBalancaUI automacaoBalancaUI = new AutomacaoBalancaUI();
	        	try {
	        		
	        		automacaoBalancaUI.montaTela();
	        		automacaoBalancaUI.conectaPortaSerial();
	        		
	        	}
	        	catch (NoSuchPortException noSuchPortException) {
	        		noSuchPortException.printStackTrace();
	        		automacaoBalancaUI.showMessage("Porta serial configurada não reconhecida");
	        	}
	        	catch (Exception e) {
	        		e.printStackTrace();
	        		automacaoBalancaUI.showMessage(e.getMessage());
	        	}
	         }
		 });
		
	}
	
	public static void setUIFont(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }

	public void montaTela() {
		preparaJanela();
		preparaPainelPrincipal();
		preparaCampos();
		preparaBotaoGravar();
//		preparaBotaoConectar();
		preparaBotaoPorta();
		mostraJanela();
	}
	
	private void limpaCampos() {
		jtfNumPedido.setText(null);
		jtfNumNotaFiscal.setText(null);
		jtfOperador.setText(null);
		jtfPeso.setText(null);
	}

	private void preparaJanela() {
		janela = new JFrame("Balança-Automação-v1.0.0");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void mostraJanela() {
		janela.pack();
		janela.setSize(640,640);
		janela.setVisible(true);
	}

	private void preparaPainelPrincipal() {
		painelPrincipal = new JPanel();
		painelPrincipal.setLayout(null);
		janela.add(painelPrincipal);
	}
	
	private void preparaCampos() {
		jlNumPedido = new JLabel("Pedido:");
		adiciona(jlNumPedido, 50, 100, 150, 35);
		
		jtfNumPedido = new JTextField(20);
		adiciona(jtfNumPedido, 300, 100, 500, 35);
		
		jlNumNotaFiscal = new JLabel("Nota Fiscal:");
		adiciona(jlNumNotaFiscal, 50, 140, 150, 35);

		jtfNumNotaFiscal = new JTextField(20);
		adiciona(jtfNumNotaFiscal, 300, 140, 500, 35);
		
		jlOperador = new JLabel("Operador:");
		adiciona(jlOperador, 50, 180, 150, 35);

		jtfOperador = new JTextField(20);
		adiciona(jtfOperador, 300, 180, 500, 35);
		
		jlPeso = new JLabel("Peso:");
		adiciona(jlPeso, 50, 220, 150, 35);

		jtfPeso = new JTextField(20);
		adiciona(jtfPeso, 300, 220, 500, 35);
	}

	private void preparaBotaoGravar() {
		botaoGravar = new JButton("Gravar");
		botaoGravar.setFont(botaoGravar.getFont().deriveFont(25f));
		botaoGravar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
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
					showMessage("Informações gravadas com sucesso!");
					System.out.println("Gravando...");
				} catch (Exception e) {
					e.printStackTrace();
					showMessage(e.getMessage());
				}
			}
		});

		adiciona(botaoGravar, 300, 300, 200, 35);
	}
	
	private void conectaPortaSerial() throws Exception {
		SerialComm serialComm = new SerialComm();
//		serialComm.connect("/dev/ttyUSB02");
		serialComm.connect("COM1");
		customThread = new CustomThread(jtfPeso, serialComm.getInputStream());
		customThread.start();
	}
	
	private void preparaBotaoPorta() {
		botaoPorta = new JButton("Porta Serial");
		botaoPorta.setFont(botaoPorta.getFont().deriveFont(25f));
		botaoPorta.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				try {
					ReadPort readPort = new ReadPort();
					List<String> ports = readPort.listPorts();
					
					String msgPorts = "Portas Seriais Reconhecidas:\n";
					
					if (!ports.isEmpty()) {
						for (String port : ports) {
							msgPorts += port + "\n";
						}
					} else {
						msgPorts += "Nenhuma";
					}
					
					showMessage(msgPorts);
				} catch (Exception e) {
					e.printStackTrace();
					showMessage(e.getMessage());
				}
			}
		});
		
		adiciona(botaoPorta, 2, 2, 300, 35);
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
	
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(janela, message);
	}
	
	public void showStackTrace(Exception e) {
		e.printStackTrace();
		StringBuilder sb = new StringBuilder(e.toString());
	    for (StackTraceElement ste : e.getStackTrace()) {
	        sb.append("\n\tat ");
	        sb.append(ste);
	    }
	    String trace = sb.toString();
		JOptionPane.showMessageDialog(janela, trace);
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
