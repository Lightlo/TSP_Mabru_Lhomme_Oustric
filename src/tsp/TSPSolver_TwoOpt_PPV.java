package tsp;
import java.util.ArrayList; 

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * 
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes: 
 * 	- #m_instance :  the Instance object which contains the problem data
 * 	- #m_solution : the Solution object to modify. This object will store the result of the program.
 * 	- #m_timeLimit : the maximum time limit (in seconds) given to the program.
 *  
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 * 
 */
public class TSPSolver_TwoOpt_PPV {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;

	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Creates an object of the class Solution for the problem data loaded in Instance
	 * @param instance the instance of the problem
	 * @param timeLimit the time limit in seconds
	 */
	public TSPSolver_TwoOpt_PPV(Instance instance, long timeLimit) {
		m_instance = instance;
		m_solution = new Solution(m_instance);
		m_timeLimit = timeLimit;
	}

	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/**
	 * **TODO** Modify this method to solve the problem.
	 * 
	 * Do not print text on the standard output (eg. using `System.out.print()` or `System.out.println()`).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (`System.err.print()` or `System.err.println()`).
	 * 
	 * When your algorithm terminates, make sure the attribute #m_solution in this class points to the solution you want to return.
	 * 
	 * You have to make sure that your algorithm does not take more time than the time limit #m_timeLimit.
	 * 
	 * @throws Exception may return some error, in particular if some vertices index are wrong.
	 */
	public void solve() throws Exception {
		
		m_solution.print(System.err);
		
		// Example of a time loop
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		
		int NbCities = this.getSolution().getInstance().getNbCities();
		 
	    //On initialise la solution à la solution du PPV    
	    this.m_solution = this.PPV();
	    //**
	    Solution new_solution;
	    Solution memory = this.m_solution;
	    
	    int k = 0;
		
		while (spentTime < m_timeLimit * 1000) {
			 
	        for (int i=1; i < NbCities-1; i++) {
	            for (int j= i+1; j<NbCities; j++) {
	                new_solution = this.m_solution.TwoOpt_Swap(i, j);
	                k++;
	                
	                if ( new_solution.getObjectiveValue() < this.m_solution.getObjectiveValue() ) {
	                	//Cas d'une amélioration de la longueur de la solution
	                    this.m_solution = new_solution;
	                    System.out.println("Iteration " +k+ " , distance : " + this.m_solution.getObjectiveValue());
	                    this.m_solution.evaluate();
	                    System.out.println("Iteration " +k+ " , distance : " + this.m_solution.getObjectiveValue());
	                }
	            }
	        }
	        //On regarde si le processus améliore encore la solution après une boucle
	        if (memory.getObjectiveValue()==this.m_solution.getObjectiveValue()) {
	        	spentTime = m_timeLimit * 1000; //On force la sortie de la time loop
	        } else {
	        	spentTime = System.currentTimeMillis() - startTime;
	        	memory = this.m_solution;
	        }
			
		}
		//*/
	}
	
	//-----------------------------
	//-----FONCTIONS ANNEXES-------
	//-----------------------------
	
	//Détermination Plus Proche Voisin
	public Solution PPV() throws Exception {
		//Initialisation des villes non visitées sans la ville d'origine (0)
		int NbCities = this.getSolution().getInstance().getNbCities();
		ArrayList<Integer> UnvisitedCities = initializeUnvisitedCities(NbCities);
		
		//Prise en compte de la ville de départ/d'arrivée (0)
		this.getSolution().setCityPosition(0, 0);
		int NbVisitedCities = 1; //Compteur du nb de villes dans la solution
		
		//Recherche du plus proche voisin (PPV) à chaque itération jusqu'à ce que toutes les villes soient visitées
		while (NbVisitedCities < NbCities-1) {
			UnvisitedCities = NextUnvisitedCitiesViaPPV(NbVisitedCities,UnvisitedCities);
			NbVisitedCities ++;
		}
		
		//Ajout de la dernière ville non visitée (dernier élément de l'ArrayList UnVisitedCities)
		this.getSolution().setCityPosition(UnvisitedCities.get(1), NbVisitedCities);
		//Ajout de la distance entre l'avant-dernière et la dernière ville visitées
		this.getSolution().setObjectiveValue(this.getSolution().getObjectiveValue()
				+ this.getSolution().getInstance()
					.getDistances( this.getSolution().getCity(NbCities-2), this.getSolution().getCity(NbCities-1)));
		//Ajout de la ville d'origine (0) en tant que ville d'arrivée dans la solution
		this.getSolution().setCityPosition(0,NbCities);
		//Ajout de la distance entre l'ultime ville visitée et la ville d'arrivée
		this.getSolution().setObjectiveValue(this.getSolution().getObjectiveValue()
				+ this.getSolution().getInstance()
					.getDistances( this.getSolution().getCity(NbCities-1), 0));
		
		//renvoie de la solution PPV
		return this.getSolution();
	}

	public ArrayList<Integer> initializeUnvisitedCities(int NbCities){
		ArrayList<Integer> UnvisitedCities = new ArrayList<Integer>();
		for(int i=0; i<NbCities;i++) {
			UnvisitedCities.add(i);
		}
		return UnvisitedCities;
	}

	public ArrayList<Integer> NextUnvisitedCitiesViaPPV(int NbVisitedCities, ArrayList<Integer> UnvisitedCities) throws Exception{
		//Initialisation de la position du PPV sachant que la position 0 est la dernière ville visitée
		int PPVposition = 1;
		//Initialisation de la distance au PPV depuis la dernière ville visitée
		long PPVdistance = this.getSolution().getInstance()
				.getDistances(UnvisitedCities.get(0), UnvisitedCities.get(PPVposition)), new_distance;
		
		//Recherche du PPV dans les villes non visitées
		for (int i=2;i<UnvisitedCities.size();i++) {
			new_distance = this.getSolution().getInstance()
					.getDistances(UnvisitedCities.get(0), UnvisitedCities.get(i));
			if(new_distance < PPVdistance) {
				PPVposition = i;
				PPVdistance = new_distance;
			}
		}
		
		//Mise à jour de la solution (contenu, objectiveValue) et de l'ArrayList des listes non visitées
		this.getSolution().setObjectiveValue(this.getSolution().getObjectiveValue() + PPVdistance);
		UnvisitedCities.set(0,UnvisitedCities.get(PPVposition)); //On met à la position 0 le dernier PPV visité
		UnvisitedCities.remove(PPVposition); //Suppression de la ville visitée de l'ArrayList UnvisitedCities
		this.getSolution().setCityPosition(UnvisitedCities.get(0), NbVisitedCities); //ajoute ville la plus proche a la solution
		
		return UnvisitedCities;
	}

	
	
	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/** @return the problem Solution */
	public Solution getSolution() {
		return m_solution;
	}

	/** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

	/** @return Time given to solve the problem */
	public long getTimeLimit() {
		return m_timeLimit;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one will be deleted).
	 * @param solution : new solution
	 */
	public void setSolution(Solution solution) {
		this.m_solution = solution;
	}

	/**
	 * Sets the problem data
	 * @param instance the Instance object which contains the data.
	 */
	public void setInstance(Instance instance) {
		this.m_instance = instance;
	}

	/**
	 * Sets the time limit (in seconds).
	 * @param time time given to solve the problem
	 */
	public void setTimeLimit(long time) {
		this.m_timeLimit = time;
	}

}
