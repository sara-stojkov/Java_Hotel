package enums;

public enum StatusSobe {

	SLOBODNA(1), ZAUZETO(2), SPREMANjE(3), OBRISANA(4);
	
	int status;
	
	private StatusSobe(int i) { this.status = i;}
	
	private String [] opis = {"SLOBODNA", "ZAUZETO", "SPREMANjE", "OBRISANA"};
	
	@Override
	public String toString() {
		return opis[this.ordinal()];
		
	}
	
	public static StatusSobe fromInteger(int i) {
		switch (i) {
		
		case 1:
			return SLOBODNA;
		case 2:
			return ZAUZETO;
		case 3:
			return SPREMANjE;
		case 4:
			return OBRISANA;
		}
		return null;
	}
	
	
}
