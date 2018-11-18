/**
 * @author Albert
 */

import java.util.*;
import java.io.*; 
public class Node {
    int positionRow;
    int positionCol;
    LinkedList neighbours;
    // Sick spread
    int daysSick;
    int minDaysSick;
    int maxDaysSick;
    int daysBeforeImmune;
    // Status of node
    int sick;     
    int immune;
    int dead;

    public Node(int[] position, int minDays, int maxDays) { //Constructor
        this.positionRow = position[0];
        this.positionCol = position[1];
        this.neighbours = new LinkedList();
        this.sick = 0;
        this.immune = 0;
        this.dead = 0;
        this.daysSick = 0;
        this.minDaysSick = minDays;
        this.maxDaysSick = maxDays;
        this.daysBeforeImmune = maxDays;
    }
    
    public int getSick(){
        return this.sick;
    }
    
    public void setSick(){
        this.sick = 1;
        Random generator = new Random(this.positionCol + this.positionRow + 420003156); // col + 42 is seed
        double rand = (generator.nextDouble() % 0.001 * 1000);
        int daysBeforeImmune = (int) (minDaysSick + (maxDaysSick - minDaysSick + 1) * rand );
        System.out.println("days before immune " + daysBeforeImmune);
    }
    
    // Returns boolean true if node got sick, false if it didnt.
    public boolean sickRisk(double pSick, double random){
        boolean sick = false;
        if(random < pSick){ 
            sick = true;
            //System.out.println("random number was " + random + " person is now sick " + node.getRowPosition() + "," + node.getCowPosition());
        }
        return sick;
    }
    
    public void setDead(){
        this.dead = 1;
    }
    public int getDead(){
        return this.dead;
    }
    
    public char getStatus(){
        char status = '0';
        if(this.sick == 1) status = 'S';
        if(this.immune == 1) status = 'I';
        if(this.dead == 1) status = 'D'; 
        return status;
    }
    
    // Increases days sick incase of node becoming immune
    public void incDay(){
        if (this.sick == 1 & this.dead != 1){
            this.daysSick++;
            if(daysSick >= daysBeforeImmune) this.immune = 1; 
        }
    }
    
    public int getImmune(){
        return this.immune;
    }
    public void setImmune(){
        this.sick = 0;
        this.immune = 1;
    }
    
    public int getRowPosition(){
        return this.positionRow;
    }
    public int getCowPosition(){
        return this.positionCol;
    }
    
    // Calculates all adjacent neighbours and stores them in "neigbours"
    public void calcNeighbours(Node[][] matrix){
        for(int i  = this.positionRow - 1; i <= this.positionRow + 1; i++){
            for(int j  = this.positionCol - 1; j <= this.positionCol + 1; j++){
                try{
                    
                    if(i == this.positionRow & j == this.positionCol){
                        // We dont want to add self as a neighbour.
                    }
                    else{
                        //System.out.println("printing out for position " + i + "," + j);
                        this.neighbours.add(matrix[i][j]);
                    }
                }
                catch(ArrayIndexOutOfBoundsException exception){
                    // Do nothing
                }
            }
        }

    }
    
    // For debug
    public void printNeighbours(){
        for (int i = 0; i < this.neighbours.size(); i++) {
            System.out.println(this.neighbours.get(i));
        }
    }
    
}
