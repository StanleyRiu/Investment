package market.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileHandler {
	protected String fileName = null;
	protected FileInputStream fis = null;
	protected InputStreamReader isr = null;
	protected BufferedReader br = null;
	
	public FileHandler(String fileName) {
		super();
		this.fileName = fileName;
		try {
			fis = new FileInputStream(this.fileName);
			isr = new InputStreamReader(fis, "big5");
			br = new BufferedReader(isr);
		} catch(FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
