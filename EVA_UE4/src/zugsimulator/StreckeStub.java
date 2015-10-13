package zugsimulator;

import java.lang.reflect.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import java.util.LinkedList;

public class StreckeStub {

	public Object sendReceiveMsg(String dieNachricht) {
		System.out.println("\n\n\n");
		System.out.println("Nachricht empfangen:" + dieNachricht);

		StaxMessageParserUtility parser = new StaxMessageParserUtility();
		boolean messageOpened = parser.open(dieNachricht);
		if (!messageOpened)
			return null;
		try {
			// methodCall
			parser.getStartElement();
			String methodName = parser.getKnownLeafElement("methodName");
			// params
			parser.getStartElement();
			
			while (parser.isKnownStartElement("param")) {
				parser.skipKnownStartElement("param");
				parser.skipKnownStartElement("value");
				try {
					System.out.println(parser.getKnownLeafElement("int"));
				} catch (Exception e) {
					System.out.println(parser.getKnownLeafElement("boolean"));
				}
				parser.skipKnownEndElement("value");
				parser.skipKnownEndElement("param");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// Rückgabewert

		Object res = null;

		// TODO:
		// - Scannen der XML-Machricht
		// - Aufruf der entsprechenden Methode
		// - Bestimmung des Rückgabewertes
		// - Aufbau der Antwortnachricht

		return res;
	}
}