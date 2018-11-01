package tsp;

public class Cycle_Fourmi {
	
	private Double[][] Pheromone_trajet_entre_deux_villes; // matrice donnant la quantité de phéromone entre la ville i et j
														   //implémenté à la fin de chaque cycle
	
	private Instance instance; // car on a besoin du nombre de villes
	
	public Cycle_Fourmi(Instance instance) {
		this.instance=instance;
		this.Pheromone_trajet_entre_deux_villes = new Double[this.instance.getNbCities()][this.instance.getNbCities()];
		for(int i=0;i<this.instance.getNbCities();i++) { // on initialise la matrice avec que des zéros (aucune phéromone)
			for(int j=0; j<this.instance.getNbCities();j++) {
				this.Pheromone_trajet_entre_deux_villes[i][j]=0.0;
			}
		}
	}
	
	public void Ajout_Pheromones(Fourmi fourmi, Double Q) throws Exception{ // rajoutte à la matrice les phéromones déposées par les fourmis
																			// Q = paramètre influençant la quantité de phéromone déposée
		for(int i=1;i<this.instance.getNbCities();i++) {
			this.Pheromone_trajet_entre_deux_villes[fourmi.getTrajet_fourmi().getCity(i)][fourmi.getTrajet_fourmi().getCity(i-1)]+=fourmi.depot_de_pheromone(Q);
			this.Pheromone_trajet_entre_deux_villes[fourmi.getTrajet_fourmi().getCity(i-1)][fourmi.getTrajet_fourmi().getCity(i)]+=fourmi.depot_de_pheromone(Q);
			//matrice symétrique, il faut donc remplir de la même façon les cases [i][i-1] et [i-1][i]
		}
	}
	
	
	public void Evaporation_Pheromones(Double rho) { // enleve les phéromones évaporées (rho = coeff d'évaporation)(0<rho<1)
		for(int i=0;i<this.instance.getNbCities();i++) { 
			for(int j=0; j<this.instance.getNbCities();j++) { // parcourt de l'ensemble de la matrice
				this.Pheromone_trajet_entre_deux_villes[i][j] = (1-rho)*this.Pheromone_trajet_entre_deux_villes[i][j];
			}
		}
	}
}
