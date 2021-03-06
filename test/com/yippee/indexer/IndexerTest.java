package com.yippee.indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.util.Configuration;

import static org.junit.Assert.*;

public class IndexerTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(IndexerTest.class);
	
//	 Threading tester
	public static void main(String[] args) {
		// Start putting test documents into the queues
		DocCreator doccreate = new DocCreator();
		doccreate.start();
		
		Indexer indexer = new Indexer();
		indexer.makeThreads();
		
	}
}

class DocCreator extends Thread {	
	
    String testHTML = "<HTML><HEAD><TITLE>CSE455/CIS555 HW2 Grading Data</TITLE></HEAD>" +
            "<H3>XML to be crawled</H3>" +
            "<UL>" +
            "<LI><A HREF=\"rss/cnnp.xml\"><B>CNN's politics - MATCHED</A></B></LI>" +
            "<LI><I><A HREF=\"rss/cnnt.xml\">CNN top stories - MATCHED</A></I></LI>" +
            "<LI><A HREF=\"rss/cnnl.xml\">CNN Laws - N-O-T MATCHED</A></LI>" +
            "</UL>" +
            "<H3>Other XML data</H3>" +
            "<UL>" +
            "<LI><A HREF=\"restrict/frontpage.xml\">BBC frontpage - restricted</A></LI>" +
            "<LI><A HREF=\"eurofxref-hist.xml\">Historical Euro exchange rate data - too large</A></LI>" +

            // these will probably fail (level 3)
            "      <li><a href=\"/~nvas/something/Africa.html\">Africa</a></li>\n" +
            "      <li><a href=\"./Americas.html\">Americas</a></li>\n" +
            "      <li><a href=\"../AsiaPacific.html\">Asia</a></li>\n" +

            //these should pass, are nothing special (level 0)
            "<li><a href=\"http://d.o.t.y/~nvas/something/Africa.html\">dots</a></li>\n" +
            "<li><a href=\"http://domain:8080/./Americas.html\">port</a></li>\n" +
            "<li><a href=\"http://www.seas.upenn/../AsiaPacific.html\">..</a></li>\n" +

            "<li><a href=\"http://we.com/index.php\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/index\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/index/\">domain</a></li>\n" +
            "<li><a href=\"http://we.com\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/\">domain</a></li>\n" +

            // these are a bit advanced (level 2) -- these should have different semantics depending whether we are in a dir or page!
            "      <li><a href=\"nothingSpecial/./ea.html\">Business</a></li>\n" +
            "      <li><a href=\"nothingSpecial/\">Europe</a></li>\n" +
            "      <li><a href=\"nothingSpecial\">Front Page</a></li>\n" +
            "      <li><a href=\"nothingSpecial/whatevah/../ea.html\">Middle East</a></li>\n" +

            "      <li><a href=\"?who=me\">params</a></li> " +
            "      <li><a href=\"#tag\">tags</a></li>" +
            "</UL>" +
            "<H3>NON XML files</H3>" +
            "<UL>" +
            "<B><LI><A HREF=\"1.txt\">1.txt</A></LI></B>" +
            "<LI><A HREF=\"2.png\">2.png</A></LI>" +
            "</UL>" +
            "</BODY></HTML>";
	
	DocAugManager dam;
	int counter;
	
	public void connect(){
		try {
			URL u = new URL("http://www.princeton.edu/main#content");
			URLConnection conn = u.openConnection();
			conn.getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer buff = new StringBuffer();
			while ((inputLine = in.readLine()) != null) 
				buff.append(inputLine);
			in.close();
			testHTML = new String(buff);
		} catch (MalformedURLException e) {
			System.out.println("malformed url...really?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		Configuration.getInstance().setBerkeleyDBRoot("db/test");
		
		dam = new DocAugManager();
		counter = 0;
		connect();
		while (true) {
			
			DocAug doc = new DocAug();
			
			doc.setId(String.valueOf(counter) + "ID");
			doc.setUrl(String.valueOf(counter) + ".com");
			doc.setDoc(testHTML);
			
			System.out.println("Creating doc: " + counter);
						
			dam.create(doc);
			
			counter++;

			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			DocAug newDoc = dam.poll();
//			
//			System.out.println("Retrieved ID: " + newDoc.getId());
//			System.out.println("Retrieved URL: " + newDoc.getUrl());
//			
//			System.out.println(dam.peek());
			
		}
		
		
	}
	
	@Test
	public void testIndexer() {
		fail("not yet implemented");
	}
}
