package tsp;
import java.util.ArrayList;
public class Fourmi {
	
	private Solution Trajet_fourmi; // rempli dans l'ordre des villes par lesquelles est passée la fourmi
	private boolean visited;												 
	private int position;
	private Instance instance;
	private ArrayList<Integer> ville_non_visitees;
	
	 		
	public Fourmi(Instance instance) throws Exception{ //constructeur pour les fourmis 
		this.instance=instance;
		this.position=0;
		this.Trajet_fourmi = new Solution(instance);
		this.Trajet_fourmi.setCityPosition(0,0); // initialisation du trajet à la ville de départ soit la ville 0
		this.Trajet_fourmi.setCityPosition(0, this.instance.getNbCities()-1);//retour à la ville de départ 
		for(int i=0;i<this.instance.getNbCities()-2;i++) {//init donc toutes les villes sauf celle de départ
			this.ville_non_visitees.add(i+1);
		}
		/*for(int i=0;i<this.instance.getNbCities();i++) {
			this.Trajet_fourmi.add(0);
			this.Proba_ville.add(0.0);
		}*/ // initialisation vide utile ?
		
		
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
	
	public Double Proba_non_normalisee(Integer i, Integer j,Double alpha, Double beta, Double gamma, Double[][] Pheromone_trajet_entre_deux_villes) throws Exception{
		return (gamma+Math.pow(Pheromone_trajet_entre_deux_villes[i][j],alpha)/Math.pow(this.instance.getDistances(i, j),beta));
	}//renvoi la proba non normalisee (non divisée par la somme des proba des villes restantes)
	
	public Integer Get_ville_suivante(Double alpha, Double beta, Double gamma, Double[][] Pheromone_trajet_entre_deux_villes) throws Exception{
		double somme_proba = 0;
		for(int index_ville_suivante=0;index_ville_suivante<this.ville_non_visitees.size();index_ville_suivante++) {
			somme_proba+=Proba_non_normalisee(this.position,this.ville_non_visitees.get(index_ville_suivante),alpha,beta,gamma,Pheromone_trajet_entre_deux_villes);
		} // calcul de la somme des probas pour aller aux villes restantes ce qui nous servira à normaliser
		double nb_hasard = Math.random(); //nombre au hasard entre 0 et 1
		ArrayList<Double> Intervalle_proba = new ArrayList<Double>(); // contiendra les proba_normalisées (entre 0 et 1) et permettra de déterminer dans quel intervalle nb_hasard se trouvera
		Intervalle_proba.add(0.0);
		for(int i=0; i<this.ville_non_visitees.size();i++) {
			Intervalle_proba.add(Proba_non_normalisee(this.position,this.ville_non_visitees.get(i),alpha,beta,gamma,Pheromone_trajet_entre_deux_villes)/somme_proba);
		}
		boolean ville_retenue = false; // boolean servant à ne plus rien faire dans la boucle for une fois l'intervalle trouvé
		int ville_suivante=0; // initialisation de la variable servant à stocker la ville vers laquelle la fourmi ira
		for(int i=0; i<Intervalle_proba.size();i++) {
			if(ville_retenue == false && nb_hasard<Intervalle_proba.get(i)) {
				ville_retenue = true; // dès que nb_hasard appartient à une "tranche"
				ville_suivante = this.ville_non_visitees.get(i); // on note le numéro de la ville à laquelle cette tranche correspond 
									// cette ville sera celle vers laquelle la fourmi ira
			}
		}
		return ville_suivante;
		
	}
	
	public static void main(String[] args) {
		
	}
	
	
}
