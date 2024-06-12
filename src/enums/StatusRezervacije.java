package enums;

public enum StatusRezervacije {

	NA_ČEKANjU(1), POTVRĐENA(2), ODBIJENA(3), OTKAZANA(4), U_TOKU(5), PROŠLA(6), OBRISANA(7);
		 
	int status;
	
	private StatusRezervacije(int i) {this.status = i;}
	
	private String [] opis = {"NA_ČEKANjU", "POTVRĐENA", "ODBIJENA", "OTKAZANA", "U_TOKU", "PROŠLA", "OBRISANA"};
	
	@Override
	public String toString() {
		return opis[this.ordinal()];
	}
	
	public static StatusRezervacije fromInteger(int x) {
		switch (x){
			
		case 1:
            return NA_ČEKANjU;
        case 2:
            return POTVRĐENA;
        case 3:
        	return ODBIJENA;
        case 4:
        	return OTKAZANA;
        case 5:
        	return U_TOKU;
        case 6:
        	return PROŠLA;
        case 7:
        	return OBRISANA;
        }
        return null;
			
	}
	
	
}
