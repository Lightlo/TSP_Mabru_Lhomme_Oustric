package tsp;

public class Ant {
	
	//Définition des constantes pour l'Ant Colony Optimization Heuristic
	/** 0<=ALPHA : Contrôle l'intensité d'une arête */
	public static final double ALPHA = 1;
	/** 1<=BETA : Contrôle la visibilité d'une arête */
	public static final double BETA = 2;
	/** 0<GAMMA<1 : Probabilité non nulle d'exploration des villes non visitées*/
	public static final double GAMMA = 0.000000005; 
	
	//ATTRIBUTS
	/** 
	 * Objet Solution construit à partir du parcours de la fourmi
	 * path_ant = null à la construction, la méthode #{@link processAnt()} permet d'avoir : path_ant = SolutionIntelligible
	 */
	private Solution path_ant = null;
	/**
	 * Ce tableau de booléens indique les villes déjà visitées ou non par la fourmi
	 * #visited[i] est le boolean vrai si la ville a été visitée, faux sinon
	 */
	private boolean[] visited;
	/**
	 * Objet AntColonyOptimization dont dépend la fourmi 
	 * (contenant les donnnées du problème et les niveaux de phéromones) */
	private AntColonyOptimization aco;
	
	/** Ville d'origine du parcours de la fourmi */
	private int originCity = 0; //On fixe la ville de départ/d'arrivée à 0
	
	/** Nombre de villes du problème considéré (static pour la lisibilité du code) */
	public static int NbCities;
	
	//CONSTRUCTOR
	/**
	 * Crée un object de la classe Ant (une nouvelle fourmi) pour l'objet aco AntColonyOptimization
	 * Attention : la Solution path_ant de la fourmi n'est pas intelligible à sa construction
	 * Pour rendre la solution intelligible, il faut appliqué à la foumi la méthode #{@link processAnt()}
	 * @param aco L'objet AntColonyOptimisation dont dépend la fourmi
	 */
	public Ant(AntColonyOptimization aco) {
		this.aco = aco;
		NbCities = this.aco.getInstance().getNbCities();
		this.visited = new boolean[NbCities];
	}
	
	//GETTERS
	
	/**
	 * Note : path_ant = null à la construction, la méthode #{@link processAnt()} permet d'avoir : path_ant = SolutionIntelligible
	 * @return l'objet Solution construit à partir du parcours de la fourmi
	 */
	public Solution getPath_ant() {
		return this.path_ant;
	}
	/**
	 * @return le tableau de booléens indiquant les villes déjà visitées ou non par la fourmi
	 */
	public boolean[] getVisitedCities() {
		return this.visited;
	}
	/**
	 * @return la ville d'origine du parcours de la fourmi
	 */
	public int getOriginCity() {
		return this.originCity;
	}
	/**
	 * @return l'objet AntColonyOptimization dont dépend la fourmi (contenant les donnnées du problème et les niveaux de phéromones)
	 */
	public AntColonyOptimization getACO() {
		return this.aco;
	}
	
	//METHODS
	
	/**
	 * Détermine en fonction des phéromones sur les arêtes des villes, le parcours de la fourmi
	 * La solution path_ant est alors bien la solution intelligible d'une fourmi qui vient d'être construite
	 * Note : Cette méthode doit être appelée une seule fois sur une fourmi Ant tout juste construite
	 * @throws Exception Voir les exceptions renvoyées par {@link #getJ()}
	 */
	public void processAnt() throws Exception {
		//Initialisation
		int[] path = new int [NbCities+1];
		long path_distance = (long)0.0;
		
		//Prise en compte de la ville d'origine et d'arrivée (0) (1ere et derniere position)
		this.initializeVisitedCities(this.getOriginCity());
		int NbVisitedCities = 1;
		path[0] = this.getOriginCity();
		path[NbCities] = this.getOriginCity();
		
		//Initialisation du couple de villes consecutives
		//On suppose que les problèmes contiennent au moins 2 villes donc j existe forcément
		int i = this.getOriginCity();
		int j = this.getJ(i);
		
		//Construction progressive du chemin path de la fourmi
		while (NbVisitedCities<NbCities) {
			path[NbVisitedCities] = j;
			path_distance += this.getACO().getInstance().getDistances(i, j);
			/** this.adjustPheromoneLevel(i,j,path_distance); */
			this.getVisitedCities()[j] = true;
			NbVisitedCities++;
			i = j;
			if(NbVisitedCities<NbCities) {
				j = this.getJ(i);
			}
		}
		//Prise en compte Fermeture du chemin sur la ville de départ OriginCity
		path_distance += this.getACO().getInstance().getDistances(j, 0);
		
		//Via le set de la solution à l'instance path_ant, la solution est désormais intelligible
		this.path_ant = new Solution(this.getACO().getInstance(),path,path_distance);
	}
	
	/**
	 * Initialise à false toutes les villes visitées par la fourmi sauf sa ville d'origine originCity
	 * @param originCity la ville d'origine du parcours de la fourmi
	 */
	public void initializeVisitedCities(int originCity) {
		for (int i=0;i<NbCities;i++) {
			if (i==originCity) {
				this.getVisitedCities()[i] = true;
			} else {
				this.getVisitedCities()[i] = false;
			}
		}
	}
	
