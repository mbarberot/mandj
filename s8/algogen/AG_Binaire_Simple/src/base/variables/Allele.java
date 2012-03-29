package base.variables;

import java.util.BitSet;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * September 2009
 * 
 * last update: March 2011
 * 
 * 
 * An allele is one of the different values that can take a single gene.
 * 
 */

public class Allele {

	private Gene gene;

	private int intValue;
	private BitSet bitValue;
	private int nbBits;

	/**
	 * Instantiates a new Allele with a random value which is valid with the
	 * specification of the associated gene.
	 * 
	 * @param gene
	 */
	public Allele(Gene gene) {
		this.gene = gene;
		nbBits = gene.getNbBits();

		bitValue = new BitSet(nbBits);

		for (int i = 0; i < nbBits; i++){
			double alea = Math.random();
			if (alea < 0.5){
				bitValue.clear(i);
			} else{
				bitValue.set(i);
			}
		}

		intValue = Integer.parseInt(toString(bitValue),2);

	}

	/**
	 * 
	 */

	public void setDoubleValue(Double newValue){
		Double d = (((newValue - gene.getMin()) / (gene.getMax() - gene.getMin())) 
				* Math.pow(2, nbBits));
		intValue = (int) Math.round(d + .5);
		bitValue = toBitSet(intValue);
	}

	public void setIntValue(int newValue) {
		intValue = newValue;
		bitValue = toBitSet(intValue);
	}

	public void setBitValue(BitSet newValue){
		bitValue = newValue;
		intValue = Integer.parseInt(toString(bitValue),2);
	}

	public void setStringValue(String newValue){
		intValue = Integer.parseInt(newValue,2);
		bitValue = toBitSet(intValue);
	}
	/**
	 * Returns the current real value of this allele.
	 * 
	 * @return Returns the value.
	 */
	
	public double getDoubleValue(){
		double doubleValue = (intValue * (gene.getMax() - gene.getMin())/ 
				Math.pow(2, nbBits)) + gene.getMin();
		return doubleValue;
	}
	
	public int getIntValue(){
		return intValue;
	}
	
	public BitSet getBitValue(){
		return bitValue;
	}

	public String getStringValue(){
		return toString(bitValue);
	}

	
	/**
	 * Accesses the associated gene of this allele.
	 * 
	 * @return Returns the gene.
	 */
	public Gene getGene() {
		return gene;
	}

	public String intToBinary (int intValue){
		String binaryValue = Integer.toBinaryString(intValue);
		// To complete all binaryValues to "nbBits" digits
		int len = binaryValue.length();
		for (int i = 0; i< nbBits-len; i++){
			binaryValue = 0+binaryValue;
		}
		return binaryValue;
	}

	public String toString(BitSet bs){
		String s = "";
		for (int i = 0; i < nbBits; i++){
			s += bs.get(i) == true ? "1" : "0";
		}
		return s;
	}

	public BitSet toBitSet(int n){
		BitSet bs = new BitSet(nbBits);
		String s = intToBinary(n);
		for (int i = 0; i < s.length(); i++){
			if (s.charAt(i) == '0'){
				bs.clear(i);
			} else{
				bs.set(i);
			}
		}
		return bs;
	}
}