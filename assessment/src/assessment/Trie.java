package assessment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Trie {

	static final int SIZE = 26;
	static HashMap<Character, Integer> letterValue = new HashMap<>();
	static int stringLength = 0;
	static TrieNode root = new TrieNode();

	// trie Node
	static class TrieNode {
		TrieNode[] Child = new TrieNode[SIZE];

		// isLeaf is true if the node represents
		// end of a word
		boolean leaf;

		// Constructor
		public TrieNode() {
			leaf = false;
			for (int i = 0; i < SIZE; i++)
				Child[i] = null;
		}
	}

	static void insert(TrieNode root, String Key) {
		int n = Key.length();
		TrieNode pChild = root;

		for (int i = 0; i < n; i++) {
			int index = Key.charAt(i) - 'a';

			if (pChild.Child[index] == null)
				pChild.Child[index] = new TrieNode();

			pChild = pChild.Child[index];
		}

		// make last node as leaf node
		pChild.leaf = true;
	}

	static void searchWord(TrieNode root, boolean Hash[], String str, List<String> result, FrequencyCounter avail) {
		// if we found word in trie / dictionary
		if (root.leaf == true) {
			FrequencyCounter check = new FrequencyCounter();
			Trie trie = new Trie();
			check = trie.stringToMap(str);
			boolean state = true;
			for (char c : check.stringAvail.keySet()) {
				if (avail.stringAvail.get(c) == null || avail.stringAvail.get(c) < check.stringAvail.get(c)) {
					state = false;
					break;
				}
			}
			if (state == true) {
				result.add(str);
			}
		}

		// traverse all child's of current root
		for (int K = 0; K < SIZE; K++) {
			if (Hash[K] == true && root.Child[K] != null) {
				// add current character
				char c = (char) (K + 'a');

				// Recursively search reaming character
				// of word in trie
				searchWord(root.Child[K], Hash, str + c, result, avail);
			}
		}
	}

	static List<String> PrintAllWords(char Arr[], TrieNode root, int n, FrequencyCounter avail) {
		// create a 'has' array that will store all
		// present character in Arr[]
		boolean[] Hash = new boolean[SIZE];
		List<String> result = new ArrayList<>();
		for (int i = 0; i < n; i++)
			Hash[Arr[i] - 'a'] = true;

		// tempary node
		TrieNode pChild = root;

		// string to hold output words
		String str = "";

		// Traverse all matrix elements. There are only
		// 26 character possible in char array
		for (int i = 0; i < SIZE; i++) {
			// we start searching for word in dictionary
			// if we found a character which is child
			// of Trie root
			if (Hash[i] == true && pChild.Child[i] != null) {
				str = str + (char) (i + 'a');
				searchWord(pChild.Child[i], Hash, str, result, avail);
				str = "";
			}
		}
		return result;
	}

	// convert string to hashmap
	public FrequencyCounter stringToMap(String string) {
		FrequencyCounter temp = new FrequencyCounter();
		char[] cs = string.toCharArray();
		for (char c : cs) {
			if (temp.stringAvail.get(c) == null) {
				temp.stringAvail.put(c, 1);
			} else {
				temp.stringAvail.put(c, temp.stringAvail.get(c) + 1);
			}
		}
		return temp;
	}

	public Solution subsets(List<String> nums, FrequencyCounter avail, HashMap<FrequencyCounter, Solution> dpMap) {

		// call dfs to do backtracking
		Solution score = dfs(new Stack<String>(), 0, nums, avail, dpMap);
		return score;
	}

	private Solution dfs(Stack<String> cur, int start, List<String> nums, FrequencyCounter avail,
			HashMap<FrequencyCounter, Solution> dpMap) {
		boolean able2MoveAhead = false;
		int maxScoreForCur = 0;
		if (start > nums.size() - 1 || nums.size() == 0 || avail.allZero()) {
			return new Solution(maxScoreForCur, new ArrayList<String>());
		}

		if (dpMap.containsKey(avail)) {
			Solution ret = dpMap.get(avail);
			return ret;
		}
		int maxIndex = -1;
		List<String> bestMatch = new ArrayList<String>();

		// Do backtracking from current start point.
		for (int i = start; i < nums.size(); i++) {
			boolean state = removeFromAvail(nums.get(i), avail);
			able2MoveAhead = able2MoveAhead || state;
			if (state) {
				cur.push(nums.get(i));
				Solution childScore = dfs(cur, 0, nums, avail, dpMap);
				int availScore = childScore.score + calculateScore(nums.get(i));
				if (availScore > maxScoreForCur) {
					maxScoreForCur = availScore;
					maxIndex = i;
					bestMatch.clear();
					for (String str : childScore.match)
						bestMatch.add(str);
				}
				cur.pop();
				addToAvail(nums.get(i), avail);
			}
		}
		if (!able2MoveAhead)
			return new Solution(maxScoreForCur, new ArrayList<String>());
		bestMatch.add(nums.get(maxIndex));
		Solution solution = new Solution(maxScoreForCur, bestMatch);
		dpMap.put(avail.clone(), solution);
		// list of words low.push(nums.get(maxIndex));

		return solution;
	}

	// remove string from the available letters
	public boolean removeFromAvail(String s, FrequencyCounter avail) {
		FrequencyCounter word = new FrequencyCounter();
		word = stringToMap(s);
		boolean state = true;
		for (char c : word.stringAvail.keySet()) {
			int temp = 0;
			if (!avail.stringAvail.containsKey(c))
				return false;
			temp = avail.stringAvail.get(c) - word.stringAvail.get(c);
			if (temp < 0) {
				return false;
			}
		}

		for (char c : word.stringAvail.keySet()) {
			int temp = 0;
			temp = avail.stringAvail.get(c) - word.stringAvail.get(c);
			if (temp == 0) {
				avail.stringAvail.remove(c);
			} else {
				avail.stringAvail.put(c, temp);
			}
		}
		return state;
	}

	// add a string to the available letters
	public FrequencyCounter addToAvail(String s, FrequencyCounter avail) {
		FrequencyCounter word = new FrequencyCounter();
		word = stringToMap(s);
		for (char c : word.stringAvail.keySet()) {
			if (avail.stringAvail.get(c) == null) {
				avail.stringAvail.put(c, word.stringAvail.get(c));
			} else {
				avail.stringAvail.put(c, word.stringAvail.get(c) + avail.stringAvail.get(c));
			}
		}
		return avail;
	}

	// calculate the score of a string
	public int calculateScore(String s) {
		int sum = 0;
		// System.out.println("cur size: "+ cur.size());

		char[] cs = s.toCharArray();
		int score = 0;
		for (char c : cs) {
			score += letterValue.get(c);
		}
		sum += s.length() == stringLength ? score * s.length() + 50 : score * s.length();
		return sum;
	}

	// convert available letters into char array
	public char[] availToCharArray(FrequencyCounter avail) {
		String word = "";
		for (char c : avail.stringAvail.keySet()) {
			for (int i = 0; i < avail.stringAvail.get(c); i++) {
				word = word + c;
			}
		}
		return word.toCharArray();
	}

	// sort chars in string in Alphabet order
	public static String sortString(String inputString) {
		// convert input string to char array
		char tempArray[] = inputString.toCharArray();

		// sort tempArray
		Arrays.sort(tempArray);

		// return new sorted string
		return new String(tempArray);
	}

	// remove the words contain same letters but in different order
	public List<String> matchesClean(List<String> result) {
		HashMap<String, List<String>> matches = new HashMap<>();
		for (String string : result) {
			String s = sortString(string);
			if (matches.get(s) == null) {
				List<String> list = new ArrayList<>();
				list.add(string);
				matches.put(s, list);
			} else {
				matches.get(s).add(string);
			}
		}
		List<String> list = new ArrayList<>();
		for (String string : matches.keySet()) {
			list.add(string);
		}
		return list;
	}

	public static void main(String[] args) throws IOException {
		// read files to memory
		List<String> dict = assessment.readJSON.getWords("/Users/ppp/Desktop/assessment/src/words.json");
		letterValue = assessment.readJSON.getLetterValues("/Users/ppp/Desktop/assessment/src/letter-values.json");
		List<String> test = assessment.readJSON.getTest("/Users/ppp/Desktop/assessment/src/test.txt");

		// insert words in Dict into Trie.
		Trie trie = new Trie();
		for (String s : dict) {
			insert(root, s);
		}

		// loop the test words
		for (String string : test) {
			// creat DP Map
			HashMap<FrequencyCounter, Solution> dpMap = new HashMap<FrequencyCounter, Solution>();
			dpMap.clear();

			// frequencyCounter for test word
			FrequencyCounter stringAvail = new FrequencyCounter();
			stringAvail = trie.stringToMap(string);
			FrequencyCounter avail = new FrequencyCounter();
			avail = trie.stringToMap(string);

			stringLength = string.length();

			// find all valid words in Trie
			char letters[] = string.toCharArray();
			List<String> result = new ArrayList<>();
			result = PrintAllWords(letters, root, letters.length, avail);

			// clean the words to reduce the amount
			List<String> list = new ArrayList<>();
			list = trie.matchesClean(result);

			// begin to find best combination
			trie.subsets(list, avail, dpMap);
			System.out.println("Test word: "+string);
			System.out.println("Max score: "+dpMap.get(stringAvail).score);
			System.out.println("Combination:");
			for (String string2 : dpMap.get(stringAvail).match) {
				List<String> matches = new ArrayList<>();
				matches = PrintAllWords(string2.toCharArray(), root, string2.length(), trie.stringToMap(string2));
				for (String match : matches) {
					if (match.length() == string2.length()) {
						System.out.println(match);
						break;
					}
				}
			}
			System.out.println();
		}
	}
}
