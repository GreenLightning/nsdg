package eu.greenlightning.nsdg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

import eu.greenlightning.nsdg.elements.*;

public class XMLParser implements AutoCloseable {

	private InputStream input;
	private XMLEventReader reader;

	public XMLParser(Path path) throws IOException, XMLStreamException {
		input = Files.newInputStream(path);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		reader = factory.createXMLEventReader(input);
		skip();
	}

	public Element parseDiagram() throws XMLStreamException {
		reader.nextEvent(); // START_DOCUMENT
		skip();
		reader.nextEvent(); // START_ELEMENT diagram
		skip();
		Diagram diagram = new Diagram(parseChild());
		reader.nextEvent(); // END_ELEMENT diagram
		skip();
		reader.nextEvent(); // END_DOCUMENT
		return diagram;
	}

	private Element parseChild() throws XMLStreamException {
		List<Element> children = new ArrayList<>();
		while (reader.peek().getEventType() != XMLEvent.END_ELEMENT) {
			children.add(parseBlock());
		}
		if (children.size() == 1) {
			return children.get(0);
		} else {
			return new Sequence(children);
		}
	}

	private Element parseBlock() throws XMLStreamException {
		StringBuilder text = new StringBuilder();
		reader.nextEvent(); // START_ELEMENT
		while (reader.peek().getEventType() == XMLEvent.CHARACTERS) {
			text.append(reader.nextEvent().toString());
		}
		reader.nextEvent(); // END_ELEMENT
		skip();
		StringBuilder result = new StringBuilder();
		String[] parts = text.toString().split("\n");
		for (int i = 0; i < parts.length; i++) {
			result.append(parts[i].trim());
			if (i < parts.length - 1) {
				result.append('\n');
			}
		}
		return new Block(result.toString().trim());
	}

	private void skip() throws XMLStreamException {
		while (skippable()) {
			reader.nextEvent();
		}
	}

	private boolean skippable() throws XMLStreamException {
		if (!reader.hasNext()) {
			return false;
		}
		XMLEvent event = reader.peek();
		boolean comment = event.getEventType() == XMLEvent.COMMENT;
		boolean whiteSpace = event.isCharacters() && event.asCharacters().isWhiteSpace();
		return comment || whiteSpace;
	}

	@Override
	public void close() throws IOException, XMLStreamException {
		reader.close();
		input.close();
	}

}
