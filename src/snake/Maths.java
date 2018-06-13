package snake;

public class Maths {
	public static double format(String s) {
		s = s.replace(" ", "");
		s = s.replace("\t", "");
		char[] c = s.toCharArray();
		if(s.contains("(")) {
			int openbrackets = 0;
			for (int i = 0; i < s.length(); i++) {
				if (c[i] == '(') openbrackets++;
				else if (c[i] == ')') {
					openbrackets--;
					if(openbrackets == 0) {
						s = s.replace(s.substring(s.indexOf('('), i+1), ""+(format(s.substring(s.indexOf('(')+1, i))));
						break;
					}
				}
			}
		}
		if (s.contains("(")) s = "" + format(s);
		c = s.toCharArray();
		for(int i = c.length-1; i > 0; i--) {
			if(c[i] == '+') {
				return format(s.substring(0, i)) + format(s.substring(i+1, s.length()));
			} else if(c[i] == '-') {
				return format(s.substring(0, i)) - format(s.substring(i+1, s.length()));
			}
		}
		for(int i = s.length()-1; i > 0; i--) {
			if(c[i] == '*') {
				return format(s.substring(0, i)) * Double.parseDouble(s.substring(i+1, s.length()));
			} else if (c[i] == '/') {
				return format(s.substring(0, i)) / Double.parseDouble(s.substring(i+1, s.length()));
			}
		}
		return s.equals("") ? 0 : Double.parseDouble(s);
	}
}
