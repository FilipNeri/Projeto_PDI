package OpenCV;



import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
public class Teste {
	public static void main( String[] args )
	   {
	    
		Sobel();
		Canny();
		Laplaciano();
		Prewitt();
	   }
	
	public static void Canny(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		String filename = "C:\\Users\\filip\\Desktop\\Filipe\\Faculdade\\Processamento digital de Imagem\\Imagens\\imgs\\java.png";
        Mat color = Imgcodecs.imread(filename);
        
		Mat gray = new Mat();
		Mat draw = new Mat();
		Mat wide = new Mat();

		Imgproc.cvtColor(color, gray, Imgproc.COLOR_BGR2GRAY);
	
		Imgproc.Canny(gray, wide, 50, 150,3,false);	
		wide.convertTo(draw, CvType.CV_8U);
		
		ImShow im = new ImShow("Filtro Canny");
		im.showImage(draw);

//
//		if(Imgcodecs.imwrite("Canny_Java.png", draw)) {
//			System.out.println("borda detectada..........");
//
//		}
	}
	
	public static void Sobel() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		String filename = "C:\\Users\\filip\\Desktop\\Filipe\\Faculdade\\Processamento digital de Imagem\\Imagens\\imgs\\java.png";
        Mat color = Imgcodecs.imread(filename);        

        Mat gray = new Mat();
        Mat aresta = new Mat();
        
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();

  


        Imgproc.cvtColor(color, gray, Imgproc.COLOR_BGR2GRAY);

        // Gradient X
       
        Imgproc.Sobel(gray, grad_x, CvType.CV_16S, 1, 0, 3, 1, 0);
        Core.convertScaleAbs(grad_x, abs_grad_x);

        // Gradient Y

         Imgproc.Sobel(gray, grad_y, CvType.CV_16S, 0, 1, 3, 1, 0);
        Core.convertScaleAbs(grad_y, abs_grad_y);

        // Total Gradient (approximate)
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, aresta);


        ImShow im = new ImShow("Filtro Sobel");
		im.showImage(aresta);
	}
	
	public static void Laplaciano() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		String filename = "C:\\Users\\filip\\Desktop\\Filipe\\Faculdade\\Processamento digital de Imagem\\Imagens\\imgs\\java.png";
        Mat color = Imgcodecs.imread(filename);
        
		Mat gray = new Mat();
		Mat kernel = new Mat(3,3, CvType.CV_32F){
		       {
		          put(0,0,0);
		          put(0,1,-1);
		          put(0,2,0);

		          put(1,0-1);
		          put(1,1,4);
		          put(1,2,-1);

		          put(2,0,0);
		          put(2,1,-1);
		          put(2,2,0);
		       }
		    };
		Mat draw = new Mat();
		Mat wide = new Mat();

		Imgproc.cvtColor(color, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.filter2D(gray, draw, -1, kernel);
		Imgproc.Laplacian(draw, wide, -1);

		ImShow im = new ImShow("Filtro Laplaciano");
		im.showImage(wide);

	}
	public static void Prewitt(){
		
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
			String filename = "C:\\Users\\filip\\Desktop\\Filipe\\Faculdade\\Processamento digital de Imagem\\Imagens\\imgs\\java.png";
	        Mat color = Imgcodecs.imread(filename);
	        
			Mat mat = new Mat();
			

			int kernelSize = 9;
			
	        Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F) {
	        	{
	        		put(0,0,-1);
	        		put(0,1,0);
	                put(0,2,1);

	                put(1,0-1);
	                put(1,1,0);
	                put(1,2,1);

	                put(2,0,-1);
	                put(2,1,0);
	                put(2,2,1);
	             }
	          };	 
			
			Imgproc.filter2D(color, mat, -1, kernel); 
			ImShow im = new ImShow("Filtro Prewitt");
			im.showImage(mat);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Eliminar ruidos		
	Size size = new Size(3,3);
	Imgproc.blur(color,wide, size);
	*/
	
	public static void redimensionamento() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String filename = "C:\\Users\\filip\\eclipse-workspace\\TesteOpenCV\\src\\java.png";

        Mat img = Imgcodecs.imread(filename);
        Mat draw = new Mat();
		Mat wide = new Mat();
		System.out.println("aqui");
        Size size = new Size (600,300);
        Imgproc.resize(img, wide,size);
		
        wide.convertTo(draw, CvType.CV_8U);
		
		ImShow im = new ImShow("oi");
		im.showImage(draw);
	}
	

}
