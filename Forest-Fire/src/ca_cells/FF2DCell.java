//*******************************************
//* 
//*    Forest Fire CA - a Cell object 
//*    Dr Martin Bayley 18/02/11
//*    Module COM2005
//*
//*******************************************

package ca_cells;                 // assign class to ca_cells package (implications for class dir. structure)

import java.util.Random;

public class FF2DCell{           // declare class 

  //******************************************************************************  
  //  Constructor - initialise all private state varibles  to false (zero) state 
  //******************************************************************************

  public FF2DCell(){       // At construction initialise a 3x3 private state array to all false
    
    int i; int j;           

    for(i=0;i<3;i++){
      for(j=0;j<3;j++){
        cellState[i][j]=false;
      }
    }
  }
  
  //******************************************************************************************
  //  Member functions to allow the Grid class (next level up-defining a group of cells)
  //  to set up the private cell-state variables for the neighbourhood and report its own state 
  //******************************************************************************************

  public void setState(int xPos, int yPos, boolean setStateIn){    // set a state in the 3x3 state array
    cellState[xPos][yPos]=setStateIn;
  }

  public boolean getState(){                              // return the cell state (pos 1,1 of private state array)
    return(cellState[1][1]);
  }

  //****************************************************************************
  //  Create a next-state method to encapsulate FF rules and return the 
  //  next generational state for that cell. 
  //****************************************************************************

///////////////////////////////////////////////////////////////////////////////////////////////////////////
// Conway's Game of Life Rules:
//
// 1. DECISION METRIC -Sum all live neighbours.
// 2. KILLING RULE    -If live, and nLiveNeighbors is not 2 or 3 then next state changes to dead.
// 3. LIVING RULE     -If dead, and nLiveNeighbors=3 then state changes to live next step. 
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////

  // nextState takes in fuel, the current fire state, and wind direction
  public boolean nextState(int cellFuel, boolean currentOnFire, int biasX, int biasY){

    int nLiveN=0;                        // local to store no. of live neighbour states
    int i; int j;                        // iterators
    boolean nextState=cellState[1][1];   // set the default return state to be the unchanged current state 
    int probContr = 1;					 // probabilistic contribution per neighbour on fire
    Random rand = new Random();
    int windBias = 0;					 // initialise the bias due to wind as 0

    
    // count the number of live neighbours
    for(i=0;i<3;i++){
      for(j=0;j<3;j++){
        if((i!=1)||(j!=1)){              // don't include the current cell state
          if (cellState[i][j]==true) {
            nLiveN++ ;
            //Check for the wind direction 
            //With the wind, spread is more likely
            if (biasX==i && biasY==j){
            	windBias = 5;
            }
            //Against the wind, spread is less likely
            else if (2-biasX==i && 2-biasY==j){
            	windBias = -5;
            }
          }
        }
      }
    }
    
    // If the current cell is on fire and there's fuel left, the next state is also on fire
    if (currentOnFire && cellFuel>0) {
    	nextState = true;
    }
    // Else, compute probability of catching fire
    else if (!currentOnFire && nLiveN>0 && cellFuel>0) {
    	// Threshold to pass in order to catch fire. (Neighbours on fire)*(probability per neighbour) + (wind bias)
		int catchThreshold = nLiveN*probContr + windBias;
		int catchVal = rand.nextInt(20);	// Effective 'dice roll'
    	
    	// if catchVal is less than the threshold, set the cell on fire
		if (catchVal<=catchThreshold){
			nextState = true;
		}
    	
		else {
			nextState = false;
		}
    	//nextState = true;		// For when testing without probabilistic spread
	}
    // Else, the cell becomes dormant
    else if (cellFuel == 0){
    	nextState = false;
    }
    
    return(nextState);	                    // pass out the new nextState value 
  }

  //******************************************************************************
  //private components - stores states of cell and all other in its neighbourhood
  //******************************************************************************

  private boolean[][] cellState= new boolean[3][3];  // the private 3x3 array of cell and neighbour states
}