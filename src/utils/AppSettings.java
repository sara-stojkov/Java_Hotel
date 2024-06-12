package utils;

public class AppSettings {
	
	private String korisniciFileName, sobeFileName, tipSobeFileName, rezervacijeFileName, dodatneUslugeFileName, cenovniciFileName, ciscenjeLogFileName, dnevniDolasciFileName;

	public AppSettings(String korisniciFileName, String sobeFileName, String tipSobeFileName,
			String rezervacijeFileName, String dodatneUslugeFileName, String cenovniciFileName,
			String ciscenjeLogFileName, String dnevniDolasciFileName) {
		super();
		this.korisniciFileName = korisniciFileName;
		this.sobeFileName = sobeFileName;
		this.tipSobeFileName = tipSobeFileName;
		this.rezervacijeFileName = rezervacijeFileName;
		this.dodatneUslugeFileName = dodatneUslugeFileName;
		this.cenovniciFileName = cenovniciFileName;
		this.ciscenjeLogFileName = ciscenjeLogFileName;
		this.dnevniDolasciFileName = dnevniDolasciFileName;
	}

	public String getKorisniciFileName() {
		return korisniciFileName;
	}

	public void setKorisniciFileName(String korisniciFileName) {
		this.korisniciFileName = korisniciFileName;
	}

	public String getSobeFileName() {
		return sobeFileName;
	}

	public void setSobeFileName(String sobeFileName) {
		this.sobeFileName = sobeFileName;
	}

	public String getTipSobeFileName() {
		return tipSobeFileName;
	}

	public void setTipSobeFileName(String tipSobeFileName) {
		this.tipSobeFileName = tipSobeFileName;
	}

	public String getRezervacijeFileName() {
		return rezervacijeFileName;
	}

	public void setRezervacijeFileName(String rezervacijeFileName) {
		this.rezervacijeFileName = rezervacijeFileName;
	}

	public String getDodatneUslugeFileName() {
		return dodatneUslugeFileName;
	}

	public void setDodatneUslugeFileName(String dodatneUslugeFileName) {
		this.dodatneUslugeFileName = dodatneUslugeFileName;
	}

	public String getCenovniciFileName() {
		return cenovniciFileName;
	}

	public void setCenovniciFileName(String cenovniciFileName) {
		this.cenovniciFileName = cenovniciFileName;
	}

	public String getCiscenjeLogFileName() {
		return ciscenjeLogFileName;
	}

	public void setCiscenjeLogFileName(String ciscenjeLogFileName) {
		this.ciscenjeLogFileName = ciscenjeLogFileName;
	}

	public String getDnevniDolasciFileName() {
		return dnevniDolasciFileName;
	}

	public void setDnevniDolasciFileName(String dnevniDolasciFileName) {
		this.dnevniDolasciFileName = dnevniDolasciFileName;
	}
	
	
	
	
}
