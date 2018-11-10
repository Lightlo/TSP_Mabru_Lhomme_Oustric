package tsp;
import java.util.ArrayList; 

public class TSPSolver_LocalSearch_PPV {
	

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
		public TSPSolver_LocalSearch_PPV(Instance instance, long timeLimit) {
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
			
			//Initialisation de la "time loop"
			long startTime = System.currentTimeMillis();
			long spentTime = 0;
			 
		    //On initialise la solution à la solution du PPV   
		    this.m_solution = this.PPV();
			
			//Mise en place de la RechercheLocale sur le PPV
		    //Cette RechercheLocale utilise un voisinage définit par la permutation swap (voir classe Solution) 
			
		    //Copie de la solution PPV sur laquelle on fera des permutations + itérations
			Solution candidat = this.m_solution.copy();
			long score_solution = this.m_solution.getObjectiveValue();
			long score_candidat = candidat.getObjectiveValue();
			
			boolean arret; //Condition d'arrêt de la boucle
			do {
				for(int i=1;i<this.m_instance.getNbCities()-2;i++) { 
					for(int j=i+1;j<this.m_instance.getNbCities()-1;j++) { //On a bien i<j pour #swap()
						Solution provisoire = this.m_solution.copy(); //2ème copie de la solution pour tester si elle est meilleure ou non
						provisoire.swap(i,j); //permutations de i et j dans le tableau des villes (voir classe Solution pour #swap() ) 
						long score_provisoire = provisoire.getObjectiveValue();
						if(score_provisoire<score_candidat) { 	//test du score: 
							score_candidat=score_provisoire; 	//si il est inférieur au candidat proposé, on garde celui-ci pour la prochaine itération
							candidat=provisoire; 				//et devient le candidat
						}
					}
				}
				if(score_solution>score_candidat) { //Si la condition est vérifiée: le score est amélioré
					arret=false; //Condition d'arrêt est fausse : recherche locale sur candidat
					score_solution=score_candidat; //La solution finale prend alors la valeur du candidat
					this.m_solution=candidat;
					this.m_solution.print(System.err);
				} else {
					arret = true; //Condition d'arrêt vraie : on a trouvé un minimum local
				}
				
				spentTime = System.currentTimeMillis() - startTime;
				
			} while (!arret && (spentTime < m_timeLimit * 1000) );
			
		}
		
		//-----------------------------
		//-----FONCTIONS ANNEXES-------
		//-----------------------------

		/**
		 * Renvoie la solution par l'algorithme du PPV à partir de la ville d'origine/d'arrivée 0
		 * 
		 * @return la solution Solution par l'algorithme du PPV (plus proche voisin) du problème considéré
		 * @throws Exception Voir les exceptions renvoyées par {@link #getSolution()}, {@link #getInstance()}
		 */
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

		/**
		 * Initialise une ArrayList avec l'ensemble des villes du problème à visiter
		 * @param NbCities le nombre de villes du problème considéré
		 * @return une ArrayList d'entiers contenant l'ensemble des villes représentées par les entiers de 0 inclu à NbCities exclu
		 */
		public ArrayList<Integer> initializeUnvisitedCities(int NbCities){
			ArrayList<Integer> UnvisitedCities = new ArrayList<Integer>();
			for(int i=0; i<NbCities;i++) {
				UnvisitedCities.add(i);
			}
			return UnvisitedCities;
		}

		/**
		 * Permet de trouver la prochaine ville la plus proche à visiter,
		 * de mettre à jour la solution correspondant au PPV avec cette ville,
		 * et de mettre à jour l'ArrayList contenant les villes non visitées.
		 * @param NbVisitedCities le nombre de villes déjà visitées
		 * @param UnvisitedCities ArrayList contenant l'ensemble des villes du problème qu'il reste à visiter
		 * @return l'ArrayList d'entiers contenant l'ensemble des villes qu'il reste à visiter
		 * @throws Exception Voir les exceptions renvoyées par {@link #getSolution()}, {@link #getInstance()}
		 */
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

