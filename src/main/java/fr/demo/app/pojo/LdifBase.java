package fr.demo.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LdifBase implements Serializable {

	private static final long serialVersionUID = 8342890304160103650L;

	private Collection<String> lines;
	private Map<String, List<Integer>> tokenAndIndexLineWithCpt;

	public LdifBase() {
		super();
		lines = new ArrayList<>();
		tokenAndIndexLineWithCpt = new HashMap<String,  List<Integer>>();
	}

	public Collection<String> getLines() {
		return lines;
	}

	public void setLines(Collection<String> lines) {
		this.lines = lines;
	}

	public Map<String, List<Integer>> getTokenAndIndexLineWithCpt() {
		return tokenAndIndexLineWithCpt;
	}

	public void setTokenAndIndexLineWithCpt(Map<String, List<Integer>> tokenAndIndexLineWithCpt) {
		this.tokenAndIndexLineWithCpt = tokenAndIndexLineWithCpt;
	}

}
