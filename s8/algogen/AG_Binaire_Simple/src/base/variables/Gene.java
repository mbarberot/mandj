package base.variables;

import java.math.BigDecimal;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * September 2009
 * 
 * last update: March 2011
 * 
 * 
 * A gene is a basic unit of information that characterizes an individual (a solution) 
 * 
 */

public class Gene {

	private double gene [] = new double [3];
	private double min, max, precision;

	public Gene(double minValue, double maxValue, double precision){
		gene[0] = minValue;
		gene[1] = maxValue;
		gene[2] = precision;
		min = minValue;
		max = maxValue;
		this.precision = precision;
	}


	/**
	 * @return Returns the lower bound of the gene.
	 */
	public double getMin() {
		return gene[0];
	}

	/**
	 * @return Returns the upper bound of the gene.
	 */
	public double getMax() {
		return gene[1];
	}

	/**
	 * @return Returns the precision of the gene.
	 */
	public int getPrecision() {
		return (int)gene[2];
	}

	public int getNbBits(){
		double len = Math.log((max - min) * Math.pow(10,precision)) / Math.log(2);
		return (int) Math.ceil(len);
	}

	/**
	 * @return Returns the different information relative to the gene (lower and upper bounds, and the precision associated with this gene)
	 */
	public String toString() {
		String s = "";
		for(int i = 0; i < gene.length; i++){
			s += gene[i] + " ";
		}
		return s;
	}

}