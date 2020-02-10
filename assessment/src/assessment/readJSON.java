package assessment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class readJSON {
	public static List<String> getWords(String filePath) throws IOException {
		List<String> list = new ArrayList<String>();
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;

			while ((lineTxt = bufferedReader.readLine()) != null) {
				lineTxt = lineTxt.replaceAll("[\\pP\\pS\\pZ]", "");
				list.add(lineTxt.toLowerCase());
			}
			bufferedReader.close();
			read.close();
		} else {
			System.out.println("Cannot find the path");
		}
		return list;
	}

	public static HashMap<Character, Integer> getLetterValues(String filePath) throws IOException {
		HashMap<Character, Integer> map = new HashMap<>();
		File file = new File(filePath);
		if (file.isFile() && file.exists()) { 
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;

			while ((lineTxt = bufferedReader.readLine()) != null) {
				// System.out.println(lineTxt);
				lineTxt = lineTxt.replaceAll("[\\pP\\pS\\pZ]", "").trim();
				String[] line = lineTxt.split("");
				char letter = line[0].charAt(0);
				Integer value = null;
				if (line.length > 1) {
					value = Integer.valueOf(line[1]);
					if (line.length > 2) {
						value = 10;
					}
				}
				map.put(letter, value);

			}
			bufferedReader.close();
			read.close();
		}
		return map;
	}

	public static List<String> getTest(String filePath) throws IOException {
		List<String> strings = new ArrayList<>();
		File file = new File(filePath);
		if (file.isFile() && file.exists()) { 
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;

			while ((lineTxt = bufferedReader.readLine()) != null) {
				strings.add(lineTxt);

			}
			bufferedReader.close();
			read.close();
		}
		return strings;
	}

}
