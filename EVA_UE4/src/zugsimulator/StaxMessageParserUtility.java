
package zugsimulator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxMessageParserUtility {
    private final boolean     debug = false;
    protected int             event;
    protected XMLStreamReader parser;

    public StaxMessageParserUtility() {
    }

    public boolean open(final String doc) {
        try {
            final InputStream in = new ByteArrayInputStream(doc.getBytes());
            // Start
            final XMLInputFactory factory = XMLInputFactory.newInstance();
            parser = factory.createXMLStreamReader(in);
            event = parser.next();
        } catch (final XMLStreamException ex) {
            System.out.println("Exception " + ex);
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            parser.close();
        } catch (final XMLStreamException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected boolean isStartElement() {
        if (debug) {
            System.out.println("Call: isStartElement()");
        }
        final boolean b = parser.isStartElement();
        if (debug) {
            System.out.println("Exit: isStartElement() : " + b);
        }
        return (b);
    }

    protected boolean isKnownStartElement(final String name) throws ParserException {
        if (debug) {
            System.out.println("Call: isKnownStartElement(\"" + name + "\")");
        }
        if (event == XMLStreamConstants.START_ELEMENT) {
            if (debug) {
                System.out.println("test " + name + " " + parser.getLocalName());
            }
            if (name.equals(parser.getLocalName())) {
                if (debug) {
                    System.out.println("Exit: isKnownStartElement (\"" + name + "\") : ok");
                }
                return true;
            } else {
                throw new ParserException("StartElement " + name + " erwartet" + parser.getLocalName());
            }
        } else {
            throw new ParserException("StartElement erwartet" + getType() + " gefunden");
        }
    }

    protected String getStartElement() throws ParserException {
        if (debug) {
            System.out.println("Call: getStartElement()");
        }
        final String element = parser.getLocalName();
        try {
            event = parser.next();
        } catch (final XMLStreamException e) {
            e.printStackTrace();
            throw new ParserException();
        }
        return element;
    }

    protected boolean skipWhiteSpace() {
        if (debug) {
            System.out.println("Call: skipWhiteSpace");
        }
        if (parser.isWhiteSpace()) {
            if (debug) {
                System.out.println("Whitespaces");
            }
            try {
                event = parser.nextTag();
            } catch (final XMLStreamException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void skipToken() throws ParserException {
        if (debug) {
            System.out.println("Call: skipToken()");
        }
        try {
            event = parser.next();
            if (debug) {
                System.out.println("Exit: skipToken(): ok!");
            }
        } catch (final XMLStreamException e) {
            e.printStackTrace();
            throw new ParserException("ERROR: skip");
        }
    }

    protected void skipKnownStartElement(final String name) throws ParserException {
        if (debug) {
            System.out.println("Call: skipKnownStartElement(\"" + name + "\")");
        }
        if (isKnownStartElement(name)) {
            skipToken();
            if (debug) {
                System.out.println("Exit: skipKnownStartElement(\"" + name + "\") : ok!");
            }
        }
    }

    protected String getCharacters() throws ParserException {
        String value = null;
        if (debug) {
            System.out.println("Call: getCharacters()");
        }
        value = parser.getText();
        if (debug) {
            System.out.println("Exit: getCharacters() : " + value);
        }
        skipToken();
        return value;
    }

    // Aus Konstrukten der Form
    // <int>46</int>
    // wird der Wert extrahiert.
    protected String getKnownLeafElement(final String elementName) throws ParserException {
        if (debug) {
            System.out.println("Call: getKnownLeafElement(" + elementName + ")");
        }
        String elementWert = null;
        skipKnownStartElement(elementName);
        elementWert = getCharacters();
        skipKnownEndElement(elementName);
        if (debug) {
            System.out.println("Exit: getKnownLeafElement(" + elementName + ") : " + elementWert);
        }
        return elementWert;
    }

    protected boolean skipKnownEndElement(final String name) throws ParserException {
        if (debug) {
            System.out.println("Call: skipKnownEndElement(" + name + ")");
        }
        if (isKnownEndElement(name)) {
            try {
                event = parser.next();
                // skipWhiteSpace();
                if (debug) {
                    System.out.println("Call: skipKnownStartElement(" + name + ") : ok");
                }
            } catch (final XMLStreamException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    protected boolean isKnownEndElement(final String name) throws ParserException {
        if (debug) {
            System.out.println("Call: isKnownEndElement(" + name + ")");
        }
        if (event == XMLStreamConstants.END_ELEMENT) {
            if (debug) {
                System.out.println("test " + name + " " + parser.getLocalName());
            }
            if (name.equals(parser.getLocalName())) {
                if (debug) {
                    System.out.println("Skip \\" + name);
                }
                return true;
            } else {
                throw new ParserException("Elementname falsch");
            }
        } else {
            throw new ParserException("kein Endelement" + getType() + "<");
        }
    }

    public String getType() {
        final String theType = "";
        switch (event) {
            case XMLStreamConstants.ATTRIBUTE:
                if (debug) {
                    System.out.print("ATTRIBUTE");
                }
                break;
            case XMLStreamConstants.CDATA:
                if (debug) {
                    System.out.print("CDATA");
                }
                break;
            case XMLStreamConstants.CHARACTERS:
                if (debug) {
                    System.out.print("CHARACTERS: " + parser.getText());
                }
                break;
            case XMLStreamConstants.COMMENT:
                if (debug) {
                    System.out.print("COMMENT");
                }
                break;
            case XMLStreamConstants.DTD:
                if (debug) {
                    System.out.print("DTD");
                }
                break;
            case XMLStreamConstants.END_DOCUMENT:
                if (debug) {
                    System.out.print("END_DOCUMENT");
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                if (debug) {
                    System.out.print("END_ELEMENT");
                }
                break;
            case XMLStreamConstants.ENTITY_DECLARATION:
                if (debug) {
                    System.out.print("ENTITY_DECLARATION");
                }
                break;
            case XMLStreamConstants.ENTITY_REFERENCE:
                if (debug) {
                    System.out.print("ENTITY_REFERENCE");
                }
                break;
            case XMLStreamConstants.NAMESPACE:
                if (debug) {
                    System.out.print("NAMESPACE");
                }
                break;
            case XMLStreamConstants.NOTATION_DECLARATION:
                if (debug) {
                    System.out.print("NOTATION_DECLARATION");
                }
                break;
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                if (debug) {
                    System.out.print("PROCESSING_INSTRUCTION");
                }
                break;
            case XMLStreamConstants.SPACE:
                if (debug) {
                    System.out.print("SPACE");
                }
                break;
            case XMLStreamConstants.START_DOCUMENT:
                if (debug) {
                    System.out.print("START_DOCUMENT");
                }
                break;
            case XMLStreamConstants.START_ELEMENT:
                if (debug) {
                    System.out.print("START_ELEMENT " + parser.getLocalName());
                    // System.out.println(">>" + parser.getText() + "<<");
                }
                break;
            default:
                break;
        }
        return theType;
    }

    public void parseAll() {
        if (debug) {
            System.out.println("Call: ParseAll");
        }
        try {
            for (; event != XMLStreamConstants.END_DOCUMENT; event = parser.next()) {
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (debug) {
                            System.out.print("START_ELEMENT ");
                        }
                        if (debug) {
                            System.out.println(parser.getLocalName());
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (debug) {
                            System.out.print("END_ELEMENT ");
                        }
                        if (debug) {
                            System.out.println(parser.getLocalName());
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (debug) {
                            System.out.print("CHARACTERS ");
                        }
                        if (debug) {
                            System.out.println(parser.getText());
                        }
                        break;
                    case XMLStreamConstants.CDATA:
                        if (debug) {
                            System.out.print("CDATA ");
                        }
                        if (debug) {
                            System.out.println(parser.getText());
                        }
                        break;
                    default:
                        if (debug) {
                            System.out.print("finde ");
                        }
                        getType();
                } // end switch
            }
        } catch (final XMLStreamException e) {
            e.printStackTrace();
        } // end while
        if (debug) {
            System.out.println("Exit ParseAll");
        }
    }
}