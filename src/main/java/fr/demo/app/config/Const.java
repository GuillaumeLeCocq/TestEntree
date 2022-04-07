package fr.cnp.sec.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Const {

	// For now Unique TOKEN :
	public static final String TOKEN_CPT = "${COMPTEUR_NNNN}";
	public static final String TOKEN_NOW = "${DATETIME_NOW}";
	
	public static final List<String> list = Collections.unmodifiableList(
		    new ArrayList<String>() {
				private static final long serialVersionUID = 2598499902136001625L;

			{
		        add(TOKEN_CPT);
		        add(TOKEN_NOW);
		    }});
	
	public static final String KEY_LdifBase = "LdifBase";
}
