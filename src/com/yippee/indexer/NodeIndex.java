package com.yippee.indexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yippee.db.indexer.model.Hit;

public class NodeIndex {
	 /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(NodeIndex.class);
	
	private HashMap<String, ArrayList<Hit>> wordIndex;
	int capacity = 10000;
	
	
	public NodeIndex() {
		wordIndex = new HashMap<String, ArrayList<Hit>>();
	}
	
	/**
	 * adds all hits from a document into wordIndex
	 * if word isnt there, make new entry. otherwise append the Hit
	 * 
	 * @param hits
	 */
	public synchronized void addAllHits(ArrayList<Hit> hits) {
		for(int i = 0; i<hits.size(); i++){
			Hit hit = hits.get(i);
			String word = hit.getWord();
			ArrayList<Hit> list;
			if(wordIndex.containsKey(word)){
				list = wordIndex.get(word);			
			} else {
				list = new ArrayList<Hit>();
			}
			list.add(hit);
			wordIndex.put(word, list);
			
			if (wordIndex.size() > capacity) {
				sendWordsToRing();
				new HashMap<String, ArrayList<Hit>>();
			}
		}
		
	}
	
	public synchronized ArrayList<Hit> getHitList(String word){
		return wordIndex.get(word);
	}

	public synchronized void printIndex(){
		Set<String> keys = wordIndex.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			String word = iter.next();
//			System.out.println("[" + word + "=" + wordIndex.get(word).size() + "]");
			logger.info("[" + word.hashCode() + "=" + wordIndex.get(word).size() + "]");
		}
		
	}
	
	public synchronized void sendWordsToRing() {
		// HASH keys
		
		// Send keys
		printIndex();		
	}
	
}