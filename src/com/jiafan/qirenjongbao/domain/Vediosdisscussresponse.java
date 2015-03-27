package com.jiafan.qirenjongbao.domain;

import java.io.Serializable;
import java.util.LinkedList;

public class Vediosdisscussresponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6234077091434783151L;
	private LinkedList<Vediodiscussbean>  results;
	public LinkedList<Vediodiscussbean> getResults() {
		return results;
	}
	public void setResults(LinkedList<Vediodiscussbean> results) {
		this.results = results;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Vediosdisscussresponse [results=" + results + "]";
	}
	
}
