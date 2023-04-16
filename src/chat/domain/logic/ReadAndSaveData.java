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

	private final String PATH = "./resources/";

	public void saveFileWith(List<String> userinfos, String filename) {
		File f = new File(PATH + filename + ".txt");
		if (userinfos.isEmpty() || userinfos == null) {
			return;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			for (String info : userinfos) {
				writer.write(info + "\n");
			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public List<String> readDataFromFile(String filename) {
		File f = new File(PATH + filename + ".txt");
		String[] information = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			while (br.ready()) {
				information = br.readLine().split("\n");
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

	public boolean doesFileExistAlready(String filename) {
		return new File(PATH + filename + ".txt").exists();
	}

	public String[] getAllChannelNames() {
		File directory = new File(PATH + "Channels/");
		String[] channelList = directory.list();
		return channelList;
	}
}
