package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


import com.google.common.hash.HashCode;
//import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
public class Client {
	 static SortedMap<Integer, String> circle =new TreeMap<Integer, String>();	
private static String server1="http://localhost:3000";
private static  String server2="http://localhost:3001";
private static String server3="http://localhost:3002";
static HashFunction hf = Hashing.md5();
//private Hashing hashing;
static char values[]={'a','b','c','d','e','f','g','h','i','j'};

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        List<String> servers = new ArrayList<String>();
        servers.add(server1);
        servers.add(server2);
        servers.add(server3);
        for(int i=0;i<servers.size();i++){
        	add(servers.get(i),i);
        }
        
        for(int i=0;i<10;i++){
        	//System.out.println(servers.size());
        int bucket=  Hashing.consistentHash(Hashing.md5().hashLong(i), circle.size());
        String Cserver=get(bucket);
        System.out.println("going to server" + Cserver);
       // System.out.println("removing server"+ servers.remove(server1));
        CacheServiceInterface cache = new DistributedCacheService(Cserver);
        System.out.println("putting the values in the server"+Cserver);
        cache.put(i+1, String.valueOf(values[i]));
        System.out.println("getting the values from the server"+Cserver);
        cache.get(i+1);
        System.out.println("Existing Cache Client...");
        //servers.remove(bucket);
        }
       
    }
    
   
    public static void add(String node,int i) {
    	HashCode hc=hf.hashLong(i);
    	//System.out.println(hc.asInt()+"hashcode value");
    	circle.put(hc.asInt(), node);
      }

      public static void remove(String node) {
       
          circle.remove(hf.hashCode());
        
      }

      public static String get(Object key) {
    	  
        if (circle.isEmpty()) {
          return null;
        }
        int hash = hf.hashLong( (Integer)key).asInt();
        if (!circle.containsKey(hash)) {
          SortedMap<Integer,String> tailMap =circle.tailMap(hash);
          hash = tailMap.isEmpty() ?
                 circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
      } 
     

}
