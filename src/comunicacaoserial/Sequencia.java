package comunicacaoserial;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Sequencia implements Serializable {
	public char[] seq;
	
	public Sequencia(char[] seq){
		
		this.seq = seq;
	}
	
	
	public char[] getSeq() {
		return seq;
	}

	public void setSeq(char[] seq) {
		this.seq = seq;
	}
	
	public void execSequencia(ControlePorta arduino) throws InterruptedException{
		for (int i = 0; i < 15; i++) {
			Thread.sleep(100);
			arduino.enviaDados(seq[i]);
			System.out.println(seq[i]);
		}
	}
}