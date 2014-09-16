package eu.greenlightning.nsdg;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;

import javax.xml.stream.XMLStreamException;

import eu.greenlightning.nsdg.elements.Element;
import eu.greenlightning.nsdg.xml.ParserException;
import eu.greenlightning.nsdg.xml.XMLParser;

public class NassiShneidermanDiagramGenerator {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java -jar nsdg.jar <path-to-xml-file>");
			System.out.println("Copyright © 2014 Green Lightning");
			return;
		}

		for (int i = 0; i < args.length; i++) {
			handle(args[i]);
			System.out.println();
		}
	}

	private static void handle(String argument) {
		System.out.println("Handling '" + argument + "':");

		Path path = Paths.get(argument);

		if (!Files.exists(path)) {
			System.out.println("The file '" + path + "' does not exist.");
			return;
		}

		Element diagram;

		try (XMLParser parser = new XMLParser(path)) {
			diagram = parser.parseDiagram();
		} catch (ParserException e) {
			System.out.println("Error: " + e.getMessage());
			return;
		} catch (IOException | XMLStreamException e) {
			System.out.println("An error occured while parsing the xml file.");
			System.out.println();
			e.printStackTrace();
			return;
		}

		Path imagePath = getImagePath(path);

		try (OutputStream out = Files.newOutputStream(imagePath)) {
			new ImageSaver().save(diagram, out);
		} catch (IOException e) {
			System.out.println("An error occured while saving the image.");
			System.out.println();
			e.printStackTrace();
			return;
		}

		System.out.println("Done writing " + imagePath + "!");
	}

	private static Path getImagePath(Path path) {
		return path.subpath(0, path.getNameCount() - 1).resolve(
			path.getFileName().toString().replace(".xml", ".png"));
	}

}
