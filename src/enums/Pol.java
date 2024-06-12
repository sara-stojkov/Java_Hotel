package enums;

public enum Pol {

	M(1), Z(2);
	
		int pol;
		
		private Pol() {}
		
		private Pol(int i) {this.pol = i;}
	
		private String [] opis = {"musko", "zensko"};
	
		@Override
		public String toString() {
			return opis[this.ordinal()];
		}
		
	    public static Pol fromString(String x) {
	        switch(x) {
	        
	        case "1":
	            return M;
	        case "2":
	            return Z;
	        }
	        return null;
	    }

}
