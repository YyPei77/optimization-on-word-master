package assessment;

import java.util.HashMap;
import java.util.Map;

public class FrequencyCounter implements Cloneable {
	HashMap<Character, Integer> stringAvail = new HashMap<Character, Integer>();

	@Override
	public int hashCode() {
		int result = 13;
		for (Map.Entry<Character, Integer> entry : this.stringAvail.entrySet()) {
			int char2Int = entry.getKey() - 'a';
			result += 31 * char2Int + 23 * entry.getValue();
		}
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (this.getClass() != object.getClass())
			return false;
		FrequencyCounter fc = (FrequencyCounter) object;
		return this.stringAvail.equals(fc.stringAvail);
	}

	@Override
	public String toString() {
		String string = "";
		for (Map.Entry<Character, Integer> entry : this.stringAvail.entrySet()) {
			string = string + entry.getKey();
			string = string + " ";
			string = string + entry.getValue();
		}
		return string;
	}

	public boolean allZero() {
		for (Map.Entry<Character, Integer> entry : this.stringAvail.entrySet()) {
			if (entry.getValue() != 0)
				return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FrequencyCounter clone() {
		FrequencyCounter cpy = new FrequencyCounter();
		cpy.stringAvail = (HashMap<Character, Integer>) this.stringAvail.clone();
		return cpy;
	}

}