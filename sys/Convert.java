

package mariam.sys;

import java.text.DecimalFormat;

public class Convert {
	
	/**
	 * Convert a matrix to dot string to use in Graphviz
	 * 
	 * @param matrix Matrix will be converted
	 * @return The string formatted in Dot
	 */
	public static String toDot(double[][] matrix){
		if(matrix == null || matrix.length == 0){
			throw new IllegalArgumentException("matrix shouldn't be null or empty");
		}
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("graph G {\n");
		buffer.append("layout=\"circo\";\n");
		
		DecimalFormat df = new DecimalFormat("0.000000000000000");
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i; j < matrix.length; j++) {
				if(i != j){
					buffer.append(i+" -- "+j+" [penwidth="+df.format(matrix[i][j])+"]\n");
				}
			}
		}
		
		buffer.append("}");
		
		return buffer.toString();
	}
	
	/**
	 * Convert from string array to double array
	 * @param array Array will be converted
	 * @return A double array
	 */
	public static double[] toDoubleArray(String[] array){
		if (array == null) {
			throw new IllegalArgumentException("array shouldn't be null");
		}
		
		double[] result = new double[array.length];
		
		for (int i = 0; i < array.length; i++) {
			result[i] = Double.valueOf(array[i].trim());
		}
		
		return result;
	}
	
	/**
	 * Convert from string array to int array
	 * @param array Array will be converted
	 * @return A int array
	 */
	public static int[] toIntArray(String[] array){
		if (array == null) {
			throw new IllegalArgumentException("array shouldn't be null");
		}
		
		int[] result = new int[array.length];
		
		for (int i = 0; i < array.length; i++) {
			result[i] = Integer.valueOf(array[i].trim());
		}
		
		return result;
	}

}
