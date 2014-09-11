package eu.greenlightning.nsdg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
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
			String name = reader.peek().asStartElement().getName().getLocalPart();
			switch (name) {
				case "block":
					children.add(parseBlock());
					break;
				case "branch":
					children.add(parseBranch());
					break;
			}
		}
		if (children.size() == 1) {
			return children.get(0);
		} else {
			return new Sequence(children);
		}
	}

	private Element parseBlock() throws XMLStreamException {
		StringBuilder text = new StringBuilder();
		reader.nextEvent(); // START_ELEMENT block
		while (reader.peek().getEventType() == XMLEvent.CHARACTERS) {
			text.append(reader.nextEvent().toString());
		}
		reader.nextEvent(); // END_ELEMENT block
		skip();
		return new Block(trimLines(text.toString()));
	}

	private String trimLines(String input) {
		StringBuilder result = new StringBuilder();
		String[] parts = input.trim().split("\n");
		for (int i = 0; i < parts.length; i++) {
			result.append(parts[i].trim());
			if (i < parts.length - 1) {
				result.append('\n');
			}
		}
		return result.toString();
	}

	private Element parseBranch() throws XMLStreamException {
		Attribute conditionAttribute = reader.peek().asStartElement().getAttributeByName(
			new QName("condition"));
		String condition = conditionAttribute == null ? "" : conditionAttribute.getValue();
		reader.nextEvent(); // START_ELEMENT branch
		skip();
		Labelled left = new Labelled(), right = new Labelled();
		while (reader.peek().isStartElement()) {
			boolean isLeft = reader.peek().asStartElement().getName().getLocalPart().equals("left");
			Attribute labelAttribute = reader.peek().asStartElement().getAttributeByName(
				new QName("label"));
			String label = labelAttribute == null ? "" : labelAttribute.getValue();
			reader.nextEvent(); // START_ELEMENT left / right
			skip();
			Element child = reader.peek().isStartElement() ? parseChild() : new Block();
			reader.nextEvent(); // END_ELEMENT left / right
			skip();
			if (isLeft) {
				left = new Labelled(label, child);
			} else {
				right = new Labelled(label, child);
			}
		}
		reader.nextEvent(); // END_ELEMENT branch
		skip();
		return new Branch(condition, left, right);
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