	/**
	 * Détermine la ville à visiter par la fourmi depuis la ville i
	 * de manière aléatoire mais influencée par les niveaux de phéromones sur les arêtes
	 * @param i la ville depuis laquelle la fourmi cherche une autre ville à rejoindre, une arête à emprunter
	 * @return la prochaine ville à visiter par la fourmi
	 * @throws Exception Voir les exceptions renvoyées par {@link #getNextCitiesProbabilites()}
	 */
	public int getJ(int i) throws Exception {
		double random = Math.random();
		double[] NextCitiesProbabilities = this.getNextCitiesProbabilities(i);
		int nextJ = 1; //Compteur puis valeur eponyme, Vaut 1, Car on a fixé OriginCity à 0
		boolean trouve = false;
		while (!trouve && nextJ<NbCities) { //Le 2eme test est toujours vérifié : Sécurité
			if (NextCitiesProbabilities[nextJ]>random) {
				trouve = true;
			}
			random -= NextCitiesProbabilities[nextJ];
			nextJ++;
		}
		return (nextJ-1);
	}
	
	/**
	 * A partir de la ville i, la méthode retourne les probabilités normalisées de rejoindre une autre ville x
	 * A la case x, on a la probabilité normalisée de la fourmi de rejoindre la ville x depuis la ville x, d'emprunter l'arête (i,x)
	 * @param i la ville depuis laquelle la fourmi cherche une autre ville à rejoindre, une arête à emprunter
	 * @return le tableau des probabilités normalisées pour déterminer la prochaine ville à visiter
	 * @throws Exception Voir les exceptions renvoyées par {@link #getNumeratorProba()}
	 */
	public double[] getNextCitiesProbabilities(int i) throws Exception {
		//Initialisation du tableau des probabilités pour aller la ville suivante à visiter
		double[] NextCitiesProbabilities = new double[NbCities];
		for (int j=0;j<NbCities;j++) {
			NextCitiesProbabilities[j] = 0.0;
		}
		//Construction du tableau des probabilités normalisées pour choisir la ville suivante à visiter
		double denominator = this.getDenominatorProba(i, NextCitiesProbabilities);
		for (int j=0;j<NbCities;j++) {
			NextCitiesProbabilities[j] = (this.getNumeratorProba(i, j) / denominator);
		}
		return NextCitiesProbabilities;
	}
	
	/**
	 * A partir de la ville i, la méthode retourne le dénominateur qui permet de normaliser les chances de visite
	 * Le quotient qui utilise ce dénominateur donne la probabilité à la fourmi d'aller vers une ville, d'emprunter une arête
	 * @param i la ville depuis laquelle la fourmi cherche une autre ville à rejoindre, une arête à emprunter
	 * @param NextCitiesProbabilities le tableau de probabilités de visite depuis la ville i
	 * @return le dénominateur pour la normalisation des chances de visite
	 * @throws Exception Voir les exceptions renvoyées par {@link #getNumeratorProba()}
	 */
	public double getDenominatorProba(int i, double[] NextCitiesProbabilities) throws Exception {
		double denominator = 0.0;
		for (int j=0;j<NbCities;j++) {
			if (!this.getVisitedCities()[j]) {
				if (i==j) {
					NextCitiesProbabilities[j]=0.0;
				} else {
					NextCitiesProbabilities[j]=this.getNumeratorProba(i, j);
				}
				denominator += NextCitiesProbabilities[j];
			}
		}
		return denominator;
	}
	
	/**
	 * A partir de la ville i, la méthode retourne le numérateur du quotient donnant la probabilité d'aller vers la ville j pour la fourmi
	 * @param i la ville depuis laquelle la fourmi cherche une autre ville à rejoindre, une arête à emprunter
	 * @param j la ville vers laquelle la fourmi peut envisager d'aller depuis la ville i
	 * @return le numérateur de la probabilité, la chance que la fourni aille à la ville j, emprunte l'arête (i,j)
	 * @throws Exception Voir les exceptions renvoyées par {@link #getDistance()} (classe Instance)
	 */
	public double getNumeratorProba (int i, int j) throws Exception {
		double PheromoneLevel = this.getACO().getPheromoneLevel(i, j);
		if (PheromoneLevel != 0.0 && !this.getVisitedCities()[j]) {
			return (GAMMA + Math.pow(PheromoneLevel,ALPHA) * Math.pow((1/this.getACO().getInstance().getDistances(i, j)), BETA));
			//return (Math.pow(PheromoneLevel,ALPHA) * Math.pow((1/this.getACO().getInstance().getDistances(i, j)), BETA));
		} else {
			return 0.0;
		}
	}
	
	//Méthode dans le cas d'une recherche sans cycle/itération
		/**
		public void adjustPheromoneLevel(int i, int j, long path_distance) {
			double oldPheromoneLevel = this.getACO().getPheromoneLevel(i,j);
			// Cycle/Itération de NB_ANTS fourmis
			double newPheromoneLevel = (oldPheromoneLevel + (Q/path_distance));
			//double newPheromoneLevel = ((1-RHO)*oldPheromoneLevel + (Q/path_distance));
			this.getACO().setPheromoneLevel(i, j, newPheromoneLevel);
		}
		*/
		
		
}
