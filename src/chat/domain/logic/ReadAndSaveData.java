package chat.domain.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadAndSaveData {

	private final String PATH = "./database/";

	/**
	 * Saves a file with the given params to the "Database"-folder as a text file.
	 * 
	 * @param fileinfos contained by the file that is about to be saved
	 * @param filename  of the file
	 */
	public void saveFileWith(List<String> fileinfos, String filename) {
		File f = new File(PATH + filename + ".txt");
		if (fileinfos == null)
			return;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			int counter = 0;
			for (String info : fileinfos) {
				counter++;
				writer.write(info);
				if (counter < fileinfos.size()) {
					writer.write("#");
				}
			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Reads the data that is stored in a textfile.
	 * 
	 * @param filename of the file
	 * @return all the read information as a list of strings
	 */
	public List<String> readDataFromFile(String filename) {
		File f = new File(PATH + filename + ".txt");
		String[] information = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			while (br.ready()) {
				information = br.readLine().split("#");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (information == null) {
			return new ArrayList<String>();
		}
		return Arrays.asList(information);
	}

	/**
	 * Checks, if a file is already existing.
	 * 
	 * @param filename of the file
	 * @return wether there is a file under the given name
	 */
	public boolean doesFileExistAlready(String filename) {
		return new File(PATH + filename + ".txt").exists();
	}

	/**
	 * Makes an array of all channel names that are in the given directory.
	 * 
	 * @return array with all existing channel names
	 */
	public String[] getAllChannelNames() {
		File directory = new File(PATH + "Channels/");
		String[] channelList = directory.list();
		return channelList;
	}
}
