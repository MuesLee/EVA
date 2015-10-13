package zugsimulator;
public class Strecke
{
	
    private SyncReservierung reserv = new SyncReservierung();
    private SyncBelegung   beleg  = new SyncBelegung();

    Abschnitt dieAbschnitte[];

/*
	public Strecke(int anzAbschnitte)
	{
         dieAbschnitte = new Abschnitt[anzAbschnitte];
         for(int i=0; i<anzAbschnitte; i++)
         {
         	dieAbschnitte[i] = new Abschnitt();
         }
	}

*/
	public Strecke()
	{
		 int anzAbschnitte = 61;
         dieAbschnitte = new Abschnitt[anzAbschnitte];
         for(int i=0; i<anzAbschnitte; i++)
         {
         	dieAbschnitte[i] = new Abschnitt();
         }
	}


// ----------------------------------------------------------------------------------------------

    public Boolean reserviere(Integer zugnummer, Boolean bRichtung, Integer n)
    {
        boolean reservierung;

            // Fallunterscheidung, ob Ost oder West
        if(bRichtung == false)
        {
        	n = dieAbschnitte.length-1-n;   // Abschnitte von hinten nach vorne
        }


        synchronized(Reporting.report)
        {
            System.out.println("Versuche Abschnitt " + n + " fuer Zug " + zugnummer + " zu reservieren");
        }
        
        
        synchronized(reserv)
        {
            reservierung = dieAbschnitte[n].Reservierung;  // ermittle die Reservierung
        
            if(reservierung == false)  // kein Zug auf dem Abschnitt
            {
        	    dieAbschnitte[n].Reservierung = true;
            }
        }
        
        synchronized(Reporting.report)
        {
            if(reservierung == false)  // kein Zug auf dem Abschnitt
            {
                System.out.println("Abschnitt " + n + " wird fuer Zug " + zugnummer + " reserviert");
       	        zeigeStreckenabschnitt();
       	        return true;
            }
            else
            {
        	    System.out.println("Abschnitt "+ n + " für Zug " + zugnummer + " besetzt");
        	    return false;
            }        	
        }

    }

    // in dieser Methode werden die Züge koordiniert, die nach Westen fahren
        
    // diese Methode wird von den Zügen aufgerufen, die diesen Streckenabschnitt verlassen
    public void leaveAB(int zugnummer)
    {
		System.out.println(zugnummer + " verlaeßt Streckenabschnitt");
    	
    }
    
//    public synchronized void zeigeStreckenabschnitt()

// ----------------------------------------------------------------------------------------------


    public Integer getAbschnitt(Boolean bRichtung, Integer n)
    {
        if(bRichtung == true)
        {
        	return n;   
        }
        else
        {
        	return dieAbschnitte.length-1-n;   
        }
    	
    }

// ----------------------------------------------------------------------------------------------


    public Integer getAnzahlZuege()
    {
    	synchronized(beleg)
    	{
            int counter = 0;
            for(int i=0; i<dieAbschnitte.length; i++)
            {
        	    if (dieAbschnitte[i].Belegung != 0) counter += 1;   
            }
        
            return counter;
    	}
    }

// ----------------------------------------------------------------------------------------------

    public void zeigeStreckenabschnitt()
    {

        // Die ganze Methode wird synchronisiert mit allen anderen print-Befehlen.
        // Die Synchronisation stützt sich hier auf beide Ausgabezeilen, so dass diese
        // stets untereinander stehen.
        
        synchronized(Reporting.report)
        {
        	synchronized(reserv)
        	{
        	    synchronized(beleg)
        	    {
    	            for(int i=0; i< dieAbschnitte.length; i++)
    	            {
    		             if(dieAbschnitte[i].Reservierung == true)
    		             {
    		                 System.out.print("r|");
    		             }
    		             else
    		             {
    		                 System.out.print(" |");
    		             }    		
    	            }    	
		            System.out.println("");
    	            for(int i=0; i< dieAbschnitte.length; i++)
    	            {
    		            System.out.print(dieAbschnitte[i].Belegung + "|");
    		 
    	            }
    	
   		            System.out.println("");
                }
            }
        }
    	
    }
    
// ----------------------------------------------------------------------------------------------
    
    public Integer getStreckenLaenge()
    {
    	return dieAbschnitte.length;
    }
    
// ----------------------------------------------------------------------------------------------
   
    public void freigeben(Integer zugnummer, Boolean bRichtung, Integer n)
    {
    	synchronized(reserv)
    	{
            if(bRichtung == false)
            {
        	    n = dieAbschnitte.length-1-n;   // Abschnitte von hinten nach vorne
            }

    	    dieAbschnitte[n].Reservierung = false;
    	
    	}
        System.out.println("Abschnitt " + n + " wird freigegeben");
        zeigeStreckenabschnitt();
    }


// ----------------------------------------------------------------------------------------------
   
    public void wechselnVon(Integer zugnummer, Boolean bRichtung, Integer from)
    {
        
        synchronized(beleg)
        {
            if(bRichtung == false)
            {
            	System.out.println(""+ dieAbschnitte.length + " " + from);
        	    from = dieAbschnitte.length-1-from;   // Abschnitte von hinten nach vorne
            }

    	    dieAbschnitte[from].Belegung = 0;
    	
            System.out.println("Zug verlaesst Abschnitt" + from);
        }
    };


// ----------------------------------------------------------------------------------------------
   
    public void wechselnNach(Integer zugnummer, Boolean bRichtung, Integer to)
    {
        synchronized(beleg)
        {
            if(bRichtung == false)
            {   
        	    to = dieAbschnitte.length-1-to;     // Abschnitte von hinten nach vorne
            }

    	    dieAbschnitte[to].Belegung = zugnummer;
    	
            System.out.println("Zug wechselt befaehrt jetzt " + to);
        }
       	    zeigeStreckenabschnitt();
       	
    };


// ----------------------------------------------------------------------------------------------
   
    public void ankommen(Integer zugnummer, Boolean bRichtung)
    {
        synchronized(beleg)
        {
            int first = 0;
            if(bRichtung == false)
            {
        	    first = dieAbschnitte.length-1-first;   // Abschnitte von hinten nach vorne
            }

        	dieAbschnitte[first].Belegung = zugnummer;
    	
            System.out.println("Zug kommt nach " + first);
        }
    	    zeigeStreckenabschnitt();
    };

// ----------------------------------------------------------------------------------------------
   
    public void verlassen(Integer zugnummer, Boolean bRichtung)
    {
    	synchronized(beleg)
    	{
            int last = 0;
            if(bRichtung == true)
            {
        	    last = dieAbschnitte.length-1-last;   // Abschnitte von hinten nach vorne
            }

    	    dieAbschnitte[last].Belegung = 0;
    	
            System.out.println("Zug verläßt " + last);
    	}
    	zeigeStreckenabschnitt();
    };
}


class SyncReservierung
{
	// Diese Klasse dient nur dazu, die Reservierungen zu synchroniseren.
}

class SyncBelegung
{
	// Diese Klasse dient nur dazu, die Belegungen zu synchroniseren.
}

