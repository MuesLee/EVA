
package zugsimulator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class StreckeStub {
    private Object classObj;
    Class          clazz;

    public StreckeStub() {
        try {
            clazz = Strecke.class;
            // clazz = Class.forName("Strecke");
            classObj = clazz.newInstance();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public Object sendReceiveMsg(final String dieNachricht) {
        System.out.println("\n\n\n");
        System.out.println("Nachricht empfangen:" + dieNachricht);
        final StaxMessageParserUtility parser = new StaxMessageParserUtility();
        final boolean messageOpened = parser.open(dieNachricht);
        if (!messageOpened) {
            return null;
        }
        try {
            final List<Class> paraClass = new LinkedList<>();
            final List<Object> paraObj = new LinkedList<>();
            // methodCall
            parser.getStartElement();
            final String methodName = parser.getKnownLeafElement("methodName");
            // params
            parser.getStartElement();
            if (!methodName.equals("getStreckenLaenge") && !methodName.equals("getAbschnitt")) {
                parser.skipKnownStartElement("param");
                parser.skipKnownStartElement("value");
                paraClass.add(Integer.class);
                paraObj.add(Integer.valueOf(parser.getKnownLeafElement("int")));
                parser.skipKnownEndElement("value");
                parser.skipKnownEndElement("param");
            }
            if (!methodName.equals("getStreckenLaenge")) {
                parser.skipKnownStartElement("param");
                parser.skipKnownStartElement("value");
                paraClass.add(Boolean.class);
                paraObj.add(Boolean.parseBoolean(parser.getKnownLeafElement("boolean")));
                parser.skipKnownEndElement("value");
                parser.skipKnownEndElement("param");
            }
            if (!methodName.equals("verlassen") && !methodName.equals("getStreckenlaenge")) {
                parser.skipKnownStartElement("param");
                parser.skipKnownStartElement("value");
                paraClass.add(Integer.class);
                paraObj.add(Integer.valueOf(parser.getKnownLeafElement("int")));
                parser.skipKnownEndElement("value");
                parser.skipKnownEndElement("param");
            }
            return callMethod(methodName, paraClass, paraObj);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object callMethod(final String methodName, final List<Class> paraClass, final List<Object> paraObj)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        final Method mtd = clazz.getMethod(methodName, paraClass.toArray(new Class[0]));
        return mtd.invoke(classObj, paraObj.toArray(new Object[0]));
    }
}