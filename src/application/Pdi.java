package application;


import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Pdi {

	private static final int RED = 1;
	private static final int GREEN= 2;
	private static final int BLUE= 3;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getGrafico(Image img, BarChart<String,Number>grafico) {
	
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();
	    eixoX.setLabel("Intensidade");       
	    eixoY.setLabel("Valor");
	
	    XYChart.Series vlr = new XYChart.Series();//histograma é um vetor de inteiros 
		
	    vlr.setName("Intensidade");
	    
		int[] hist = histogramaUnico(img);//passa a imagem e retorna o histograma da imagem
		System.out.println("aqui foi");
		

		for (int i=0; i < hist.length; i++) {
			vlr.getData().add(new XYChart.Data(i+"", hist[i]));
		}
		grafico.getData().addAll(vlr);

		for(Node n:grafico.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill:red;");
		}
	}

	//MÉTODO MÉDIA ARITIMETICA
	public static Image cinzaMediaAritimetica(Image imagem) {//recebe uma imagem e os percentuais de cada cor

		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();//ler cada pixel da imagem
			WritableImage wi = new WritableImage(w,h);//
			PixelWriter pw = wi.getPixelWriter();// criar a imagem nova

			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i,j);// cada cor em cada pixel (cada cor tem 3 elementos R G B )/ atraves da cordenadas i j
					double media = (corA.getRed()+corA.getGreen()+corA.getBlue())/3;//media aritimética
					Color corN = new Color(media, media, media, corA.getOpacity());//nova cor/vetor de tamanho 3 + Opacidade
					pw.setColor(i, j, corN);//escrever essa nova cor no i, j 
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//MÉTODO MÉDIA PONDERADA
	public static Image cinzaMediaPonderada(Image imagem, int peso1, int peso2, int peso3) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();

			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i,j);
					double mediaP = (corA.getRed()*peso1+corA.getGreen()*peso2+corA.getBlue()*peso3)/(peso1+peso2+peso3);//media Ponderada
					//					if(pcR != 0 || pcG != 0 || pcB != 0)
					//						mediaP = (corA.getRed()*pcR + corA.getGreen()*pcG +corA.getBlue()*pcB)/100;//media ponderada
					Color corN = new Color(mediaP, mediaP, mediaP, corA.getOpacity());//vetor de tamanho 3 + Opacidade
					pw.setColor(i, j, corN);//escrever essa nova cor no i, j 
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//MÉTODO LIMIARIZAÇÃO
	public static Image limiarizacao(Image imagem ,double limiar) {
		try{
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();

			for(int i=0;i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i, j);//Cor antiga
					Color corN;//Cor nova

					if(corA.getRed() *255 >= limiar)//Se o canal escolhido for maior ou igual ao limiar...

						corN = new Color(1,1,1, corA.getOpacity());//coloca tudo branco
					else
						corN = new Color(0,0,0, corA.getOpacity());//senao, coloca tudo preto

					pw.setColor(i, j, corN);
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//MÉTODO NEGATIVA
	public static Image negativa(Image imagem) {
		try{
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();

			for(int i=0;i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i,j);//recupera a cor antiga
					Color corN = new Color(//Cor Nova / quano diminuo - 1 ele tranforma a cor em sua negativa
							1 - corA.getRed(),
							1 - corA.getGreen(),
							1 - corA.getBlue(),
							corA.getOpacity());
					pw.setColor(i, j, corN);

				}

			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//MÉTODO RUÍDOS
	public static Image eliminacaoRuidos(Image imagem, int tipoVizinhos ) {
		try{
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();

			for(int i=1;i<w-1; i++) {//desconsiderando as bordas
				for(int j=1; j<h-1; j++) {
					Color corA = pr.getColor(i,j);

					Pixel p = new Pixel(corA.getRed(), corA.getGreen(), corA.getBlue(),i, j);

					buscaVizinhos(imagem,p);//passa a imagem e o pixel e busca vinhos nas 3 modalidades

					Pixel vetor[] = null;
					//retornando os vetores preenchidos
					if(tipoVizinhos==Constantes.VIZINHOS3x3)
						vetor = p.viz3;
					if(tipoVizinhos==Constantes.VIZINHOSCRUZ)
						vetor = p.vizC;
					if(tipoVizinhos== Constantes.VIZINHOSX)
						vetor = p.vizX;
					//ordenando os vetores e pegando a sua mediana
					double red = mediana(vetor, Constantes.CANALR);
					double green = mediana(vetor, Constantes.CANALG);
					double blue = mediana (vetor, Constantes.CANALB);
					//substitui o pixel e coloca as medianas
					Color corN = new Color(red,green,blue,corA.getOpacity());
					pw.setColor(i,j, corN);
				}
			}
			return wi;
		}catch (Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	//metodo busca pixel vizinho
	private static void buscaVizinhos(Image imagem, Pixel p) {
		p.viz3 = buscaVizinho3(imagem, p);
		p.vizX = buscaVizinhoX(imagem, p);
		p.vizC = buscaVizinhoC(imagem, p);
	}

	//Metodo de busca por pixel
	private static Pixel[] buscaVizinhoX(Image imagem, Pixel p) {
		Pixel[] vX = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();

		Color cor = pr.getColor(p.x-1, p.y+1);
		vX[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y+1);

		cor = pr.getColor(p.x+1, p.y-1);
		vX[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y-1);

		cor = pr.getColor(p.x-1, p.y-1);
		vX[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y-1);

		cor = pr.getColor(p.x+1, p.y+1);
		vX[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y+1);
		vX[4] = p;

		return vX;
	}

	private static Pixel[] buscaVizinhoC(Image imagem, Pixel p) {
		Pixel[] vC = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();

		Color cor = pr.getColor(p.x, p.y+1);
		vC[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y+1);

		cor = pr.getColor(p.x, p.y-1);
		vC[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y-1);

		cor = pr.getColor(p.x-1, p.y);
		vC[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y);

		cor = pr.getColor(p.x+1, p.y);
		vC[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y);
		vC[4] = p;

		return vC;
	}





	private static Pixel[] buscaVizinho3(Image imagem, Pixel p) {
		Pixel[] v3 = new Pixel[9];
		PixelReader pr = imagem.getPixelReader();

		Color cor = pr.getColor(p.x, p.y+1);
		v3[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y+1);

		cor = pr.getColor(p.x, p.y-1);
		v3[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y-1);

		cor = pr.getColor(p.x-1, p.y);
		v3[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y);

		cor = pr.getColor(p.x+1, p.y);
		v3[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y);

		cor = pr.getColor(p.x-1, p.y+1);
		v3[4] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y+1);

		cor = pr.getColor(p.x+1, p.y-1);
		v3[5] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y-1);

		cor = pr.getColor(p.x-1, p.y-1);
		v3[6] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y-1);

		cor = pr.getColor(p.x+1, p.y+1);
		v3[7] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y+1);

		v3[8] = p;

		return v3;
	}

	private static double mediana (Pixel[] pixels, int canal) {

		double v[] = new double[pixels.length];

		for(int i=0; i<pixels.length; i++) {
			if(canal == Constantes.CANALR) {
				v[i] = pixels[i].r;
			}
			if(canal == Constantes.CANALG) {
				v[i] = pixels[i].g;
			}
			if(canal == Constantes.CANALB) {
				v[i] = pixels[i].b;
			}
		}
		v = ordenaVetor(v);
		return v[v.length/2];
	}

	private static double[] ordenaVetor(double[] v) {
		double aux;

		for (int x = 0; x < v.length; x++) {
			for (int y = x+1; y < v.length; y++) {
				if(v[x] > v[y] ){
					aux = v[x];
					v[x] = v[y];
					v[y] = aux;
				}
			}
		}

		return v;
	}

	////////////////////////////////////////////////////////////////////////
	public static Image adicao(Image img1, Image img2, double ti1, double ti2) {//ti1 e ti2 são as ponderações que eu quero fazer

		int w1 = (int)img1.getWidth();
		int h1 = (int)img1.getHeight();

		int w2 = (int)img2.getWidth();
		int h2 = (int)img2.getHeight();
		//pega o tamanho minimo da imagem, cria a terceira imagem do mesmo tamanho da imgaem menor
		int w = Math.min(w1, w2);
		int h = Math.min(h1, h2);

		PixelReader pr1 = img1.getPixelReader();
		PixelReader pr2 = img2.getPixelReader();
		WritableImage wi = new WritableImage(w,h);
		PixelWriter pw = wi.getPixelWriter();

		for(int i=1;i<w; i++) {
			for(int j=1; j<h; j++) {
				//esse metodo retorna as cores dos pixels
				Color corImg1 = pr1.getColor(i, j);
				Color corImg2 = pr2.getColor(i, j);
				//somar as cores dos pixels
				double r = (corImg1.getRed()*ti1) + (corImg2.getRed()*ti2);
				double g = (corImg1.getGreen()*ti1) + (corImg2.getGreen()*ti2);
				double b = (corImg1.getBlue()*ti1) + (corImg2.getBlue()*ti2);

				System.out.println("valor: "+r+"   "+g+"   "+b);
				//R é maior que o 1? é 1 senão é o proprio r 
				r = r>1 ? 1:r;
				g = g>1 ? 1:g;
				b = b>1 ? 1:b;
				System.out.println("valor: "+r+"   "+g+"   "+b);
				//opacidade minima traz transparencia e maxima ele fica opaco
				Color newCor = new Color(r,g,b,1);
				System.out.println("valor: "+r+"   "+g+"   "+b);
				pw.setColor(i, j, newCor);
				System.out.println("valor: "+r+"   "+g+"   "+b);
			}


		}
		return wi;
	}
	public static Image subtracao(Image img1, Image img2) {

		int w1 = (int)img1.getWidth();
		int h1 = (int)img1.getHeight();
		int w2 = (int)img2.getWidth();
		int h2 = (int)img2.getHeight();

		int w = Math.min(w1, w2);
		int h = Math.min(h1, h2);

		PixelReader pr1 = img1.getPixelReader();
		PixelReader pr2 = img2.getPixelReader();
		WritableImage wi = new WritableImage(w,h);
		PixelWriter pw = wi.getPixelWriter();

		for(int i=1;i<w; i++) {
			for(int j=1; j<h; j++) {

				Color oldCor1 = pr1.getColor(i, j);
				Color oldCor2 = pr2.getColor(i, j);

				double r = (oldCor1.getRed())-(oldCor2.getRed());
				double g = (oldCor1.getGreen())-(oldCor2.getGreen());
				double b = (oldCor1.getBlue())-(oldCor2.getBlue());


				r = r<0?0:r;
				g = g<0?0:g;
				b = b<0?0:b;

				Color newCor = new Color(r,g,b,oldCor1.getOpacity());
				pw.setColor(i, j, newCor);
			}


		}
		return wi;

	}

	public static Image moldura(Image img,int x1,int y1,int x2,int y2) {
		try{


			int w = (int)img.getWidth();
			int h = (int)img.getHeight();

			PixelReader pr1 = img.getPixelReader();

			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color corAntiga = pr1.getColor(i, j);
					pw.setColor(i, j, corAntiga);
				}
			}
			for (int k = x1;k < x2;k++) {
				Color corAntiga= pr1.getColor(k, y1);
				if(k <= x2) {
					double corR = (230/255);
					double corG = (9/255);
					double corB = (150/255);
					Color corNova = new Color(corR, corG, corB, corAntiga.getOpacity());
					pw.setColor(k, y1, corNova);
				}
			}
			for (int k = x1;k < x2;k++) {
				Color corAntiga= pr1.getColor(k, y2);
				if(k <= x2) {
					double corR = (230/255);
					double corG = (9/255);
					double corB = (150/255);
					Color corNova = new Color(corR, corG, corB, corAntiga.getOpacity());
					pw.setColor(k, y2, corNova);
				}
			}
			for (int k = y1;k < y2; k++) {
				Color corAntiga = pr1.getColor(x1, k);
				if(k <= y2) {
					double corR = (230/255);
					double corG = (9/255);
					double corB = (150/255);
					Color corNova = new Color(corR, corG, corB, corAntiga.getOpacity());
					pw.setColor(x1, k, corNova);
				}
			}for (int k = y1;k < y2; k++) {
				Color corAntiga = pr1.getColor(x2, k);
				if(k <= y2) {
					double corR = (230/255);
					double corG = (9/255);
					double corB = (150/255);
					Color corNova = new Color(corR, corG, corB, corAntiga.getOpacity());
					pw.setColor(x2, k, corNova);
				}
			}
			System.out.println("Aqui foi");
			return wi;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	public static int[] histogramaUnico(Image img) {
		img = cinzaMediaAritimetica(img);
		int[]qt = new int [256];

	
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++ ) {

				
				//contar quantos pixels tem de cada cor
				qt[(int)(pr.getColor(i, j).getRed()*255)]++;
				qt[(int)(pr.getColor(i, j).getGreen()*255)]++;
				qt[(int)(pr.getColor(i, j).getBlue()*255)]++;

			}
		}

		return qt;
	}

	public static Image equalizaHistograma(Image img, boolean todos) {

		int w = (int)img.getWidth();
		int h = (int)img.getHeight();

		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w,h);
		PixelWriter pw = wi.getPixelWriter();
		//histograma por canal 
		int[] hR = histograma(img,RED);
		int[] hB = histograma(img,GREEN);
		int[] hG = histograma(img,BLUE);
		//histograma acumulado, criando um segundo vetor  somando os anteriores
		int[] histAcR = histogramaAc(hR);
		int[] histAcG = histogramaAc(hB);
		int[] histAcB = histogramaAc(hG);
		//quantidade de tons validos por canal (255- a quantidade de 0)
		int qtTonsRed 	= qtTons(hR);
		int qtTonsBlue 	= qtTons(hB);
		int qtTonsGreen = qtTons(hG);
		// qual o valor minimo de cada histograma(primeiro pixel valido do histograma)
		double minR = pontoMin(hR);
		double minB = pontoMin(hB);
		double minG = pontoMin(hG);

		if(todos) {
			qtTonsRed = 255;
			qtTonsBlue 	= 255;
			qtTonsGreen = 255;
			minR = 0;
			minB = 0;
			minG = 0;

		}
		// n é a quantidade de pixel na imagem/w largura *h altura
		double n =w*h;

		for (int i=1; i<w; i++ ) {
			for(int j=1; j<h; j++) {
				Color oldCor= pr.getColor(i,j);
				//pegando o valor acumulado de um determinado pixel
				double acR = histAcR[(int)(oldCor.getRed()*255)];
				double acB = histAcB[(int)(oldCor.getBlue()*255)];
				double acG = histAcG[(int)(oldCor.getGreen()*255)];

				//quantidade de pixels validos
				double pxR = ((qtTonsRed-1)/n)*acR;
				double pxG = ((qtTonsGreen-1)/n)*acG;
				double pxB = ((qtTonsBlue-1)/n)*acB;

				//partir do ponto mínimo
				double corR = (minR+pxR)/255;
				double corG = (minG+pxG)/255;
				double corB = (minB+pxB)/255;


				Color newCor = new Color (corR,corG,corB,oldCor.getOpacity());
				pw.setColor(i,j,newCor);
			}
		}
		return wi;
	}
	private static int[] histograma(Image img1, int canal) {

		PixelReader pr = img1.getPixelReader();
		int w1 = (int)img1.getWidth();
		int h1 = (int)img1.getHeight();
		int[] vetor = new int[256];

		for (int i = 0; i < w1; i++) {
			for (int j = 0; j < h1; j++) {
				Color prevColor = pr.getColor(i, j);

				if (canal == Constantes.CANALR) {
					vetor[(int)(prevColor.getRed() * 255)]++;
				} else if (canal == Constantes.CANALG) {
					vetor[(int)(prevColor.getGreen() * 255)]++;
				}else if (canal == Constantes.CANALB) {
					vetor[(int)(prevColor.getBlue() * 255)]++;
				}
			}
		}
		return vetor;
	}


	public static int[] histogramaAc(int[] his){
		int[] tmp = new int[his.length];//Tmp  recebe histograma
		int vl = his[0];//vl recebe posição 1 do histograma
		for(int i=0; i<his.length-1; i++){
			tmp[i] = vl; 
			vl = vl+ his[i+1];
		}
		return tmp;
	}

	private static int qtTons(int[] t){
		int tons = 0;
		for (int i = 0; i < t.length; i++) {
			if (t[i] == 0) {
				tons++;
			}
		}
		return tons;
	}

	public static int pontoMin(int[] his) {
		for (int i = 0; i < his.length; i++ ) {
			if(his[i] >0) 
				return i;	
		}
		return 0;	
	}


	//Prova questão 2 

	public static Image diagonal(Image imagem) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();

			int[] hR = histograma(imagem,RED);
			int[] hB = histograma(imagem,GREEN);
			int[] hG = histograma(imagem,BLUE);

			int[] histAcR = histogramaAc(hR);
			int[] histAcG = histogramaAc(hB);
			int[] histAcB = histogramaAc(hG);

			int	qtTonsRed = 255;
			int	qtTonsBlue 	= 255;
			int qtTonsGreen = 255;
			double	minR = 0;
			double	minB = 0;
			double	minG = 0;


			// n é a quantidade de pixel na imagem/w largura *h altura
			double n =w*h;

			for (int i=1; i<w; i++ ) {
				for(int j=1; j<h; j++) {
					Color oldCor= pr.getColor(i,j);

					if(i>j) {
						//pegando o valor acumulado de um determinado pixel
						double acR = histAcR[(int)(oldCor.getRed()*255)];
						double acB = histAcB[(int)(oldCor.getBlue()*255)];
						double acG = histAcG[(int)(oldCor.getGreen()*255)];

						//quantidade de pixels validos
						double pxR = ((qtTonsRed-1)/n)*acR;
						double pxG = ((qtTonsGreen-1)/n)*acG;
						double pxB = ((qtTonsBlue-1)/n)*acB;

						//partir do ponto mínimo
						double corR = (minR+pxR)/255;
						double corG = (minG+pxG)/255;
						double corB = (minB+pxB)/255;


						Color newCor = new Color (corR,corG,corB,oldCor.getOpacity());
						pw.setColor(i,j,newCor);
					}else if(i<j){

						pw.setColor(i,j,oldCor);

					}else if(i==j) {
						Color newCor = new Color(0, 0, 0, 1);
						pw.setColor(i, j, newCor);
					}
				}}

			return wi;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//prova 1
	public static Image quadrante(Image imagem, int quadrante) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			int m=0;
			for(int i=0;i<w; i++) {
				if(i==w/2) {
					Color newCor = new Color(0, 0, 0, 1);
					pw.setColor(i, m, newCor);
				}
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i, j);//Cor antiga
					pw.setColor(i,j , corA);
		
					if(j==h/2) {
						Color newCor = new Color(0, 0, 0, 1);
						pw.setColor(i, j, newCor);
					}
					

				}
			}
			
			return wi;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Image rot180(Image imagem, int x, int y){
	
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			// organizar 4 quadrantes
			ArrayList<Color> quadrante1 = new ArrayList<>();
			ArrayList<Color> quadrante2 = new ArrayList<>();
			ArrayList<Color> quadrante3 = new ArrayList<>();
			ArrayList<Color> quadrante4 = new ArrayList<>();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					pw.setColor(i, j, pr.getColor(i, j));
				}
			}

			if (x == 1 || y == 1) {
				for (int i = 0; i < w/2; i++) {
					for (int j = 0; j < h/2; j++) {
						quadrante1.add(pr.getColor(i, j));
					}
				}

				int c = 0;
				for (int i = (w/2)-1; i > 0; i--) {
					for (int j = (h/2)-1; j > 0; j--) {
						pw.setColor(i, j, quadrante1.get(c));
						c++;
					}
					c++;
				}
			}

			if (x == 2 || y == 2) {
				for (int i = w/2; i < w; i++) {
					for (int j = 0; j < h/2; j++) {
						quadrante2.add(pr.getColor(i, j));
					}
				}

				int c = 0;
				for (int i = w-1; i > (w/2)-1; i--) {
					for (int j = (h/2)-1; j > 0; j--) {
						pw.setColor(i, j, quadrante2.get(c));
						c++;
					}
					c++;
				}
			}

			if (x == 3 || y == 3) {
				for (int i = 0; i < w/2; i++) {
					for (int j = h/2; j < h; j++) {
						quadrante3.add(pr.getColor(i, j));
					}
				}

				int c = 0;
				for (int i = (w/2)-1; i > 0; i--) {
					for (int j = h-2; j > (h/2)-1; j--) {
						pw.setColor(i, j, quadrante3.get(c));
						c++;
					}
					c++;
				}
			}

			if (x == 4 || y == 4) {
				for (int i = w/2; i < w; i++) {
					for (int j = h/2; j < h; j++) {
						quadrante4.add(pr.getColor(i, j));
					}
				}

				int c = 0;
				for (int i = w-1; i > (w/2)-1; i--) {
					for (int j = h-2; j > (h/2)-1; j--) {
						pw.setColor(i, j, quadrante4.get(c));
						c++;
					}
					c++;
				}
			
			return wi;
	}
			return null;
		
	}

//	//questão 3
//	public static Image identifica(Image imagem) {
//		int w = (int)imagem.getWidth();
//		int h = (int)imagem.getHeight();
//
//		PixelReader pr = imagem.getPixelReader();
//		WritableImage wi = new WritableImage(w,h);
//
//		int x1 = 0; 
//		int y1 = 0; 
//
//		for (int i = 0; i < w; i++) {
//			for (int j = 0; j < h; j++) {
//				if (pr.getColor(i, j).getBlue() == 0.0 &&
//					pr.getColor(i, j).getRed() == 0.0 &&
//					pr.getColor(i, j).getGreen() == 0.0) {
//					
//					Color corN=pr.getColor(i, j)
//					identifica(corN){
//					
//					
//					}
//				}
//			}
//		}
//		
//
//		return wi;
//	}
}






















