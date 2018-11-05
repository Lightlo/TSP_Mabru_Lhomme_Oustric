package tsp;

public class AntColonyOptimization {
	
	//Définition des constantes pour l'Ant Colony Optimization Heuristic
	/** Nombre de fourmis envoyées par cycle/itération */
	public static final int NB_ANTS= 50;
	/** 0<RHO<1 : taux d'évaporation des phéromones */
	public static final double RHO = 0.5;
	
	//ATTRIBUTS
	/** Données du problème associé */
	private Instance instance;
	/**
	 * Cette matrice contient les niveaux de phéromones de chaque trajet d'une ville à une autre.
	 * Cette matrice est symétrique.
	 * #pheromoneLevels_between_cities[i][j] est le niveau de phéromones du trajet de la ville i à la ville j
	 */
	private double[][] pheromoneLevels_between_cities;
	
	//CONSTRUCTOR
	/**
	 * Crée un object de la classe AntColonyOptimization pour le problème d'instance Instance
	 * @param instance L'instance du problème
	 */
	public AntColonyOptimization(Instance instance) {
		this.instance = instance;
		
		//Initialisation des niveaux de phéromones pour aller d'une ville à une autre
		int NbCities = this.instance.getNbCities();
		this.pheromoneLevels_between_cities = new double[NbCities][NbCities];
		double level_pheromone;
		for (int i=0;i<NbCities;i++) {
			for (int j=0;j<NbCities;j++) {
				//level_pheromone = 0.1;
				level_pheromone = Math.random();
				this.pheromoneLevels_between_cities[i][j] = level_pheromone;
			}
		}
	}
	
	//GETTERS - SETTERS
	
	/**
	 * @return la matrice des niveaux de phéromones des trajets d'une ville à une autre
	 */
	public double[][] getPheromoneLevels() {
		return this.pheromoneLevels_between_cities;
	}
	
	/**
	 * @param city_i la ville i
	 * @param city_j la ville j
	 * @return le niveau de phéromones du trajet de la ville i à la ville j
	 */
	public double getPheromoneLevel(int city_i, int city_j) {
		return this.pheromoneLevels_between_cities[city_i][city_j];
	}
	
	/**
	 * @return l'instance du problème considéré
	 */
	public Instance getInstance(){
		return this.instance;
	}
	
	/**
	 * Set le niveau de phéromones du trajet/de l'arête de la ville i à la ville j
	 *
	 * @param city_i ville i
	 * @param city_j ville j
	 * @param newPheromoneLevel le nouveau niveau de phéromones du trajet/de l'arête considéré
	 */
	public void setPheromoneLevel(int city_i, int city_j, double newPheromoneLevel) {
		this.pheromoneLevels_between_cities[city_i][city_j] = newPheromoneLevel;
	}
	
	//METHODS
	
	/**
	 * Ajoute la somme des dépôts de phéromones de toutes les fourmis à la fin d'un cycle sur les arêtes
	 *
	 * @param ant_cycle l'objet contenant les dépôts totaux déposés par les fourmis après un cycle
	 */
	public void addPheromoneDeposits_EndCycle(AntCycle ant_cycle) {
		for(int i=0;i<this.getInstance().getNbCities();i++) { 
			for(int j=0; j<this.getInstance().getNbCities();j++) { // parcours de l'ensemble de la matrice
				this.setPheromoneLevel(i, j, this.getPheromoneLevel(i,j)+ ant_cycle.getPheromoneDeposit(i, j));
			}
		}
	}
	
	/**
	 * Set le niveau de phéromones sur toutes les arêtes à la fin d'un nouveau cycle
	 * en ajoutant la somme des dépôts de phéromones de toutes les fourmis à la fin d'un cycle sur les arêtes
	 * et en évaporant le niveau de phéromones sur toutes les arêtes du cycle précédent
	 *
	 * @param ant_cycle l'objet contenant les dépôts totaux déposés par les fourmis après un cycle
	 */
	public void evaporationPlusAddDeposits_EndCycle(AntCycle ant_cycle) {
		for(int i=0;i<this.getInstance().getNbCities();i++) { 
			for(int j=0; j<this.getInstance().getNbCities();j++) { // parcours de l'ensemble de la matrice
				this.setPheromoneLevel(i, j, (1-RHO)*this.getPheromoneLevel(i,j) + ant_cycle.getPheromoneDeposit(i, j));
			}
		}
	}
}
