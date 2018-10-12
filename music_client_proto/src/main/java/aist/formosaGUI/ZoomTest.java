package aist.formosaGUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
  
public class ZoomTest
{

    private static BufferedImage loadImage() throws IOException {
        String filename = "93987f51.jpg";
        
            //URL url = getClass().getResource(fileName);
        return ImageIO.read(new FileInputStream(filename));
    }

    
    public static void main(String[] args) throws IOException {
    	openPanel(loadImage(), null);
    }


	public static void openPanel(BufferedImage image, SelectedListener selected ) throws IOException {
		ImagePanel panel = new ImagePanel(image);
        JScrollPane scroll = new JScrollPane(panel);

		ImageZoom zoom = new ImageZoom(panel, scroll, selected);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(zoom.getUIPanel(), "North");
        
        f.getContentPane().add(scroll);
        f.setSize(1000,1000);
        f.setLocation(200,200);
        f.setVisible(true);
	}
}
  
class ImagePanel extends JPanel
{
    BufferedImage image;
    double scale;
  
    public ImagePanel(BufferedImage bufferedImage)
    {
    	image = bufferedImage;
        scale = 1.0;
        setBackground(Color.black);
    }
  
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (w - scale * imageWidth)/2;
        double y = (h - scale * imageHeight)/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }
  
    /**
     * For the scroll pane.
     */
    public Dimension getPreferredSize()
    {
        int w = (int)(scale * image.getWidth());
        int h = (int)(scale * image.getHeight());
        return new Dimension(w, h);
    }
  
    public void setScale(double s)
    {
        scale = s;
        revalidate();      // update the scroll pane
        repaint();
    }
  
}
  
class ImageZoom extends MouseAdapter{
    ImagePanel imagePanel;
    SelectedListener selected;
    SpinnerNumberModel model = new SpinnerNumberModel(0.5, 0.1, 2.0, .1);
    final JSpinner spinner = new JSpinner(model);
    JScrollPane scroll;
    
    public ImageZoom(ImagePanel ip, JScrollPane scroll, SelectedListener selected)
    {
        imagePanel = ip;
        this.selected = selected;
        this.scroll = scroll;
        ip.addMouseListener(this);
    }
 
    public void mouseClicked(MouseEvent e) {
    	double zoom = (Double)spinner.getValue();
    	System.out.println(zoom);
    	System.out.println(scroll.getViewport().getViewPosition());
    	double sx = scroll.getViewport().getViewPosition().getX();
    	double sy = scroll.getViewport().getViewPosition().getY();
    	int x = e.getX();
    	int y = e.getY();
    	System.out.println(x + " " + y + " " + sx + " " + sy);
    	selected.selected((int)(x / zoom), (int)(y / zoom));
    }
    
    public JPanel getUIPanel() {
        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                float scale = ((Double)spinner.getValue()).floatValue();
                imagePanel.setScale(scale);
            }
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("scale"));
        panel.add(spinner);
        float scale = ((Double)spinner.getValue()).floatValue();
        imagePanel.setScale(scale);
        return panel;
    }
}