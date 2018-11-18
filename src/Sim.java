/**
 * @author abbenteprizer
 */
import java.util.*;

// Creates N*N cell automata with states, alive, sick, dead and immune. 
public class Sim {
    // Global variables
    public static int totalSick = 0;
    public static int totalDead = 0;
    public static int totalImmune = 0;
    
    public static void main (String args[]){ // Use from command line later
        int[] position;
        int day = 0;
        // These are the default value, if none are given to main.
        int nDimension = 4;//6;
        double pSick = 1;//0.08;
        double pDead = 0.15;
        int simulationDays = 20;
        int minDays = 2;;
        int maxDays = 4;
        int sickInitially = 2;
        long seed = 1354657;  
        
        // This is for pseudo random number generator
        int amount = 100;
        double[] randList = generateNumbers(seed, amount); 
        
        // Take arg from main
        try{ // This is to use default values in case not all given from args
            nDimension = Integer.parseInt(args[0]);
            pSick = Double.parseDouble(args[1]);
            pDead = Double.parseDouble(args[2]);
            simulationDays = Integer.parseInt(args[3]);
            minDays = Integer.parseInt(args[4]);;
            maxDays = Integer.parseInt(args[5]);
            sickInitially = Integer.parseInt(args[6]);
        }
        catch(Exception e){
            System.out.println("Using default values for some parameters");
        }
        /*
        // Get all input information through prompt
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter dimension N: ");
        int nDimension = reader.nextInt();
        System.out.println("Enter probability sick: ");
        double pSick = reader.nextDouble();
        System.out.println("Enter probability dying: ");
        double pDead = reader.nextDouble();
        System.out.println("Days to simulate: ");
        int simulationDays = reader.nextInt();
        System.out.println("Enter min days: ");
        int minDays = reader.nextInt();
        System.out.println("Enter max days: ");
        int maxDays = reader.nextInt();
        System.out.println("Enter number of sick: ");
        int sickInitially = reader.nextInt();
        */
        
        // Initialize NxN array
        Node[][] matrix = new Node[nDimension][nDimension];
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                int pos[] = {i,j};
                matrix[i][j] = new Node(pos, minDays, maxDays);
            }
        } 
    
        
        // Set sick nodes for initial state
        try{
        totalSick = sickInitially;
        for(int i = 0; i < sickInitially; i++){
            System.out.println("Enter coordinates x,y ");
            String positionString =  args[7+i]; //args[7 + i];  reader.next(); // argument from main
            String[] stringItem = positionString.replaceAll("\\s", "").split(",");
            matrix[Integer.parseInt(stringItem[0])] // SetSick on node
                  [Integer.parseInt(stringItem[1])].setSick();
        }
        }
        catch(Exception e){
            System.out.println("Using default values [1][1] and [3][3]");
            try{
                matrix[1][1].setSick();
                matrix[3][3].setSick();
            }
            catch(Exception err){
                System.out.println("Error, is array bigger than 3*3");
            }
        }
        
        // Pseudo random number generation
        try{
            seed = Long.parseLong(args[7 + sickInitially]); 
            randList = generateNumbers(seed, amount); 
        }
        catch(Exception seederror){
            
        }
        // Simulation for each day
        int randStart = 0; // Used to traverse further in pseudo random list. 
        for(int i = 0; i < simulationDays; i++){
            printStatus(matrix, nDimension); // Prints the N'N table
            spreadDisease(matrix, pSick, pDead, randList, randStart, nDimension);
            randStart += amount/simulationDays;
            System.out.println("");
        }
        printStatus(matrix, nDimension); // In order to get last day 
        
    } 
    // Spread the disease and update nodes that get sick, immune or die
    public static void spreadDisease(Node[][] matrix, double pSick, double pDead, double[] randList, int randStart, int nDimension){
        List<Node> setSickToday = new ArrayList<>();
        List<Node> setDeadToday = new ArrayList<>();
        
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                if(matrix[i][j].getDead() == 1 | matrix[i][j].getImmune() == 1 | matrix[i][j].getSick() == 1){ // If its dead or immune or already sick do nothing
                    
                }
                else {
                    if(isNeighbourSick(matrix, matrix[i][j])){ 
                        if(matrix[i][j].sickRisk(pSick, (randList[(i *nDimension + j + randStart)%100]))){ // ugly randlist will repeat index, 100 should be amount 
                            setSickToday.add(matrix[i][j]);
                        }
                    }
                }

                if(matrix[i][j].getSick() == 1 & matrix[i][j].getImmune() == 0 & matrix[i][j].getDead() == 0){
                    if((randList[(i * nDimension + j + randStart)% 100])  < pDead){
                        setDeadToday.add(matrix[i][j]);
                    }
                }
            }
        System.out.println("");
        }
        
        // These are updated outside of the loop
        int sickToday = 0;
        for(Node node: setSickToday){
            node.setSick();
            sickToday++;
        }
        int deadToday = 0;
        for(Node node: setDeadToday){
            node.setDead();
            deadToday++;
        }
        int totalImmune = 0;
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                if(matrix[i][j].getImmune() == 1) totalImmune++;
            }
        }
        
        System.out.println("Nodes that turned sick today is: " + sickToday);
        System.out.println("Nodes that died today is: " + deadToday);
        System.out.println("Nodes that became immune today is: " + totalImmune);
        totalSick += sickToday;
        System.out.println("Total nodes that is/was sick is: " + totalSick);
        totalDead += deadToday;
        System.out.println("Total nodes that is dead is: " + totalDead);
        
        // Increase day to update if any node becomes immune
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                matrix[i][j].incDay(); 
            }
        }
        // Calculate immune today and update totalImmune
        totalImmune = 0;
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                if(matrix[i][j].getImmune() == 1) totalImmune++; 
            }
        }
        System.out.println("Total Nodes immune: " + totalImmune);
        
       
        

    }

    // Pseudo random number generator
    public static double[] generateNumbers(long seed, int amount) {
        double[] randomList = new double[amount];
        for (int i = 0; i < amount; i++) {
            Random generator = new Random(seed);
            randomList[i] = (generator.nextDouble() % 0.001 * 1000);
            seed--;
            }
        return randomList;
    }
    
    // Checks if any adjacent node is sick
    public static boolean isNeighbourSick(Node[][] matrix ,Node node){
        boolean bool = false;
        node.calcNeighbours(matrix);
        for (int i = 0; i < node.neighbours.size(); i++) {
            if(((Node)node.neighbours.get(i)).getSick() == 1) bool = true;
        }
        return bool;
    }
    
    // Prints out the table
    public static void printStatus(Node[][] matrix, int nDimension){
        // Print out sick status
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                System.out.print(matrix[i][j].getStatus());
            }
            System.out.println("");
        }
    }
    
}