package zugsimulator;

import java.text.DateFormat;
import java.text.*;
import java.util.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream; 

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public class ClientStub // Stub
{


	private long seqNr = 1;

    public boolean reserviere(int zugnummer, boolean bRichtung, int iAbschnitt)
	{
        boolean b;
        b = (Boolean) sendMsg("reserviere", zugnummer, bRichtung, iAbschnitt);
        return b;
    }


    public void wechselnVon(int zugnummer, boolean bRichtung, int iAbschnitt)
	{
        sendMsg("wechselnVon", zugnummer, bRichtung, iAbschnitt);
    }


    public void wechselnNach(int zugnummer, boolean bRichtung, int iAbschnitt)
	{
        sendMsg("wechselnNach", zugnummer, bRichtung, iAbschnitt);
    }

    public void freigeben(int zugnummer, boolean bRichtung, int iAbschnitt)
	{
        sendMsg("freigeben", zugnummer, bRichtung, iAbschnitt);
    }


    public boolean verlassen(int zugnummer, boolean bRichtung)
	{
        boolean b;
        b = (Boolean) sendMsg("verlassen", zugnummer, bRichtung, -1);  // -1 als Dummy
        return b;
    }

    public int getStreckenLaenge()
	{
        int iLen = -1;

        // Todo returnwert ???
        iLen = (Integer )sendMsg("getStreckenLaenge", -1, false, -1);  // -1 als Dummy
        return iLen;
    }

    public int getAbschnitt(boolean bRichtung, int iAbschnitt)
	{
        int iAbschn = -1;

        // Todo returnwert ???
        iAbschn = (Integer) sendMsg("getAbschnitt", -1, false, iAbschnitt);  // -1 als Dummy
        return iAbschn;
    }





//----------------------------------------------------------------------------------------------------



    public Object sendMsg(String action, int zugnummer, boolean bRichtung, int iAbschnitt)
	{

        String strRichtung;
        String strDatum = "";
        String strUhrzeit = "";

//      String fileName = "c:\\Test.xml"; 
  	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        XMLOutputFactory xof =  XMLOutputFactory.newInstance(); 
        XMLStreamWriter xtw = null; 

		if(bRichtung == true)
		{
			strRichtung = "true";
	    }
	    else
	    {
	    	    strRichtung = "false";
	   }

        Date now = new Date();
        DateFormat dfDate = DateFormat.getDateInstance(DateFormat.LONG, Locale.GERMAN);
        DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);

        strDatum   = dfDate.format(now);
        strUhrzeit = dfTime.format(now);


    try 
    { 
//        xtw = xof.createXMLStreamWriter(new FileOutputStream(fileName), "UTF-8"); 
        xtw = xof.createXMLStreamWriter(outputStream, "UTF-8"); 
        xtw.writeStartDocument("UTF-8", "1.0"); 
//        xtw.writeComment("Entfernter Methodenaufruf"); 
        xtw.writeStartElement("methodCall");
         
        xtw.writeStartElement("methodName"); 
        xtw.writeCharacters(action);
        xtw.writeEndElement(); 

        xtw.writeStartElement("params"); 

        if (!action.equals("getStreckenLaenge") && !action.equals("getAbschnitt"))
        {
            xtw.writeStartElement("param"); 
            xtw.writeStartElement("value"); 
            xtw.writeStartElement("int"); 
            xtw.writeCharacters(""+zugnummer);
            xtw.writeEndElement(); 
            xtw.writeEndElement(); 
            xtw.writeEndElement(); 
        }  

        if (!action.equals("getStreckenLaenge"))
        {
            xtw.writeStartElement("param"); 
            xtw.writeStartElement("value"); 
            xtw.writeStartElement("boolean"); 
            xtw.writeCharacters(strRichtung);
            xtw.writeEndElement(); 
            xtw.writeEndElement(); 
            xtw.writeEndElement(); 
        }
        
        if (!action.equals("verlassen") && !action.equals("getStreckenlaenge"))
        {
            xtw.writeStartElement("param"); 
            xtw.writeStartElement("value"); 
            xtw.writeStartElement("int"); 
            xtw.writeCharacters(""+iAbschnitt);
            xtw.writeEndElement(); 
            xtw.writeEndElement(); 
            xtw.writeEndElement(); 
        }
        
        xtw.writeEndElement(); // params
        xtw.writeEndElement(); // methodCall
    } 
    catch (XMLStreamException e) 
    { 
        e.printStackTrace(); 
    } 
/*    	
    catch (IOException ie) 
    { 
        ie.printStackTrace(); 
    } 
*/
    finally 
    { 
        if (xtw != null) 
        { 
            try 
            { 
                xtw.close(); 
            } 
            catch (XMLStreamException e) 
            { 
                e.printStackTrace(); 
            } 
        }
    }





    String dieNachricht = new String(outputStream.toByteArray());

    System.out.println(dieNachricht);
    StreckeStub meinStreckeStub = new StreckeStub();
    Object o = meinStreckeStub.sendReceiveMsg(dieNachricht);
    seqNr++;
    return o;
    }
}
