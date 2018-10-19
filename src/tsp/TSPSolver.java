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
public class TSPSolver {

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
	public TSPSolver(Instance instance, long timeLimit) {
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
	public void solve() throws Exception
	{
		m_solution.print(System.err);
		
		// Example of a time loop
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		
		ArrayList<Integer> villes_non_visitees = Liste_Villes();
		int n = villes_non_visitees.size();
		this.m_solution.setCityPosition(villes_non_visitees.get(0), 0);//ajoute ville 0 à la 1ere place de la solution
		int nb_villes_placees = 1; //nb de ville dans solution
		do
		{
			villes_non_visitees=plus_proche_voisin(n,nb_villes_placees,villes_non_visitees);
			// int index_depart=0;
			
			nb_villes_placees ++;
			n--;
			
			spentTime = System.currentTimeMillis() - startTime;
		}while(spentTime < (m_timeLimit * 1000 - 100) && n>1);
		this.m_solution.setCityPosition(villes_non_visitees.get(0),this.m_instance.getNbCities()-1);
		
		//Local Search
		Solution candidat = this.m_solution.copy();
		long score_candidat = this.m_solution.getObjectiveValue();
		long score_solution = candidat.getObjectiveValue();
		do {
			for(int i=1;i<this.m_instance.getNbCities()-3;i++) {
				for(int j=i+1;j<this.m_instance.getNbCities()-2;j++) {
					Solution provisoire = this.m_solution.copy();
					provisoire.swap(i,j);
					long score_provisoire =provisoire.getObjectiveValue();
					if(score_provisoire<score_candidat) {
						score_candidat=score_provisoire;
						candidat=provisoire;
					}
				}
			}
			if(score_solution>score_candidat) {
				score_solution=score_candidat;
				this.m_solution=candidat;
			}
			
		} while (score_solution>score_candidat);	
	}
	
	//-----------------------------
	//-----FONCTIONS ANNEXES-------
	//-----------------------------

	
	public ArrayList<Integer> Liste_Villes(){
		ArrayList<Integer> villes = new ArrayList<Integer>();
		for(int i=0; i<this.m_instance.getNbCities();i++) {
			villes.add(i);
		}
		return villes;
	}

	public ArrayList<Integer> plus_proche_voisin(int n, int t, ArrayList<Integer> villes_non_visitees) throws Exception{
		int index_plusproche = 1;
		long dist_min = this.m_instance.getDistances(villes_non_visitees.get(0), villes_non_visitees.get(1));//init longueur min
		for(int i=2;i<n;i++) { // cherche ville la plus proche dans le tableau
			if(this.m_instance.getDistances(villes_non_visitees.get(0), villes_non_visitees.get(i))<dist_min) {
				index_plusproche = i;
				dist_min=this.m_instance.getDistances(villes_non_visitees.get(0), villes_non_visitees.get(index_plusproche));
			}
		}
		this.m_solution.setObjectiveValue(this.m_solution.getObjectiveValue()+dist_min); //incrémentation de la longueur tot
		villes_non_visitees.set(0,villes_non_visitees.get(index_plusproche)); //on met à l'indice 0 la prochaine ville à étudier
		this.m_solution.setCityPosition(villes_non_visitees.get(0), t); //ajoute ville la plus proche a la solution
		villes_non_visitees.remove(index_plusproche);//enleve ville visitee de l'arraylist
		return villes_non_visitees;
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
