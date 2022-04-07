package fr.cnp.sec.pojo;

import java.util.List;

public class User {

	private String header;
	private List<String> lines;
	private long number;

	public User() {
		super();
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

}
