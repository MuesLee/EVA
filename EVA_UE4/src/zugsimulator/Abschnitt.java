package zugsimulator;

class Abschnitt 
{
	public boolean Reservierung;
	public int     Belegung;

    public Abschnitt()
    {
    	Reservierung = false;    // der Abschnitt ist nicht reserviert
    	Belegung = 0;            // auf dem Abschnitt f�hrt zur Zeit kein Zug
    }
}