package zugsimulator;



public class Client 
{

    public static void main(String[] args)
    {
	    ClientStub meinStub = new ClientStub();
	    boolean bErfolg;
	    int     anzAbschnitte;
	    int     nrAbschnitt;
	    
	    // Die nachstehenden Methoden haben jeweils die Parameter
	    // Zugnummer, Richtung, Streckenabschnitt
	    bErfolg = meinStub.reserviere(1, false, 11); 
	    bErfolg = meinStub.reserviere(2, false, 21);
	    int i = meinStub.getAbschnitt(true, 9);
	    int j = meinStub.getAbschnitt(false, 9);
		meinStub.wechselnVon(3, true, 31);
		meinStub.wechselnNach(4, true, 41);
		meinStub.freigeben(5, false, 51);
	    
/*
		       
	    // Die nachstehende Methode hat die Parameter
	    // Zugnummer, Richtung 
		bErfolg =  meinStub.verlassen(6, false);

	    // Die nachstehende Methode hat die Parameter
	    // Zugnummer, Richtung 
	    anzAbschnitte  = meinStub.getStreckenLaenge();
		nrAbschnitt    = meinStub.getAbschnitt(false,61);
*/
    }


}
