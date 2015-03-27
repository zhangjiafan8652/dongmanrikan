package com.jiafan.qirenjongbao.domain;

import java.io.Serializable;
import java.util.LinkedList;


public class Newsdisscussresponse  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6593195040425449260L;
	private LinkedList<Newsdiscussbean>  results;
	public LinkedList<Newsdiscussbean> getResults() {
		return results;
	}
	public void setResults(LinkedList<Newsdiscussbean> results) {
		this.results = results;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Newsdisscussresponse [results=" + results + "]";
	}



	
	

}
