/* Copyright (c) 2014, Green Lightning <GreenLightning.online@googlemail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package eu.greenlightning.nsdg.xml;

import static javax.xml.stream.XMLStreamConstants.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

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

	public Element parseDiagram() throws ParserException, XMLStreamException {
		startDocument();
		String title = getAttribute("title", null);
		startElement("diagram");
		Diagram diagram = new Diagram(title, parseChild());
		endElement("diagram");
		endDocument();
		return diagram;
	}

	private Element parseChild() throws ParserException, XMLStreamException {
		List<Element> children = new ArrayList<>();
		while (is(START_ELEMENT)) {
			String name = getNameOfStartElement();
			switch (name) {
				case "block":
					children.add(parseBlock());
					break;
				case "procedure":
					children.add(parseProcedure());
					break;
				case "break":
					children.add(parseBreak());
					break;
				case "branch":
					children.add(parseBranch());
					break;
				case "switch":
					children.add(parseSwitch());
					break;
				case "loop":
					children.add(parseLoop());
					break;
				default:
					throw new ParserException("Unknown child type: " + name);
			}
		}
		switch (children.size()) {
			case 0:
				return new Empty();
			case 1:
				return children.get(0);
			default:
				return new Sequence(children);
		}
	}

	private Element parseBlock() throws ParserException, XMLStreamException {
		return new Block(parseTextElement("block"));
	}

	private Element parseProcedure() throws ParserException, XMLStreamException {
		return new Procedure(parseTextElement("procedure"));
	}

	private Element parseBreak() throws ParserException, XMLStreamException {
		return new Break(parseTextElement("break"));
	}

	private String parseTextElement(String name) throws ParserException, XMLStreamException {
		StringBuilder text = new StringBuilder();
		startElement(name);
		while (is(CHARACTERS) || is(COMMENT)) {
			if (is(CHARACTERS)) {
				text.append(look().toString());
			}
			next();
		}
		endElement(name);
		return replaceEscapeSequences(trimLines(text.toString()));
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

	private Element parseBranch() throws ParserException, XMLStreamException {
		String condition = getAttribute("condition");
		startElement("branch");
		Labelled left = null, right = null;
		while (look().isStartElement()) {
			String name = getNameOfStartElement();
			if (name.equals("left")) {
				if (left != null) {
					throw new ParserException("Duplicate left branch.");
				}
				left = parseLabelled("left");
			} else if (name.equals("right")) {
				if (right != null) {
					throw new ParserException("Duplicate right branch.");
				}
				right = parseLabelled("right");
			} else {
				throw new ParserException("Unkown branch child: " + name);
			}
		}
		endElement("branch");
		return new Branch(condition, left == null ? new Labelled() : left, right == null ? new Labelled()
			: right);
	}

	private Element parseSwitch() throws ParserException, XMLStreamException {
		String expression = getAttribute("expression");
		startElement("switch");
		List<Labelled> cases = new ArrayList<>();
		Labelled defaultLabelled = null;
		while (look().isStartElement()) {
			String name = getNameOfStartElement();
			if (name.equals("case")) {
				cases.add(parseLabelled("case"));
			} else if (name.equals("default")) {
				if (defaultLabelled != null) {
					throw new ParserException("Duplicate default case.");
				}
				defaultLabelled = parseLabelled("default");
			} else {
				throw new ParserException("Unkown switch child: " + name);
			}
		}
		endElement("switch");
		if (cases.isEmpty()) {
			throw new ParserException("Switch needs at least one case.");
		}
		return new Switch(expression, cases, defaultLabelled);
	}

	private Labelled parseLabelled(String name) throws ParserException, XMLStreamException {
		String label = getAttribute("label");
		startElement(name);
		Element child = parseChild();
		endElement(name);
		return new Labelled(label, child);
	}

	private Element parseLoop() throws ParserException, XMLStreamException {
		String type = getAttribute("type", "test-first");
		switch (type) {
			case "test-first":
				return parseTestLoop(true);
			case "test-last":
				return parseTestLoop(false);
			case "infinite":
				return parseInfiniteLoop();
			default:
				throw new ParserException("Loop type must be 'test-first', 'test-last' or 'infinite'.");
		}
	}

	private Element parseTestLoop(boolean testFirst) throws ParserException, XMLStreamException {
		String condition = getAttribute("condition");
		String side = getAttribute("side");
		startElement("loop");
		Element child = parseChild();
		endElement("loop");
		return new TestLoop(testFirst, condition, side, child);
	}

	private Element parseInfiniteLoop() throws ParserException, XMLStreamException {
		String top = getAttribute("top");
		String side = getAttribute("side");
		String bottom = getAttribute("bottom");
		startElement("loop");
		Element child = parseChild();
		endElement("loop");
		return new InfiniteLoop(top, side, bottom, child);
	}

	private void startDocument() throws ParserException, XMLStreamException {
		checkAvailable("start of document");
		checkIsStartOfDocument();
		next();
		skip();
	}

	private void checkIsStartOfDocument() throws ParserException, XMLStreamException {
		if (!look().isStartDocument()) {
			throw new ParserException("Expected start of document, but found " + getEventDescription() + ".");
		}
	}

	private void endDocument() throws ParserException, XMLStreamException {
		checkAvailable("end of document");
		checkIsEndOfDocument();
		next();
		skip();
	}

	private void checkIsEndOfDocument() throws ParserException, XMLStreamException {
		if (!look().isEndDocument()) {
			throw new ParserException("Expected end of document, but found " + getEventDescription() + ".");
		}
	}

	private XMLEvent startElement(String name) throws ParserException, XMLStreamException {
		checkAvailable("start of element " + name);
		checkIsStartElement(name);
		checkStartElementHasName(name);
		XMLEvent event = next();
		skip();
		return event;
	}

	private void checkIsStartElement(String name) throws ParserException, XMLStreamException {
		if (!look().isStartElement()) {
			throw new ParserException("Expected start of " + name + ", but found " + getEventDescription()
				+ ".");
		}
	}

	private void checkStartElementHasName(String name) throws ParserException, XMLStreamException {
		String elementName = getNameOfStartElement();
		if (!elementName.equals(name)) {
			throw new ParserException("Expected start of " + name + ", but found start of " + elementName
				+ ".");
		}
	}

	private XMLEvent endElement(String name) throws ParserException, XMLStreamException {
		checkAvailable("end of " + name);
		checkIsEndElement(name);
		checkEndElementHasName(name);
		XMLEvent event = next();
		skip();
		return event;
	}

	private void checkIsEndElement(String name) throws ParserException, XMLStreamException {
		if (!look().isEndElement()) {
			throw new ParserException("Expected end of " + name + ", but found " + getEventDescription()
				+ ".");
		}
	}

	private void checkEndElementHasName(String name) throws ParserException, XMLStreamException {
		String elementName = getNameOfEndElement();
		if (!elementName.equals(name)) {
			throw new ParserException("Expected end of " + name + ", but found end of " + elementName + ".");
		}
	}

	private String getEventDescription() throws XMLStreamException {
		switch (look().getEventType()) {
			case START_DOCUMENT:
				return "start of document";
			case END_DOCUMENT:
				return "end of document";
			case START_ELEMENT:
				return "start of " + getNameOfStartElement();
			case END_ELEMENT:
				return "end of " + getNameOfEndElement();
			case COMMENT:
				return "comment";
			default:
				return "<unknown>";
		}
	}

	private String getNameOfStartElement() throws XMLStreamException {
		return look().asStartElement().getName().getLocalPart();
	}

	private String getNameOfEndElement() throws XMLStreamException {
		return look().asEndElement().getName().getLocalPart();
	}

	private String getAttribute(String name) throws XMLStreamException {
		return getAttribute(name, "");
	}

	private String getAttribute(String name, String alternative) throws XMLStreamException {
		Attribute attribute = look().asStartElement().getAttributeByName(new QName(name));
		return (attribute == null) ? alternative : replaceEscapeSequences(attribute.getValue());
	}

	private String replaceEscapeSequences(String data) {
		data = data.replaceAll("(?<!\\\\)\\\\t", Matcher.quoteReplacement("\t"));
		data = data.replaceAll("(?<!\\\\)\\\\b", Matcher.quoteReplacement("\b"));
		data = data.replaceAll("(?<!\\\\)\\\\n", Matcher.quoteReplacement("\n"));
		data = data.replaceAll("(?<!\\\\)\\\\r", Matcher.quoteReplacement("\r"));
		data = data.replaceAll("(?<!\\\\)\\\\f", Matcher.quoteReplacement("\f"));
		data = data.replaceAll("(?<!\\\\)\\\\a", Matcher.quoteReplacement("&"));
		data = data.replaceAll("(?<!\\\\)\\\\g", Matcher.quoteReplacement(">"));
		data = data.replaceAll("(?<!\\\\)\\\\k", Matcher.quoteReplacement("<"));
		data = data.replaceAll("(?<!\\\\)\\\\l", Matcher.quoteReplacement("<"));
		data = data.replaceAll("(?<!\\\\)\\\\p", Matcher.quoteReplacement("'"));
		data = data.replaceAll("(?<!\\\\)\\\\q", Matcher.quoteReplacement("\""));
		data = data.replaceAll("\\\\\\\\", Matcher.quoteReplacement("\\"));
		return data;
	}

	private void skip() throws XMLStreamException {
		while (skippable()) {
			next();
		}
	}

	private boolean skippable() throws XMLStreamException {
		if (!available()) {
			return false;
		}
		return is(COMMENT) || isWhitespace();
	}

	private boolean isWhitespace() throws XMLStreamException {
		return is(CHARACTERS) && look().asCharacters().isWhiteSpace();
	}

	private boolean is(int type) throws XMLStreamException {
		return look().getEventType() == type;
	}

	private void checkAvailable(String expected) throws ParserException {
		if (!available()) {
			throw new ParserException("Unexpected end of file. Expected " + expected + ".");
		}
	}

	private boolean available() {
		return reader.hasNext();
	}

	private XMLEvent look() throws XMLStreamException {
		return reader.peek();
	}

	private XMLEvent next() throws XMLStreamException {
		return reader.nextEvent();
	}

	@Override
	public void close() throws IOException, XMLStreamException {
		reader.close();
		input.close();
	}

}
