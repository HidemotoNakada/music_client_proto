package aist.formosaGUI;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.imageio.plugins.tiff.*;


public class App2 {
	static String filename = "FS2_G053_MS_L4TWD97_20151219_020408.tif";
//	static String urlStr = "http://localhost:80/formosa/submit/";
	static String urlStr = "http://140.110.240.163:80/formosa/submit/";

	static class Selected implements SelectedListener {
		public void selected(int x , int y) {
			System.out.println(x + " " + y);
		}
	}
	

	@SuppressWarnings("serial")
	static class MainFrame extends JFrame {
	  
	  private JLabel label = new JLabel();
	  private ImageIcon imageIcon;
	  public MainFrame(ImageIcon imageIcon){
	    this.imageIcon = imageIcon;
	    super.setBounds(imageIcon.getIconWidth(), imageIcon.getIconHeight(), 
	    			imageIcon.getIconWidth(), imageIcon.getIconHeight());
	    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    this.label = new JLabel();
	    this.addMouseListener(new MouseAdapter() {
	  	  public void mouseClicked(MouseEvent e) {
	  		  System.out.println(e.getX() +" "+ e.getY()); 
	  	  }	
	    });
	    super.add(label);
	    label.setIcon(imageIcon);
	    super.setVisible(true);
	  }
	}
	
	static String postData(String urlStr, String payload) throws IOException {
		URL url = new URL(urlStr);
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection)con;
		http.setRequestMethod("POST"); // PUT is another valid option
		http.setDoOutput(true);
		
		byte[] out = payload.getBytes();
		int length = out.length;

		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		http.connect();
		OutputStream os = http.getOutputStream();
		os.write(out);
		
