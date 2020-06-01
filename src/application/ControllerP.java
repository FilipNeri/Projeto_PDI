package application;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControllerP {

	@FXML ImageView imageView1;
	@FXML ImageView imageView2;
	@FXML ImageView imageView3;

	@FXML private RadioButton selectC;
	@FXML private RadioButton selectX;
	@FXML private RadioButton select3x3;
	
	@FXML private TextField textQuadrante1;
	@FXML private TextField textQuadrante2;
	
	@FXML private TextField txtRedP;
	@FXML private TextField txtGreP;
	@FXML private TextField txtBluP;
	@FXML private CheckBox checkBoxMoldura;

	@FXML private Label lblR;
	@FXML private Label lblG;
	@FXML private Label lblB;

	private Image img1;
	private Image img2;
	private Image img3;

	@FXML private Slider slideLimiar;
	@FXML private Slider slideImg1;
	@FXML private Slider slideImg2;
	
	@FXML private int x1,y1,x2,y2;
	Pixel inicio, fim;



	@FXML
	public void abreImagem1() {
		img1 = abreImagem(imageView1,img1);
	}
	@FXML
	public void abreImagem2() {
		img2 = abreImagem(imageView2,img2);
	}


	@FXML
	private void atualizaImagem3() {
		imageView3.setImage(img3);
		imageView3.setFitWidth(img3.getWidth());
		imageView3.setFitHeight(img3.getHeight());



	} 


	private Image abreImagem(ImageView imageView, Image image) {
		
		File f = selecionaImagem();

		if(f != null) {
			image = new Image(f.toURI().toString());
			imageView.setImage(image);
			imageView.setFitHeight(image.getHeight());//colocando a a=largura para expandir
			imageView.setFitWidth(image.getWidth());//colocando altura

			return image;
		}
		return null;
	}

	private File selecionaImagem() {// selecionar uma imagem no diretorio e retornar
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new 
				FileChooser.ExtensionFilter(// tipos de imagens
						"Imagens", "*.jpg", "*.JPG", 
						"*.png", "*.PNG", "*.gif", "*.GIF", 
						"*.bmp", "*.BMP")); 	
		fileChooser.setInitialDirectory(new File(
				"C:\\Users\\filip\\Desktop\\Filipe\\Faculdade\\Processamento digital de Imagem\\Imagens"));
		File imgSelec = fileChooser.showOpenDialog(null);
		try {
			if (imgSelec != null) {
				return imgSelec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private void verificaCor(Image img, int x, int y){
		try {
			Color cor = img.getPixelReader().getColor(x-1, y-1);
			lblR.setText("R: "+(int) (cor.getRed()*255));
			lblG.setText("G: "+(int) (cor.getGreen()*255));
			lblB.setText("B: "+(int) (cor.getBlue()*255));
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	@FXML
	public void salvar() {


		WritableImage image = imageView3.snapshot(new SnapshotParameters(), null);
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("IMAGE files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(null);
		fileChooser.setInitialDirectory(new File(
				"C:\\Users\\filip\\Desktop\\Filipe\\Faculdade\\Processamento digital de Imagem\\Imagens"));

		try {
			if (file != null) {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	



	@FXML
	public void rasterImg(MouseEvent evt){
		ImageView iw = (ImageView)evt.getTarget();//elemento alvo que está gerando o evento de mouse
		if(iw.getImage()!=null)
			verificaCor(iw.getImage(), (int)evt.getX(), (int)evt.getY());//onde o ponteiro do mouse esta no eixo y e x  e verifica cor no loca
	}

	@FXML
	public void cinzaAritimetica() {
		img3 = Pdi.cinzaMediaAritimetica(img1);
		atualizaImagem3();

	}
	@FXML
	public void cinzaAritimeticaP() {
		int peso1 = Integer.parseInt(txtRedP.getText());
		int peso2 = Integer.parseInt(txtGreP.getText());
		int peso3 = Integer.parseInt(txtBluP.getText());
		img3 = Pdi.cinzaMediaPonderada(img1,peso1, peso2, peso3);
		atualizaImagem3();

	}

	@FXML
	public void limiarizacaoClick() {
		double valorLimiar =slideLimiar.getValue();
		img3 = Pdi.limiarizacao(img1,valorLimiar );
		atualizaImagem3();
	}

	@FXML
	public void negativa() {
		img3 = Pdi.negativa(img1);
		atualizaImagem3();
	}

	@FXML
	public void eliminaRuidos() {
		if(select3x3.isSelected()) {
			img3 = Pdi.eliminacaoRuidos(img1, 1);
			atualizaImagem3();
		}else if(selectC.isSelected()) {
			img3 = Pdi.eliminacaoRuidos(img1, 2);
			atualizaImagem3();
		}else if(selectX.isSelected()) {
			img3 = Pdi.eliminacaoRuidos(img1, 3);
			atualizaImagem3();
		}
	}
	
	@FXML
	public void add() {
		double valorImg1 =slideImg1.getValue()/100;
		double valorImg2 =slideImg2.getValue()/100;

		img3 = Pdi.adicao(img1, img2, valorImg1, valorImg2);
		atualizaImagem3();

	}
	@FXML
	public void sub() {
		img3 = Pdi.subtracao(img1, img2) ;
		atualizaImagem3();
	}
	
	@FXML
	public void moldura() {


		img3 =Pdi.moldura(img1,x1,y1,x2,y2);


		atualizaImagem3();
	}
	
	@FXML
	public void coordenada1(MouseEvent event) {
		ImageView iw = (ImageView)event.getTarget();
		if(checkBoxMoldura.isSelected()) {
			if(iw.getImage() != null) {
				x1=(int)event.getX();
				y1=(int)event.getY();

			}
		}
		
	}
	@FXML
	public void coordenada2(MouseEvent event) {
		ImageView iw = (ImageView)event.getTarget();
		if(checkBoxMoldura.isSelected() == true) {
			if(iw.getImage() != null) {
				x2=(int)event.getX();
				y2=(int)event.getY();
				moldura();
				
			}
		}
		
	}
	
	@FXML
	public void equalizacao() {
		img3=Pdi.equalizaHistograma(img1,true);
		atualizaImagem3();
	}
	@FXML
	public void equalizacaoValidos() {
		img3=Pdi.equalizaHistograma(img1,false);
		atualizaImagem3();

	}
	
	@FXML
	public void prova2() {
		img3 = Pdi.diagonal(img1);
		atualizaImagem3();
	}
	@FXML
	public void prova1() {
		int quadrante1 = Integer.parseInt(textQuadrante1.getText().toString());
		int quadrante2 = Integer.parseInt(textQuadrante2.getText().toString());
		img3 = Pdi.rot180(img1, quadrante1,quadrante2);
		atualizaImagem3();
	}
	@FXML
	public void prova3() {
	//	img3 = Pdi.identifica(img1);
		atualizaImagem3();
	}

	@FXML
	public void abreHistograma(ActionEvent event) {
		try {
			Stage stage = new Stage ();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Histograma.fxml"));
			
		
			Parent root = loader.load();

			stage.setScene(new Scene(root));
			stage.setTitle("Histograma");
			stage.initOwner(((Node)event.getSource()).getScene().getWindow());
			stage.show();
			

			
			HistogramaController controller = (HistogramaController)loader.getController();
			if(img1!= null)
				Pdi.getGrafico(img1,controller.grafico1);

			if(img2!= null)
				Pdi.getGrafico(img2,controller.grafico2);
			if(img3!= null)
				Pdi.getGrafico(img3,controller.grafico3);

		}catch(Exception e ) {

			e.printStackTrace();
		}

		
	}




} 








