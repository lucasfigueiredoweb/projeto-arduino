package comunicacaoserial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Projeto extends Application {

	public Sequencia[] seq = new Sequencia[1];
	public char item;
	public char[] vetor = new char[100];
	File arquivo = new File("arquivo.txt");
	File contador = new File("contador.txt");
	public static int j = 0;
	public static int aux = 0;
	public static int aux2 = 0;
	public int max;
	public int n;
	public int i = 0;
	public char x = '0';
	public static boolean clicar = true;
	Button start = new Button("Start");
	Button play = new Button("Play");
	Button record = new Button("Record");
	Button refresh = new Button("Refresh");
	public static char[] sequencia = new char[100];
	public ControlePorta arduino;
	public static ComboBox<String> sequencias = new ComboBox<String>();
	Sphere esfera1 = new Sphere();
	Sphere esfera2 = new Sphere();
	Sphere esfera3 = new Sphere();
	Sphere esfera4 = new Sphere();
	final PhongMaterial redMaterial = new PhongMaterial();
	final PhongMaterial whiteMaterial = new PhongMaterial();
	final PhongMaterial greenMaterial = new PhongMaterial();
	final PhongMaterial orangeMaterial = new PhongMaterial();
	public static int y = 0;

	public Projeto() {
		arduino = new ControlePorta("COM6", 9600);
	}

	public void SalvarSequencia(char x) {
		sequencia[i] = x;
		i++;
	}

	public static int getTotal() throws IOException {
		File f = new File("contador.txt");
		BufferedReader buffread = new BufferedReader(new FileReader(f));
		String cont2 = buffread.readLine();
		buffread.close();
		int j = Integer.parseInt(cont2);
		return j;

	}

	// FUNCAO PARA SE COMUNICAR COM O ARDUINO AO CLICAR EM DETERMINADA COR
	public void comunicacaoArduino() {

		if (clicar == true) {
			arduino.enviaDados(x);
			// System.out.println((int) x);
		} else
			arduino.close();
	}

	// FUNCAO PARA CONTAR SEQUENCIAS
	@SuppressWarnings({ "resource", "unused" })
	public static int contarSeq() throws IOException {
		File f = new File("contador.txt");

		if (f.exists()) {
			BufferedReader buffread = new BufferedReader(new FileReader(f));
			String cont2 = buffread.readLine();
			int j = getTotal();
			// for(int x = 0;x < j+1;x++){
			// sequencias.getItems().add("Sequencia "+(x+1));
			// }
			j++;
			String c = Integer.toString(j);
			// System.out.println("printando con2 " + cont2);

			BufferedWriter buffwrite = new BufferedWriter(new FileWriter(f));
			buffwrite.write(c);
			buffwrite.close();
			return j;

		} else {
			BufferedWriter buffwrite = new BufferedWriter(new FileWriter(f));

			String cont = "1";
			sequencias.getItems().add("Sequencia 1");
			// System.out.println("printando cont "+cont);
			buffwrite.write(cont);
			buffwrite.close();

		}
		return j;
	}

	// FUNCAO PARA SALVAR O OBJETO NO ARQUIVO
	public static void salvar(Sequencia seq, String caminho) throws Exception {
		File f = new File("arduino.txt");

		if (f.exists()) {
			FileOutputStream saveFile = new FileOutputStream(f, true);
			AppendingObjectOutputStream stream = new AppendingObjectOutputStream(
					saveFile);

			stream.writeObject(seq);

			stream.close();

		} else {
			FileOutputStream saveFile = new FileOutputStream(f);
			ObjectOutputStream stream = new ObjectOutputStream(saveFile);

			stream.writeObject(seq);

			stream.close();
		}
	}

	// FUNCAO PARA LER O OBJETO SALVO NO ARQUIVO
	@SuppressWarnings("resource")
	public Sequencia restaurar(String caminho) {
		Sequencia seq = null;
		File f = new File("arduino.txt");

		if (f.exists()) {
			y = 2;
			try {
				FileInputStream restFile = new FileInputStream("arduino.txt");
				ObjectInputStream stream = new ObjectInputStream(restFile);

				String seque = sequencias.getValue();

				if (seque == null) {
					System.out.println("Selecione uma sequencia!");
					return null;
				}
				String parts[] = seque.split(" ");

				int y = Integer.parseInt(parts[1]);
				System.out.println("Sequencia " + y + " executando...");

				for (int x = 0; x < y - 1; x++) {
					seq = (Sequencia) stream.readObject();
				}

				seq = (Sequencia) stream.readObject();
				seq.execSequencia(arduino);
				stream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			y = 1;

		return seq;
	}

	// FUNCAO PARA SALVAR CARACTERES DA SEQUENCIA
	// public void salvarArquivo(char[] sequencia, File arquivo)
	// throws IOException {
	// BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true));
	// for (int i = 0; i < max; i++) {
	// bw.write(sequencia[i]);
	// bw.newLine();
	// System.out.println(sequencia[i]);
	// n++;
	//
	// }
	// bw.newLine();
	// bw.close();
	// }

	// FUNCAO PARA LER CARACTERE POR CARACRETE(OBS:LEITURA DE TODOS OS
	// CARACTERES EXISTENTES NO ARQUIVO)
	// public void lerArquivo(File arquivo) throws IOException {
	// Scanner scan = new Scanner(arquivo);
	// while (scan.hasNext()) {
	// // char linha = (char) br.read() ;
	// // System.out.println(linha);
	// sequencia[i] = scan.next().charAt(0);
	// arduino.enviaDados(sequencia[i]);
	// // vetor[j] = sequencia[i];
	// try {
	// Thread.sleep(600);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// System.out.println(sequencia[i]);
	// // System.out.println(vetor);
	//
	// }
	//
	// scan.close();
	//
	// }

	public void start(Stage stage) throws Exception {

		// sphere
		redMaterial.setSpecularColor(Color.WHITE);
		redMaterial.setDiffuseColor(Color.RED);
		whiteMaterial.setSpecularColor(Color.WHITE);
		whiteMaterial.setDiffuseColor(Color.LIGHTBLUE);
		greenMaterial.setSpecularColor(Color.YELLOW);
		greenMaterial.setDiffuseColor(Color.GREEN);
		orangeMaterial.setSpecularColor(Color.WHITE);
		orangeMaterial.setDiffuseColor(Color.ORANGE);

		esfera1.setMaterial(redMaterial);
		esfera1.setRadius(175);
		esfera1.setOpacity(200);
		esfera2.setMaterial(whiteMaterial);
		esfera2.setRadius(175);
		esfera2.setOpacity(200);
		esfera3.setMaterial(greenMaterial);
		esfera3.setRadius(175);
		esfera3.setOpacity(200);
		esfera4.setMaterial(orangeMaterial);
		esfera4.setRadius(175);
		esfera4.setOpacity(200);

		// Estilos e Localizacao dos botoes
		sequencias.disableProperty().set(true);
		play.setStyle("-fx-font: 18 arial; -fx-base: #C0C0C0;");
		record.setStyle("-fx-font: 18 arial; -fx-base: #C0C0C0;");
		start.setStyle("-fx-font: 18 arial; -fx-base: #C0C0C0;");
		sequencias.setStyle("-fx-font: 18 arial; -fx-base: #C0C0C0;");
		refresh.setStyle("-fx-font: 18 arial; -fx-base: #C0C0C0;");
		sequencias.setTranslateX(-35);
		refresh.setTranslateX(-25);
		start.setTranslateX(-5);
		play.setTranslateX(5);

		Group root = new Group();
		// ANTIGOS RETANTGULOS
		// final Rectangle black = new Rectangle();
		// black.setWidth(700);
		// black.setHeight(700);
		// black.setFill(Color.BLACK);
		// final Rectangle vermelho = new Rectangle();
		// vermelho.setWidth(350);
		// vermelho.setHeight(350);
		// vermelho.setFill(Color.RED);
		// final Rectangle retangulo2 = new Rectangle();
		// retangulo2.setWidth(350);
		// retangulo2.setHeight(350);
		// retangulo2.setFill(Color.WHITE);
		// final Rectangle retangulo3 = new Rectangle();
		// retangulo3.setWidth(350);
		// retangulo3.setHeight(350);
		// retangulo3.setFill(Color.ORANGE);
		// final Rectangle retangulo4 = new Rectangle();
		// retangulo4.setWidth(350);
		// retangulo4.setHeight(350);
		// retangulo4.setFill(Color.GREENYELLOW);

		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		HBox hbox3 = new HBox();
		HBox hbox0 = new HBox();

		hbox0.getChildren().add(sequencias);
		hbox0.getChildren().add(refresh);
		hbox1.getChildren().add(esfera1);
		hbox1.getChildren().add(esfera2);
		// hbox1.getChildren().add(black);
		hbox2.getChildren().add(esfera3);
		hbox2.getChildren().add(esfera4);

		hbox3.getChildren().add(start);
		hbox3.getChildren().add(record);
		hbox3.getChildren().add(play);

		VBox vbox = new VBox();
		vbox.getChildren().add(hbox0);
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
		vbox.getChildren().add(hbox3);
		root.getChildren().add(vbox);

		hbox0.setAlignment(Pos.TOP_CENTER);
		hbox1.setAlignment(Pos.CENTER);
		hbox2.setAlignment(Pos.CENTER);
		hbox3.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);
		root.setLayoutX(150);
		root.setLayoutY(35);
		root.setStyle("-fx-base: #C0C0C0;");
//		hbox1.setStyle("-fx-border-color:black");
//		hbox2.setStyle("-fx-border-color:black");
		// TELA
		stage.sizeToScene();
		Scene scene = new Scene(root, 1000, 850, Color.rgb(72, 72, 72));
		stage.setTitle("Projeto");
		stage.setScene(scene);
		stage.show();

		System.out
				.println("Pressione o botao Start! ou Escolha alguma Sequencia ja existente.");

		// BOTAO START
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				System.out.println("Escolha suas cores!");
				if (clicar) {
					esfera1.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent event) {
							x = '1';
							comunicacaoArduino();
							SalvarSequencia(x);
							System.out.println("VERMELHO - 1");
							max++;
							// clicar = false;
						}
					});
					esfera2.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent event) {
							x = '2';
							comunicacaoArduino();
							SalvarSequencia(x);
							System.out.println("AZUL - 2");
							max++;
							// clicar = false;
						}
					});
					esfera3.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent event) {
							x = '3';
							comunicacaoArduino();
							SalvarSequencia(x);
							System.out.println("VERDE - 4");
							max++;
							// clicar = false;
						}
					});
					esfera4.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent event) {
							x = '4';
							comunicacaoArduino();
							SalvarSequencia(x);
							System.out.println("LARANJA - 3");
							max++;
							// clicar = false;
						}
					});
				}
			}
		});

		// BOTAO PLAY
		play.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (y == 1) {
					Alert dialogoErro3 = new Alert(Alert.AlertType.INFORMATION);
					dialogoErro3.setTitle("Atencao");
					dialogoErro3
							.setHeaderText("Sem novas sequencias!, Grave uma sequencia!");
					dialogoErro3.setContentText("Obrigado");
					dialogoErro3.showAndWait();
				} else {
					restaurar("arduino.txt");
					x = '0';
				}
			}
		});

		// BOTAO RECORD
		record.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (x == '0') {
					Alert dialogoErro2 = new Alert(Alert.AlertType.INFORMATION);
					dialogoErro2.setTitle("Atencao");
					dialogoErro2
							.setHeaderText("Escolha pelo menos uma cor para gravar sua sequencia!");
					dialogoErro2.setContentText("Obrigado");
					dialogoErro2.showAndWait();
					System.out
							.println("Escolha pelo menos uma cor gravar sua sequencia!");
				} else {
					try {
						contarSeq();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					seq[0] = new Sequencia(sequencia);
					try {
						salvar(seq[0], "arduino.txt");
						System.out.println("Sequencia salva com sucesso!");
					} catch (Exception e) {
						e.printStackTrace();
					}
					x = '0';
					max = 0;
					i = 0;
					aux++;

				}
			}
		});

		// COMBOBOX
		sequencias.setPromptText("Selecionar Sequencia");
		sequencias.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				File f = new File("contador.txt");
				if (f.exists()) {
					System.out.println("Escolha sua sequencia!");
					BufferedReader buffread = null;
					try {
						buffread = new BufferedReader(new FileReader(f));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {
						@SuppressWarnings("unused")
						String cont2 = buffread.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					int j = 0;
					try {
						j = getTotal();
					} catch (IOException e) {
						e.printStackTrace();
					}
					sequencias.getItems().clear();
					for (int x = 0; x < j; x++) {
						sequencias.getItems().add("Sequencia " + (x + 1));
					}
				} else {
					Alert dialogoErro = new Alert(Alert.AlertType.ERROR);
					dialogoErro.setTitle("Atencao");
					dialogoErro
							.setHeaderText("Nao existe sequencia, crie uma nova sequencia!");
					dialogoErro.setContentText("Obrigado");
					dialogoErro.showAndWait();
					System.out
							.println("Nao existe sequencia, crie uma nova sequencia!");
				}
			}
		});

		refresh.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				sequencias.disableProperty().set(false);
			}
		});
	}

	// MAIN
	public static void main(String[] args) throws Exception {
		Projeto.launch(args);

	}

}