/**
 * @author Albert
 */
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.util.*; // import all instead

public class sim {


    public static void main (String args[]){
            // global
    int globalSick = 0;
    int globalDead = 0;
        // Initialization of global
        int[] position;
        int day = 0;
        // Get all input information
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
        
        // Pseudo random number generation
        long seed = 1354657; // For pseudo random number generation
        int amount = 100;
        double[] randList = generateNumbers(seed, amount); // arg1 seed, arg2 amount
        
        // Initialize NxN array
        Node[][] matrix = new Node[nDimension][nDimension];
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                int pos[] = {i,j};
                matrix[i][j] = new Node(pos, minDays, maxDays);
            }
        }
        
        // Sets sick
        System.out.println("Enter number of sick: ");
        int totalSick = reader.nextInt();
        for(int i = 0; i < totalSick; i++){
            System.out.println("Enter coordinates x,y ");
            String positionString = reader.next();
            String[] stringItem = positionString.replaceAll("\\s", "").split(",");
            setSick(matrix
                    [Integer.parseInt(stringItem[0])]
                    [Integer.parseInt(stringItem[1])]);
        }

        int randStart = 0;
        for(int i = 0; i < simulationDays; i++){
            printStatus(matrix, nDimension);
            spreadDiesease(matrix, pSick, pDead, randList, randStart, nDimension);
            randStart += amount/simulationDays;
            System.out.println("");
        }
        
        
    } // end of main
   
    public static void spreadDiesease(Node[][] matrix, double pSick, double pDead, double[] randList, int randStart, int nDimension){
        List<Node> setSickToday = new ArrayList<>();
        List<Node> setDeadToday = new ArrayList<>();
        
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                if(matrix[i][j].getDead() == 1 | matrix[i][j].getImmune() == 1 | matrix[i][j].getSick() == 1){ // If its dead or immune or already sick do nothing
                    
                }
                else {
                    if(isNeighbourSick(matrix, matrix[i][j])){ 
                        if(sickRisk(matrix[i][j], pSick, (randList[(i *nDimension + j + randStart)%100]))){ // ugly randlist will repeat index, 100 should be amount 
                            setSickToday.add(matrix[i][j]);
                            //setSick(matrix[i][j]);
                        }
                    }
                }

                if(matrix[i][j].getSick() == 1 & matrix[i][j].getImmune() == 0){
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
            setSick(node);
            sickToday++;
        }
        int deadToday = 0;
        for(Node node: setDeadToday){
            node.setDead();
            deadToday++;
        }
        System.out.println("Nodes that turned sick today is: " + sickToday);
        System.out.println("Nodes that died today is: " + deadToday);
        
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                matrix[i][j].incDay();
            }
        }

    }
    // Returns boolean true if node got sick, false if it didnt.
     public static boolean sickRisk(Node node, double pSick, double random){
        boolean sick = false;
        if(random < pSick){ 
            sick = true;
            //System.out.println("random number was " + random + " person is now sick " + node.getRowPosition() + "," + node.getCowPosition());
        }
        return sick;
    }

    public static double[] generateNumbers(long seed, int amount) {
        double[] randomList = new double[amount];
        for (int i = 0; i < amount; i++) {
            Random generator = new Random(seed);
            randomList[i] = (generator.nextDouble() % 0.001 * 1000);
            seed--;
            }
        return randomList;
    }
    
    
    public static boolean isNeighbourSick(Node[][] matrix ,Node node){
        boolean bool = false;
        node.calcNeighbours(matrix);
        for (int i = 0; i < node.neighbours.size(); i++) {
            if(getSick((Node)node.neighbours.get(i)) == 1) bool = true;
        }
        return bool;
    }
    
    public static void printStatus(Node[][] matrix, int nDimension){
        // Print out sick status
        for(int i = 0; i < nDimension; i++){
            for(int j = 0; j < nDimension; j++){
                System.out.print(matrix[i][j].getStatus());
            }
            System.out.println("");
        }
    }
    
    public static int getSick(Node node){
        return node.getSick();
    }

    public static void setSick(Node node){
        node.setSick();
    }
    
    public static void setImmune(Node node){
        node.setImmune();
    }
    
}