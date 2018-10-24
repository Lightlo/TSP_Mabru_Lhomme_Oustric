package tsp;
import java.util.ArrayList;
public class Fourmi {
	
	private ArrayList<Integer> Trajet_fourmi; // rempli dans l'ordre des villes par lesquelles est passée la fourmi
	private Double[][] Proba_trajet_entre_deux_villes; // matrice donnant la proba d'emprunter le chemin entre la ville i et la ville j
													  // rempli/implémenté à la fin de chaque trajet de chaque fourmi
	private int position;
	private Instance instance;
	
	public Fourmi(Instance instance) {
		this.instance=instance;
		this.position=0;
		this.Trajet_fourmi.add(0); // initialisation du trajet à la ville de départ soit la ville 0
		/*for(int i=0;i<this.instance.getNbCities();i++) {
			this.Trajet_fourmi.add(0);
			this.Proba_ville.add(0.0);
		}*/ // initialisation vide utile ?
		
	}
	
	public void etape_trajet_fourmi(int ville_i) { //chemin entre la dernière ville visitée et la ville suivante (ici la ville i)
		this.Trajet_fourmi.add(ville_i); // ajoute la ville i au trajet 
		this.position = ville_i;  // mise à jour de la position de la fourmi
		
		// !! vérifier que ville_i non visitée
	}
	
	public Integer getPosition() {
		return this.position;
	}
	
	/*public void setPosition(int position) {
		this.position = position;
	}*/
	
	public ArrayList<Integer> getTrajet_fourmi(){
		return this.getTrajet_fourmi();
	}
	
	public Double getProba_chemin(int ville_i, int ville_j) {           //proba que la fourmi emprunte le chemin pour aller 
																		//vers ville_j en étant à ville_i (si ville_j non visitée)
		return this.Proba_trajet_entre_deux_villes[ville_i][ville_j];
	}
}
