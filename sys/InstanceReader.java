

package mariam.sys;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InstanceReader {
	
	protected FileReader reader;
	
	protected BufferedReader buffer;
	
	protected String filename;
	
	public InstanceReader(String filename){
		this.filename = filename;		
	}
	
	/**
	 * Open file to read
	 */
	public void open() {
		try {
			this.reader = new FileReader(filename);
			this.buffer = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close file after read it
	 */
	public void close() {
		try {
			if (buffer != null) {
				buffer.close();
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read a line
	 * @return A line read
	 */
	public String readLine(){
		try {
			return buffer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int readInt() {
		return Integer.valueOf(readLine());
	}
	
	public double[][] readDoubleMatrix(int i, int j) {
		return readDoubleMatrix(i, j, ",");
	}

	public double[][] readDoubleMatrix(int i, int j, String separator) {
		double[][] result = new double[i][j];

		for (int k = 0; k < i; k++) {
			String[] split = readLine().split(separator);
			result[k] = Convert.toDoubleArray(split);
		}

		return result;
	}

	public int[] readIntVector(String separator) {
		String[] split = readLine().split(separator);

		int[] result = new int[split.length];

		for (int k = 0; k < split.length; k++) {
			result[k] = Integer.valueOf(split[k]);
		}

		return result;
	}

	public int[] readIntVector(int size, String separator) {
		return readIntVector(",");
	}
	
	public int[][] readIntMatrix(int i, int j) {
		return readIntMatrix(i, j, ",");
	}

	public int[][] readIntMatrix(int i, int j, String separator) {
		int[][] result = new int[i][j];

		for (int k = 0; k < i; k++) {
			String[] split = readLine().split(separator);
			result[k] = Convert.toIntArray(split);
		}

		return result;
	}
}