		InputStream is = http.getInputStream();
		return new String(is.readAllBytes());
	}
	
	
	
	static String genJson(int [][][] array, int [][] poslst) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"data4\":\n");
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) sb.append(",\n");
			sb.append("[");
			for (int j = 0; j < array[i].length; j++) {
				if (j != 0) sb.append(",\n");
				sb.append("[");
				for (int k = 0; k < array[i][j].length; k++) {
					if (k != 0) sb.append(",");
					sb.append(array[i][j][k]);
				}
				sb.append("]");
			}
			sb.append("]");
		}
		sb.append("],\n");
		sb.append("\"poslst\":");
		sb.append("[");
		for (int j = 0; j < poslst.length; j++) {
			if (j != 0) sb.append(",\n");
			sb.append("[");
			for (int k = 0; k < poslst[j].length; k++) {
				if (k != 0) sb.append(",");
				sb.append(poslst[j][k]);
			}
			sb.append("]");
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}

	static int [][][] genDummy() {
		int [][][] array = new int[3][][];
		for (int i = 0; i < 3; i++) {
			int [][] array2 = new int[5][];
			for (int j = 0; j < 5; j++) {
				int [] array3 = new int[6];
				for (int k = 0; k < 6; k++) {
					array3[k] = k;
				}
				array2[j] = array3;
			}
			array[i] = array2;
		}
		return array;
	}
	

	private static int[][] genPoslst() {
		return  new int[][] {{0, 0}};
	}



	public static void main(String[] args) throws IOException {
		int x = 352;
		int y = 1520;
//		int x = 372;
//		int y = 1540;
		int misterious_offset = 20;
		
//		System.out.println(genJson(genDummy(), genPoslst()));
		
		
				
		
		InputStream stream = new FileInputStream(filename); 

		ImageReader tiffReader = ImageIO.getImageReadersByFormatName("tiff").next();

		ImageInputStream input = ImageIO.createImageInputStream(stream);

		tiffReader.setInput(input);

		TIFFImageReadParam mTIFFImageReadParam = new TIFFImageReadParam();

		System.out.println("--- TIFFImageReadParam - default TagSets ---");
		mTIFFImageReadParam.getAllowedTagSets().forEach(System.out::println);

		mTIFFImageReadParam.removeAllowedTagSet(BaselineTIFFTagSet.getInstance());
		mTIFFImageReadParam.removeAllowedTagSet(FaxTIFFTagSet.getInstance());
		// mTIFFImageReadParam.removeAllowedTagSet(ExifParentTIFFTagSet.getInstance());
		// mTIFFImageReadParam.removeAllowedTagSet(GeoTIFFTagSet.getInstance());

		System.out.println("--- TIFFImageReadParam - after removing 2 TagSets ---");
		mTIFFImageReadParam.getAllowedTagSets().forEach(System.out::println);

		mTIFFImageReadParam.setDestinationOffset(new Point(20, 20));

		// Read primary image and IFD.
		BufferedImage image = tiffReader.read(0, mTIFFImageReadParam);

//		ImageIcon icon = new ImageIcon(image);
//		MainFrame mainFrame = new MainFrame(icon);
//		mainFrame.setVisible(true);

		
		
		System.out.println("--- TIFFImage after retrieving---");
		System.out.println(image);
		System.out.println("- Height: " + image.getHeight());
		System.out.println("- Width: " + image.getWidth());

		IIOMetadata metaData = tiffReader.getImageMetadata(0);
		System.out.println("NativeMetadataFormatName: " + metaData.getNativeMetadataFormatName());
		//BufferedImage subImage = image.getSubimage(x, y, 16, 16);

		Raster raster = image.getRaster();
		System.out.println("" + raster.getWidth() + "," + raster.getHeight()); 
		System.out.println("banks = " + raster.getDataBuffer().getNumBanks());
		System.out.println("at xy = " + raster.getDataBuffer().getElem(0, x + y * raster.getWidth()));
		byte [] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int w = image.getWidth();
		
		ZoomTest.openPanel(image, new SelectedListener() {
			@Override
			public void selected(int x, int y) {
				System.out.println(x + " " + y);
				try {
					query(pixels, x, y, w);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		

		
		//int [] tmp =  new int[16 * 16 * 4];
		//int [] pixels = raster.getPixels(0, 0, 16, 16, tmp); 
//		DataBuffer db = raster.getDataBuffer();
//		System.out.println(db.getOffset() + " " + db.getSize());
		//		System.out.println(pixels);
//		System.out.println(tmp);
		
//		int offset = x + y * raster.getWidth();
//		for (int d = 0; d < 4; d++) {
//			System.out.println(pixels[offset* 4 + d]);
//		}
		
//		findSequence(pixels, new byte [] {67, 101, 99, 39, 68, 99} );
				
//		boolean iszero = true;  
//		int counter = 0;  
//		int flipped = 0;
//		for (int i = 0; i < pixels.length; i++) {
//			if (iszero) {
//				if (pixels[i] == 0 ) {  
//					counter++;
//				} else {
//					System.out.println("zeros: " + counter + " from: " + flipped + " to:" + (i - 1));
//					flipped = i;
//					counter = 1;
//					iszero = false;
//				}
//			} else {
//				if (pixels[i] != 0 ) {  
//					counter++;
//				} else {
//					System.out.println("non  : " + counter + " from: " + flipped + " to:" + (i - 1));
//					flipped = i;
//					counter = 1;
//					iszero = true;
//				}				
//			}
//		}
		


		
//		System.out.println(genJson(bytes, genPoslst()));
		
		// write Image
//		File newTIFFimage = new File("newTiffImage.tiff");
//		ImageIO.write(image, "TIFF", newTIFFimage);

	}

	static void query(byte [] pixels, int x, int y, int w) throws IOException {
		//int misterious_offset = 20;
		//x += misterious_offset;
		//y += misterious_offset;

		int [][][] bytes = new int[4][16][16];
		for (int c = 0; c < 4; c++)
			for (int i = 0; i < 16; i++) 
			for (int j = 0; j < 16; j++) {
				bytes[c][i][j] = getByte(pixels, w, x+j, y+i, c) ;
			}

		String reply = postData(urlStr, genJson(bytes, genPoslst()));
		System.out.println(reply);
	}
	
	private static void findSequence(byte [] pixels, byte[] bs) {
		outer:
		for (int i = 0; i < pixels.length - bs.length; i++) {
			for (int j = 0; j < bs.length; j++) {
				if (pixels[i + j] != bs[j])
					continue outer;
			}	
			System.out.println("found sequence " + bs + "at " + i );
		}
	}

	static int getByte(byte [] pixels, int w, int x, int y, int ch) {
		return ((int)(pixels[(x + y * w) * 4 + ch]) + 256) % 256;
	}


}