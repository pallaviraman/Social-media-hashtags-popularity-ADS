/** 
 * This file contains the input and output handling and storing the key,node values in a hashtable.
 *  A hashmap is used to store the deleted max values for reinsertion into the Fibonacci heap.
 * @author Pallavi
 *
 */
//package hashtagcounter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.HashMap;
/**
 * Implementation for reading input from a file and writing output to a file.
 * Implementation of storing key,node pairs in the hashtable where key is the string whose frequency is given
 * and node points to the Fibonacci node containing the frequency of occurrence of the key.
 * Contains the Main function(the starting point of the program).
 * @author Pallavi
 *
 */
public class hashtagcounter {
	/**
	 * takes input file from console as input and save the output in output_file.txt
	 * Entry point of the program and does the groundwork.
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			/** the key whose occurrence is saved in a node in Fibonacci heap */
			String hashtag;
			/** the frequency of occurrence of the hashtag */
			int hashkey;
			/** Instance of Fibonacciheap*/
			FibonacciHeap FbHeap = new FibonacciHeap();
			/** Hashtable to store key,node pairs */
			Hashtable<String,FibonacciHeapNode> FbHashTable = new Hashtable<String,FibonacciHeapNode>();
			/** buffer to store the output */
			StringBuffer answerBuffer = new StringBuffer();
			String filename = args[0];
			Scanner sc = new Scanner(new FileReader(filename));
			File file = new File("output_file.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			while(sc.hasNext())
			{
				String check_ip = sc.next();
				if(!((check_ip.equals("STOP")) || (check_ip.equals("stop"))))
				{
					if(check_ip.startsWith("#"))
					{
						hashtag = check_ip.substring(check_ip.indexOf("#") + 1);
						hashkey = Integer.parseInt(sc.next());
						if(FbHashTable.containsKey(hashtag))
						{
							/** Increasing the frequency value of an existing node */
							FbHeap.IncreaseKey(FbHashTable.get(hashtag),hashkey);	
						}
						
						else
						{
							FibonacciHeapNode hashnode;
							hashnode = FbHeap.Insert(hashkey);
							/** Inserting the newly formed key,node pairs in hashtable */
							FbHashTable.put(hashtag,hashnode);
						}
					}
					else
					{
						HashMap<String,Integer> maxObj = new HashMap<String,Integer>();
						answerBuffer.setLength(0);
						/** removing the nodes having the maximum value of frequency and storing them in a hashmap */
						for(int itr = 1; itr<=Integer.parseInt(check_ip);itr++)
						{
							FibonacciHeapNode maxnode;
							maxnode = FbHeap.removeMax();
							if(maxnode != null)
							{
								for(Entry<String,FibonacciHeapNode> entry: FbHashTable.entrySet())
								{
									if(maxnode.equals(entry.getValue()))
									{
										answerBuffer.append(entry.getKey());
										answerBuffer.append(",");
										maxObj.put(entry.getKey(),maxnode.frequency);
										FbHashTable.remove(entry.getKey());
										break;
									}
								}
							}
						}
						/** Reinserting the removed max nodes for next iteration */
						for(Entry<String,Integer> entry: maxObj.entrySet())
						{
							String key = entry.getKey();
							int freq = entry.getValue();
							FibonacciHeapNode reinsertnode = FbHeap.Insert(freq);
							FbHashTable.put(key, reinsertnode);
						}
						if (answerBuffer.length() > 0) 
						{
							answerBuffer.setLength(answerBuffer.length() - 1);
						}
						/** setting the writing of output to a file */
						System.setOut(ps);
						System.out.println(answerBuffer);
					}	
				}
				if(sc.hasNextLine())
				{
					sc.nextLine();
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Input file not found" +e);
			e.printStackTrace();
		}

	}

}

