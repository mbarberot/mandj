package problems;

import java.io.Serializable;

import javax.management.JMException;

import base.variables.Individual;


/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 * 
 * June 2010
 *
 * Last update March 2011
 * 
 * Abstract class representing a multiobjective optimization problem
 */
public abstract class Problem {
  
  /**
   * Stores the number of variables of the problem
   */
  protected int numberOfVariables_ ;
  
 /**
   * By default, the objective function is to be minimized
   */
  protected boolean isToMinimize = true;
  
  /**
   * Stores the problem name
   */
  protected String problemName_;
  
    
  /**
   * Stores the lower bound values for each variable (only if needed)
   */
  protected double [] lowerLimit_ ;
  
  /**
   * Stores the upper bound values for each variable (only if needed)
   */
  protected double [] upperLimit_ ;
  
  /**
   * Stores the number of bits used by binary coded variables (e.g., BinaryReal
   * variables). By default, they are initialized to DEFAULT_PRECISION)
   */
  protected int [] precision_  ;
    
  
  protected String [] objType_ ;
  
  
  /** 
   * Constructor. 
   */
  public Problem() {
	  
  } // Problem
        
  /** 
   * Gets the number of decision variables of the problem.
   * @return the number of decision variables.
   */
  public int getNumberOfVariables() {
    return numberOfVariables_ ;   
  } // getNumberOfVariables
    
  
  /** 
   * Gets the lower bound of the ith variable of the problem.
   * @param i The index of the variable.
   * @return The lower bound.
   */
  public double getLowerLimit(int i) {
    return lowerLimit_[i] ;
  } // getLowerLimit
    
  /** 
   * Gets the upper bound of the ith variable of the problem.
   * @param i The index of the variable.
   * @return The upper bound.
   */
  public double getUpperLimit(int i) {
    return upperLimit_[i] ;
  } // getUpperLimit 
    
  
  /**
   * Evaluates a <code>Solution</code> object.
   * @param solution The <code>Solution</code> to evaluate.
   */    
  public abstract void evaluate(Individual individual) ;    
  
  

  /**
   * Returns the number of bits that must be used to encode variable.
   * @return the number of bits.
   */
  public int getPrecision(int var) {
    return precision_[var] ;
  } // getPrecision

  /**
   * Returns array containing the number of bits that must be used to encode 
   * the variables.
   * @return the number of bits.
   */
  public int [] getPrecision() {
    return precision_ ;
  } // getPrecision

  /**
   * Sets the array containing the number of bits that must be used to encode 
   * the variables.
   * @param precision The array
   */
  public void setPrecision(int [] precision) {
    precision_ = precision;
  } // getPrecision

  public void setObjective(String type){
	  if (type.equalsIgnoreCase("minimize")){
		  isToMinimize = true;
	  } else if (type.equalsIgnoreCase("maximize")){
		  isToMinimize = false;
	  }
  }
  
  public boolean isTominize(){
	  return isToMinimize;
  }

   /**
   * Returns the problem name
   * @return The problem name
   */
  public String getName() {
    return problemName_ ;
  }
} // Problem