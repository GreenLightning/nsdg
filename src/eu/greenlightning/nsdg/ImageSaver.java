package eu.greenlightning.nsdg;

import static java.awt.image.BufferedImage.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import eu.greenlightning.nsdg.elements.Element;

public class ImageSaver {

	public void save(Element element, OutputStream out) throws IOException {
		Font font = new Font("Sans", Font.PLAIN, 18);
		BufferedImage helper = new BufferedImage(1, 1, TYPE_INT_ARGB);
		Graphics2D g = helper.createGraphics();
		g.setFont(font);
		int width = element.getWidth(g);
		int height = element.getHeight(g);
		BufferedImage result = new BufferedImage(width, height, TYPE_INT_ARGB);
		g = result.createGraphics();
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		element.paint(g, width, height);
		g.dispose();
		ImageIO.write(result, "PNG", out);
	}

}
