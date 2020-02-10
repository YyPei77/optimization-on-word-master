package assessment;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	Integer score;
	List<String> match;
	
	Solution(Integer score, List<String> match) {
		this.score = score;
		this.match = new ArrayList<String>();
		for (String string : match) {
			this.match.add(string);
		}
	}
}
