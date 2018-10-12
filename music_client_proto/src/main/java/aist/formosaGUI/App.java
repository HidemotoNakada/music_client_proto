package aist.formosaGUI;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.Rasters;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;


/**
 * Hello world!
 * 
 */
public class App 
{
	@SuppressWarnings("serial")
	static class MainFrame extends JFrame{
	  
	  private JLabel label = new JLabel();
	  private ImageIcon imageIcon;
	  public MainFrame(ImageIcon imageIcon){
	    this.imageIcon = imageIcon;
	    super.setBounds(400,400,400,400);
	    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    this.label = new JLabel();
	    super.add(label);
	    label.setIcon(imageIcon);
	    super.setVisible(true);
	  }
	}	  

//	public static BufferedImage Mat2Image(Rasters src) {
//		// Mat srcのチャネル数を取得
//		int type = 0;
//		if (src.get
//				.channels() == 1) {
//			type = BufferedImage.TYPE_BYTE_GRAY;
//		} else if (src.channels() == 3) {
//			type = BufferedImage.TYPE_3BYTE_BGR;
//		} else {
//			return null;
//		}
//		// 新規BufferedImage型をsrcの幅，縦，チャネル数で作成．
//		BufferedImage image = new BufferedImage(src.getWidth(), src.getHeight(), type);
//		// 作成したBufferedImageからRasterを抜き出す.
//		WritableRaster raster = image.getRaster();
//		// 抜き出したRasterからバッファを抜き出す．
//		DataBufferByte Buf = (DataBufferByte) raster.getDataBuffer();
//		byte[] data = Buf.getData();
//		src.get(0, 0, data);
//
//		return image;
//	}

	
	
	
	private Rasters getRasters(String filename) throws IOException {
    	File input = new File(filename);
    	
    	TIFFImage tiffImage = TiffReader.readTiff(input);
    	List<FileDirectory> directories = tiffImage.getFileDirectories();
    	System.out.println(directories.size());
    	FileDirectory directory = directories.get(0);
    	Rasters rasters = directory.readRasters();
    	System.out.println(rasters);    	
    	System.out.println("size = " + rasters.getHeight() + "," + rasters.getWidth());
    	return rasters;
	}
	
	public static void main( String[] args ) {

		
		String imgFile = "93987f51.jpg";
		ImageIcon icon = new ImageIcon(imgFile);
		MainFrame mainFrame = new MainFrame(icon);
		mainFrame.setVisible(true);
	}
}

