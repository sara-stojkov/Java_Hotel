package enums;

public enum Uloga {
	
	ADMINISTRATOR(0), RECEPCIONER(1), SOBARICA(2), GOST(3);
	
	int uloga;
	
	private Uloga(int i) {this.uloga = i;}
		
		private String [] opis = {"ADMINISTRATOR", "RECEPCIONER", "SOBARICA", "GOST"};
		
		@Override
		public String toString() {
			return opis[this.ordinal()];
		}
		
		public static Uloga fromInteger(int x) {
			switch (x){
				
			case 0:
	            return ADMINISTRATOR;
	        case 1:
	            return RECEPCIONER;
	        case 2:
	        	return SOBARICA;
	        case 3:
	        	return GOST;
	
	        }
	        return null;
				
		}

}
