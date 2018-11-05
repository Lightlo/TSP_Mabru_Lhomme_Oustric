package tsp;

public class AntCycle {
	
	//Définition des constantes des calculs de l'ACO
	public static final double Q = 0.005; // 0<Q<1 : Paramètre de réglage
		
	//ATTRIBUTS
	/**
	 * Ce tableau contient la somme des dépôts de phéromones de chaque trajet d'une ville à une autre des fourmis pendant un cycle
	 * #pheromoneDe_between_cities[i][j] est le dépôt de phéromones des fourmis pendant un cycle pour le trajet de la ville i à la ville j
	 */
	private double[][] pheromoneDeposit_between_cities;
	
	//CONSTRUCTOR	
	/**
	 * Crée un object de la classe AntCycle
	 * Son attribut pheromoneDeposit_between_cities stocke pendant un cycle/itérations 
	 * tous les dépôts des fourmis de ce cycle/itération
	 * @param NbCities le nombre de villes du problème considéré
	 */
	public AntCycle(int NbCities) {
		//Initialisation des dépôts (nuls) de phéromones au début du cycle
		this.pheromoneDeposit_between_cities = new double[NbCities][NbCities];
		for (int i=0;i<NbCities;i++) {
			for (int j=0;j<NbCities;j++) {
				this.pheromoneDeposit_between_cities[i][j] = 0.0;
			}
		}
	}
	
	//GETTERS - SETTERS
	
	/**
	 * @return la matrice des dépôts de phéromones des trajets d'une ville à une autre après un cycle/itération
	 */
	public double[][] getPheromoneDeposits() {
		return this.pheromoneDeposit_between_cities;
	}
	
	/**
	 * @return le nombre de villes du problème considéré
	 */
	public int getNbCities() {
		return this.pheromoneDeposit_between_cities.length;
	}
	
	/**
	 * @param city_i la ville i
	 * @param city_j la ville j
	 * @return le dépôt total de phéromones du trajet de la ville i à la ville j après un cycle/itération
	 */
	public double getPheromoneDeposit(int city_i, int city_j) {
		return this.pheromoneDeposit_between_cities[city_i][city_j];
	}
	
	/**
	 * Set le dépôt de phéromones du trajet de la ville i à la ville j
	 *
	 * @param city_i ville i
	 * @param city_j ville j
	 * @param newPheromoneDeposit le nouveau dépôt de phéromones du trajet considéré
	 */
	public void setPheromoneDeposit(int city_i, int city_j, double newPheromoneDeposit) {
		this.pheromoneDeposit_between_cities[city_i][city_j] = newPheromoneDeposit;
	}
	
	/**
	 * Ajoute le dépôt de phéromones PheromoneDeposit d'une fourmi à l'ensemble des dépôts
	 * relatifs au trajet de la ville i à la ville j
	 *
	 * @param city_i ville i
	 * @param city_j ville j
	 * @param PheromoneDeposit le dépôt de phéromones d'une fourmi pour le trajet considéré
	 */
	public void addPheromoneDeposit(int city_i, int city_j, double PheromoneDeposit) {
		this.pheromoneDeposit_between_cities[city_i][city_j] += PheromoneDeposit;
	}
	
	//METHODS
	
	/**
	 * Ajoute les dépôts de phéromones d'une fourmi sur les arêtes de son parcours
	 *
	 * @param ant_path les données du trajet de la fourmi
	 * @param NbCities le nombre de villes du problème considéré
	 */
	public void stockPheromoneDepositAnt(Solution ant_path) throws Exception {
		double path_distance = ant_path.getObjectiveValue();
		for (int i=0;i<this.getNbCities();i++) {
			this.addPheromoneDeposit(ant_path.getCity(i), ant_path.getCity(i+1), (Q/path_distance));
			this.addPheromoneDeposit(ant_path.getCity(i+1), ant_path.getCity(i), (Q/path_distance));
		}
	}
	
}
