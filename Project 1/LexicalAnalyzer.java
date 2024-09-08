import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Users/johnpuri33/Desktop/Towson /Spring 2023/COSC 455/Project 1/input.rtf

public class LexicalAnalyzer {

	String file;
	
	static String line;
	static int lineNumber;
	static int position = 1;
	static String curr = "";
	static Lexeme currLex;
	static boolean done = false;
	boolean nonsense = true;
	public static String[] keyWords = new String[] { "program", "print", "and", "or", "bool", "int", "if", "then", "fi",
			"while", "do", "od", "false", "true", "else", "not" };
	static List<String> kw = new ArrayList<>(Arrays.asList(keyWords));
	static ArrayList<Lexeme> symbolTable = new ArrayList<>();
	private static BufferedReader fis = null;

	//main method
	public static void main(String[] args) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			String pathway;
			System.out.println("Enter pathway to File: ");
			pathway = input.readLine();
			//File file = new File(pathway);
			File file = new File("Users/johnpuri33/Desktop/Towson/Spring 2023/COSC 455/Project 1/input.txt");

			FileReader fr = new FileReader(file);
			fis = new BufferedReader(fr);
			nextLine();

			Lexeme current = next();
			while (kind() != "end-of-text") {
				print(position(), kind(), value());
				current = next();
			}
			print(position(), kind(), value());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	// /Users/calebwossen/test.txt
	// /Users/calebwossen/Downloads/Examples/

	//Returns the next Lexeme
	public static Lexeme next() {
		if (position == line.length()) {
			nextLine();
			if (done) {
				currLex = new Lexeme("end-of-text", lineNumber, position, "");
				symbolTable.add(currLex);
				return currLex;
			}
		}

		char c = line.charAt(0);
		if (position == 0) {
			c = line.charAt(0);
		} else {
			c = line.charAt(position);
		}

		while (c == ' ') {
			position++;
			c = line.charAt(position);

		}

		if (c == ':') {
			if ((position + 1 < line.length()) && (line.charAt(position + 1) == '=')) {
				currLex = new Lexeme(":=", lineNumber, position, "");
				position++;
				position++;
			} else {
				currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
				position++;
			}
		} else if (c == '<') {
			currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
			position++;
		} else if (c == '=') {
			if ((position + 1 < line.length()) && (line.charAt(position + 1) == '<')) {
				currLex = new Lexeme("=<", lineNumber, position, "");
				position++;
				position++;

			} else {
				currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
				position++;
			}
		} else if (c == '!') {
			if ((position + 1 < line.length()) && (line.charAt(position + 1) == '=')) {
				currLex = new Lexeme("!=", lineNumber, position, "");
				position++;
				position++;

			} else {
				System.out.println((lineNumber) + " : " + (position + 1) + ">>>>> Illegal character '" + c + "'");
				System.exit(0);
			}

		} else if (c == '>') {
			if ((position + 1 < line.length()) && (line.charAt(position + 1) == '=')) {
				currLex = new Lexeme(">=", lineNumber, position, "");
				position++;
				position++;

			} else {
				currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
				position++;
			}

		} else if (c == ';') {
			currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
			position++;

		}

		else if (c == '*') {
			currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
			position++;

		} else if (c == '/') {
			if ((position + 1 < line.length()) && (line.charAt(position + 1) == '/')) {
				nextLine();
				if (done) {
					currLex = new Lexeme("end-of-text", lineNumber, position, "");
					return currLex;
				}
				next();

			} else {
				currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
				position++;
			}
		} else if (c == '+') {
			currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
			position++;
		} else if (c == '-') {
			currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
			position++;
		} else if (c == '(') {
			currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
			position++;
		} else if (c == ')') {
			currLex = new Lexeme(Character.toString(c), lineNumber, position, "");
			position++;
		} else if (c >= '0' && c <= '9') {
			int temp = position;
			while (c >= '0' && c <= '9') {
				curr = curr + c;
				position++;
				if (position < line.length())
					c = line.charAt(position);
				else
					break;
				currLex = new Lexeme("NUM", lineNumber, temp, curr);

			}
			currLex = new Lexeme("NUM", lineNumber, temp, curr);
		}
		else if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '_') || (c >= '0' && c <= '9')) {
			int temp = position;
			while ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '_') || (c >= '0' && c <= '9')) {
				curr = curr + c;
				position++;
				if (position < line.length())
					c = line.charAt(position);
				else
					break;
			}
			
			if (kw.contains(curr)) {
				currLex = new Lexeme(curr, lineNumber, temp, "");
			} else {
				currLex = new Lexeme("ID", lineNumber, temp, curr);
			}

		} else {
			System.out.println((lineNumber) + " : " + (position + 1) + ">>>>> Illegal character '" + c + "'");
			System.exit(0);

		}
		curr = "";
		symbolTable.add(currLex);
		return currLex;
	}

	//Allows the next() function to move to the next line of the file
	public static void nextLine() {
		try {
			line = fis.readLine();
			if (line.isEmpty()) {
				nextLine();
			}
			lineNumber++;
			position = 0;

		} catch (Exception e) {
			done = true;
			return;
		}//end of catch 
	}

	//Prints the position, kind, and value of a Lexeme
	public static void print(String p, String k, String v) {

		System.out.println(p + " : '" + k + "'" + "  " + v);

	}

	//returns the kind of lexeme
	public static String kind() {
		return currLex.kind;
	}

	//returns the value of a lexeme
	public static String value() {
		return currLex.value;
	}

	//returns the position of a lexeme
	public static String position() {
		return (currLex.lineNumber) + " : " + (currLex.position + 1);
	}
}

// This is an object used to represent a symbol
class Lexeme {
	String kind;
	int lineNumber;
	int position;
	String value;

	Lexeme(String k, int ln, int pos, String val) {
		this.kind = k;
		lineNumber = ln;
		position = pos;
		value = val;
	}
}