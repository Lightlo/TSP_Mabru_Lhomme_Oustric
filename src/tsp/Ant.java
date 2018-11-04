package tsp;

import java.util.ArrayList;

public class Ant {
	
	//Définition des constantes des calculs de l'ACO
	public static final double Q = 0.005; // 0<Q<1 : ...
	public static final double RHO = 0.2; // 0<RHO<1 : taux d'évaporation des phéromones
	public static final double ALPHA = 0.01; // 0<=ALPHA : ...
	public static final double BETA = 9.5; // 1<=BETA : ...
	public static final double GAMMA = 0.1; // 0<GAMMA<1 : ...
	
	//ATTRIBUTS
	private Solution path_ant = null;
	private boolean[] visited; //La case x du tableau indique si la ville x a été visitée
	private AntColonyOptimization aco;
	private int originCity = 0; //On fixe la ville de départ/d'arrivée à 0
	public static int NbCities;
	
	//CONSTRUCTOR
	public Ant(AntColonyOptimization aco) {
		this.aco = aco;
		NbCities = this.aco.getInstance().getNbCities();
		this.visited = new boolean[NbCities];
	}
	
	//GETTERS
	public Solution getPath_ant() {
		return this.path_ant;
	}
	public boolean[] getVisitedCities() {
		return this.visited;
	}
	public int getOriginCity() {
		return this.originCity;
	}
	public AntColonyOptimization getACO() {
		return this.aco;
	}
	
	//METHODS
	
	public Solution processAnt() throws Exception {
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
		
		//Construction du chemin path de la fourmi
		while (NbVisitedCities<NbCities) {
			path[NbVisitedCities] = j;
			path_distance += this.getACO().getInstance().getDistances(i, j);
			this.adjustPheromoneLevel(i,j,path_distance);
			this.getVisitedCities()[j] = true;
			NbVisitedCities++;
			i = j;
			if(NbVisitedCities<NbCities) {
				j = this.getJ(i);
			}
		}
		//Prise en compte Fermeture du chemin sur la ville de départ OriginCity
		path_distance += this.getACO().getInstance().getDistances(j, 0);
		
		return new Solution(this.getACO().getInstance(),path,path_distance);
	}
	
	public void initializeVisitedCities(int originCity) {
		for (int i=0;i<NbCities;i++) {
			if (i==originCity) {
				this.getVisitedCities()[i] = true;
			} else {
				this.getVisitedCities()[i] = false;
			}
		}
	}
	public void adjustPheromoneLevel(int i, int j, long path_distance) {
		double oldPheromoneLevel = this.getACO().getPheromoneLevel(i,j);
		//!!!! Cycle de k fourmis ? ou une par une ?
		//double newPheromoneLevel = -oldPheromoneLevel + (Q/path_distance));
		double newPheromoneLevel = ((1-RHO)*oldPheromoneLevel + (Q/path_distance));
		//newPheromoneLevel est >0
		this.getACO().setPheromoneLevel(i, j, newPheromoneLevel);
	}
	//Determine Next City J à visiter
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
		return nextJ;
	}
	public double[] getNextCitiesProbabilities(int i) throws Exception {
		double[] NextCitiesProbabilities = new double[NbCities];
		double denominator = this.getDenominatorProba(i, NextCitiesProbabilities);
		for (int j=0;j<NbCities;j++) {
			NextCitiesProbabilities[j] = (this.getNumeratorProba(i, j) / denominator);
		}
		return NextCitiesProbabilities;
	}
	public double getDenominatorProba(int i, double[] NextCitiesProbabilities) throws Exception {
		double denominator = 0.0;
		for (int j=0;i<NbCities;j++) {
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
	public double getNumeratorProba (int i, int j) throws Exception {
		double PheromoneLevel = this.getACO().getPheromoneLevel(i, j);
		if (PheromoneLevel != 0.0 && !this.getVisitedCities()[j]) {
			//return (GAMMA + Math.pow(PheromoneLevel,ALPHA) * Math.pow((1/this.getACO().getInstance().getDistances(i, j)), BETA));
			return (Math.pow(PheromoneLevel,ALPHA) * Math.pow((1/this.getACO().getInstance().getDistances(i, j)), BETA));
		} else {
			return 0.0;
		}
	}

}
