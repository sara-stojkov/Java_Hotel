package managers;

import java.io.IOException;

import utils.AppSettings;

public class ManagerFactory {
	
	private AppSettings appSettings;
	private CenovnikManager cenovnikMng;
	private DodatneUslugeManager dodatneUslugeMng;
	private KorisnikManager korisniciMng;
	private RezervacijaManager rezervacijaMng;
	private SobaManager sobaMng;
	private TipSobeManager tipSobeMng;
	
	public ManagerFactory(AppSettings appSettings) {
		this.appSettings = appSettings;
		
		this.sobaMng = SobaManager.getInstance(this.appSettings.getSobeFileName(), this.appSettings.getCiscenjeLogFileName());
		try {
			this.sobaMng.sobeUListu();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.korisniciMng = KorisnikManager.getInstance(this.appSettings.getKorisniciFileName(), sobaMng);
		
		this.cenovnikMng = CenovnikManager.getInstance(this.appSettings.getCenovniciFileName());
		this.dodatneUslugeMng = DodatneUslugeManager.getInstance(this.appSettings.getDodatneUslugeFileName());
		this.rezervacijaMng = RezervacijaManager.getInstance(this.appSettings.getRezervacijeFileName(), sobaMng, this.appSettings.getDnevniDolasciFileName());
		this.tipSobeMng = TipSobeManager.getInstance(this.appSettings.getTipSobeFileName());
	}

	public KorisnikManager getKorisnikMng() {
		return korisniciMng;
	}

	public CenovnikManager getCenovnikMng() {
		return cenovnikMng;
	}

	public DodatneUslugeManager getDodatneUslugeMng() {
		return dodatneUslugeMng;
	}

	public RezervacijaManager getRezervacijaMng() {
		return rezervacijaMng;
	}

	public SobaManager getSobaMng() {
		return sobaMng;
	}

	public TipSobeManager getTipSobeMng() {
		return tipSobeMng;
	}

	public void ucitavanje() {
		try {
			this.korisniciMng.ucitajKorisnike(); 
			this.tipSobeMng.tipoviSobaUListu();
			this.rezervacijaMng.rezervacijeUListu(korisniciMng.getGosti(), tipSobeMng.getTipoviSoba());
			this.dodatneUslugeMng.uslugeUListu();
			this.cenovnikMng.cenovniciUListu();
		} catch (IOException e) {
			System.out.println("greskaaa pri ucitavanju");
		}
	}
}