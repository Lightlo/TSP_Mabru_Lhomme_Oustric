package tsp;
import java.util.ArrayList;
public class Fourmi {
	
	private Solution Trajet_fourmi; // rempli dans l'ordre des villes par lesquelles est passée la fourmi
	
	private ArrayList<Integer> ville_non_visitees;												 
	private int position;
	private Instance instance;
	
	
	public Fourmi(Instance instance) throws Exception{ //constructeur pour les fourmis 
		this.instance=instance;
		this.position=0;
		this.Trajet_fourmi.setCityPosition(0,0); // initialisation du trajet à la ville de départ soit la ville 0
		this.Trajet_fourmi.setCityPosition(0, this.instance.getNbCities()-1);//retour à la ville de départ 
		for(int i=0;i<this.instance.getNbCities()-2;i++) {//init donc toutes les villes sauf celle de départ
			this.ville_non_visitees.add(i+1);
		}
		/*for(int i=0;i<this.instance.getNbCities();i++) {
			this.Trajet_fourmi.add(0);
			this.Proba_ville.add(0.0);
		}*/ // initialisation vide utile ?
		//
		
	}
	
	public void etape_trajet_fourmi(int ville_i, int index_ville_actuelle) throws Exception{ //chemin entre la dernière ville visitée et la ville suivante (ici la ville i)
		this.Trajet_fourmi.setCityPosition(ville_i, index_ville_actuelle+1); // ajoute la ville i au trajet 
		this.position = ville_i;  // mise à jour de la position de la fourmi
		this.ville_non_visitees.remove(ville_i); // enlève la ville_i des villes non visitées
		
		// !! vérifier que ville_i non visitée et que index_ville_actuelle<this.instance.getNbCities()-2
	}
	
	public Integer getPosition() {
		return this.position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public Solution getTrajet_fourmi(){
		return this.Trajet_fourmi;
	}
	
	
	public Double depot_de_pheromone(double Q) throws Exception{
		this.Trajet_fourmi.evaluate();
		long objective_value_trajet=this.Trajet_fourmi.getObjectiveValue();
		return Q/objective_value_trajet;
		
	}
	
}
