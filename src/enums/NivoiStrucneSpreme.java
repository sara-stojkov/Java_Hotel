package enums;

public enum NivoiStrucneSpreme {

	prvi(1), drugi(2), treci(3), cetvrti(4), peti(5), sesti(6), sedmi(7), osmi(8);
	
	int nivo;
	
	private NivoiStrucneSpreme() {}
	
	private NivoiStrucneSpreme(int i) {this.nivo = i;}

	public static String [] opis = {"prvi", "drugi", "treći", "četvrti", "peti", "šesti", "sedmi", "osmi"};

	@Override
	public String toString() {
		return opis[this.ordinal()];
	}
	
	public static NivoiStrucneSpreme fromInteger(int x) {
		switch (x){
			
		case 1:
            return prvi;
        case 2:
            return drugi;
        case 3:
        	return treci;
        case 4:
        	return cetvrti;
        case 5:
        	return peti;
        case 6:
        	return sesti;
        case 7:
        	return sedmi;
        case 8:
        	return osmi;
        }
        return null;
			
	}
}
