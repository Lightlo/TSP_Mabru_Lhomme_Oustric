package tsp;

public class AntColonyOptimization {
	
	public static final int NB_ANTS=500;
	
	//ATTRIBUTS
	/** Données du problème associé */
	private Instance instance;
	/**
	 * Ce tableau contient les niveaux de phéromones de chaque trajet d'une ville à une autre.<br>
	 * #pheromoneLevels_between_cities[i][j] est le niveau de phéromones du trajet de la ville i à la ville j
	 */
	private double[][] pheromoneLevels_between_cities;
	
	//CONSTRUCTOR
	/**
	 * Crée un object de la classe AntColonyOptimization pour le problème d'instance Instance
	 * @param instance L'instance du problème
	 */
	public AntColonyOptimization(Instance instance) throws Exception {
		this.instance = instance;
		
		//Initialisation des niveaux de phéromones pour aller d'une ville à une autre
		int NbCities = this.instance.getNbCities();
		this.pheromoneLevels_between_cities = new double[NbCities][NbCities];
		double level_pheromone;
		for (int i=0;i<NbCities;i++) {
			for (int j=0;j<NbCities;j++) {
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
	 * Set le niveau de phéromones du trajet de la ville i à la ville jcity indexCity at position position in the solution.<br>
	 *
	 * @param city_i ville i
	 * @param city_j ville j
	 * @param newPheromoneLevel le nouveau niveau de phéromones du trajet considéré
	 */
	public void setPheromoneLevel(int city_i, int city_j, double newPheromoneLevel) {
		this.pheromoneLevels_between_cities[city_i][city_j] = newPheromoneLevel;
	}
	
	//METHODS
	
	public void evaporationPheromones_EndCycle (double evaporationRateRho) {
		for(int i=0;i<this.getInstance().getNbCities();i++) { 
			for(int j=0; j<this.getInstance().getNbCities();j++) { // parcourt de l'ensemble de la matrice
				this.setPheromoneLevel(i, j, (1-evaporationRateRho)*this.getPheromoneLevel(i,j));
			}
		}
	}

}
