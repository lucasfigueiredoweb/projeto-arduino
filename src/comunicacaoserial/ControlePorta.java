package comunicacaoserial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JOptionPane;


public class ControlePorta {
	
	  protected OutputStream serialOut;
	  protected int taxa;
	  protected String portaCOM;
	 
	  /**
	   * Construtor da classe ControlePorta
	   * @param portaCOM - Porta COM que ser� utilizada para enviar os dados para o arduino
	   * @param taxa - Taxa de transfer�ncia da porta serial geralmente � 9600
	   */
	  public ControlePorta(String portaCOM, int taxa) {
	    this.portaCOM = portaCOM;
	    this.taxa = taxa;
	    this.initialize();
	  }     
	 
	  /**
	   * M�doto que verifica se a comunica��o com a porta serial est� ok
	   */
	  private void initialize() {
	    try {
	      //Define uma vari�vel portId do tipo CommPortIdentifier para realizar a comunica��o serial
	      CommPortIdentifier portId = null;
	      try {
	        //Tenta verificar se a porta COM informada existe
	        portId = CommPortIdentifier.getPortIdentifier(this.portaCOM);
	      }catch (NoSuchPortException npe) {
	        //Caso a porta COM n�o exista ser� exibido um erro 
	        JOptionPane.showMessageDialog(null, "Porta COM n�o encontrada.",
	                  "Porta COM", JOptionPane.PLAIN_MESSAGE);
	      }
	      //Abre a porta COM 
	      SerialPort port = (SerialPort) portId.open("Comunica��o serial", this.taxa);
	      serialOut = port.getOutputStream();
	      port.setSerialPortParams(this.taxa, //taxa de transfer�ncia da porta serial 
	                               SerialPort.DATABITS_8, //taxa de 10 bits 8 (envio)
	                               SerialPort.STOPBITS_1, //taxa de 10 bits 1 (recebimento)
	                               SerialPort.PARITY_NONE); //receber e enviar dados
	    }catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	 
	  /**
	   * M�todo que fecha a comunica��o com a porta serial
	   */
	  public void close() {
	    try {
	        serialOut.close();
	    }catch (IOException e) {
	      JOptionPane.showMessageDialog(null, "N�o foi poss�vel fechar porta COM.",
	                "Fechar porta COM", JOptionPane.PLAIN_MESSAGE);
	    }
	  }
	 
	  /**
	   * @param opcao - Valor a ser enviado pela porta serial
	   */
	  public void enviaDados(char x){
	    try {
	      serialOut.write(x);//escreve o valor na porta serial para ser enviado
	    } catch (IOException ex) {
	        JOptionPane.showMessageDialog(null, "N�o foi poss�vel enviar o dado. ",
	                "Enviar dados", JOptionPane.PLAIN_MESSAGE);
	    }
	  } 
	}


