package huffman;
/*												HuffmanCoding - Implementation of Lossless Compression Datastructure.
 *@author: Ajay Badrinath
 *@date  : 05-05-2023
 *@version:1.1.0
					Version Changelog:
							1.Yet to comment
							2.Done Testing .
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
class Node{
	String key;
	int value;
	Node left,right;
	Node(String key,int value){
		this.key=key;
		this.value=value;
		right=null;
		left=null;
	}
}
class Binarytree{
	private final int cnt=20;//change it to 20 for a better look
	
	Node insert(Node tmp,String key,int value) {
		if(tmp==null) {
			tmp=new Node(key,value);
		}
		if(value<tmp.value) {
			tmp.left=insert(tmp.left,key,value);
		}
		else if(value>tmp.value) {
			tmp.right=insert(tmp.right,key,value);
		}
		return tmp;
	}
	String x="";
	void print(Node root,int space)  {
		if(root==null) {
			return;
		}
		space+=cnt;
		print(root.right,space);
		for(int i=cnt;i<space;i++) {
			System.out.print(" ");
			x+=" ";
		}
		System.out.println(root.key+root.value);
		x+=(root.key+root.value);
		x+="\n";
		print(root.left,space);
	}
	String c="";
	
	void  printLeafNodes(Node root,String tgt,String dummy) {
		if(root==null) {
			return ;
		}
		
		
		if((root.left==null) &&(root.right==null)&& root.key.equals(tgt)) {
		
			c=dummy;
			
			return ;
		}
		
		printLeafNodes(root.left,tgt,dummy+'0');
		
		printLeafNodes(root.right,tgt,dummy+'1');
		
	}
	void FindMyChild(Node Root,Node child) {
		if(Root==null) {
			return;
		}
		if(Root.key==child.key) {
			
			Root.left=child.left;
			Root.right=child.right;
			return ;
			
		}
		FindMyChild(Root.left,child);
		FindMyChild(Root.right,child);
	}
	Node ConstructTree(Node[] arr,int length) {
		Node root=null;
		//System.out.println(arr[length-2].key);
		root=arr[length];
		for(int i=length;i>=0;i--) {
			FindMyChild(root,arr[i]);
		}
		//System.out.println(root.left.key);
		return root;
	}
	Node[] MakeTree(List<Entry<String,String>>k1,GenerateFrequency f1) {
		Node [] slots=new Node[250];
		ArrayList <Entry<String,String>> k=new ArrayList<>();
		Node tmp;
		Entry<String,Integer>k2;
		int track=0;
		 for(int i=0;i<25;i++) {
			 slots[i]=null;
			
		 }
		 do{		
				try {
					slots[track]=f1.CombineStringAndPush(0, 1);
				
				track++;
				}catch(Exception e) {
					continue;
				}
			}while(f1.ls.size()!=1);
			for(int i=0;i<track;i++) {
				System.out.print(slots[i].key+"\t");
			}
			
			
		
		return slots;
	}
	List<Entry<String, String>> PopulateMapTable(List<Entry<String, String>> k1,Node root) {
		Entry<String,String>k3;
		
		List<Entry<String, String>> k=new ArrayList<>();
		
		String dummy="";
		
		for(int i=0;i<k1.size();i++) {
			
			
			printLeafNodes(root,k1.get(i).getKey(),dummy);
			
			k3=Map.entry(k1.get(i).getKey(), c);
			
			k.add(k3);
			dummy="";
		}
		
		return k;
	}
	String Encode(String seq,List<Entry<String, String>> k1) {
		String EsH="";
		String k="";
		for(int i=0;i<seq.length();i++) {
			for(int j=0;j<k1.size();j++) {
				k+=seq.charAt(i);
				if(k.equals(k1.get(j).getKey())) {
					
					EsH+=k1.get(j).getValue();
				}
				k="";
			}
		}
		
		return EsH;
	}
	List<Entry<String,String>> PadDictionaryWithZero(List<Entry<String,String>>k1)
	{
		int max=0,diff;
		Entry<String,String>k3;
		String toAdd="";
		for(int i=0;i<k1.size();i++) {
			if(k1.get(i).getValue().length()>max) {
				max=k1.get(i).getValue().length();
			}
		}
		for(int i=0;i<k1.size();i++) {
			if(k1.get(i).getValue().length()<max) {
				diff=max-k1.get(i).getValue().length();
				for(int j=0;j<diff;j++) {
					toAdd+='0';		
				}
				k3=Map.entry(k1.get(i).getKey(), k1.get(i).getValue()+toAdd);
				k1.set(i, k3);
				toAdd="";
			}
		}
		return k1;
	}
	List<Entry<String,String>> PadDictionaryWithhexZero(List<Entry<String,String>>k1)
	{
		int max=0,diff;
		Entry<String,String>k3;
		String toAdd="";
		for(int i=0;i<k1.size();i++) {
			if(k1.get(i).getValue().length()>max) {
				max=k1.get(i).getValue().length();
			}
		}
		for(int i=0;i<k1.size();i++) {
			if(k1.get(i).getValue().length()<max) {
				diff=max-k1.get(i).getValue().length();
				for(int j=0;j<diff;j++) {
					toAdd+='0';		
				}
				k3=Map.entry(k1.get(i).getKey(), toAdd+k1.get(i).getValue());
				k1.set(i, k3);
				toAdd="";
			}
		}
		return k1;
	}
	
	String decode(List<Entry<String,String>>k2,String BinSeq) {
		int len =k2.get(0).getValue().length();
		String decoded="";
		for(int i=0;i<BinSeq.length();i+=len) {
			for(int j=0;j<k2.size();j++) {
				
				if(BinSeq.subSequence(i, i+len).equals(k2.get(j).getValue())) {
					decoded+=k2.get(j).getKey();
				}
				
			}
		}
		System.out.println(decoded);
		return decoded;
		
	}
}
 class  GenerateFrequency {
	  static String sequence="";
	 Map <String,Integer> freq=new HashMap<String,Integer>();
	 List<Map.Entry<String, Integer>> ls;
	GenerateFrequency(String sequence){
		GenerateFrequency.sequence=sequence;
		
	}
	 void getCount(String s) {
		int s1=0;
		
		String dummy="";
		for(int i=0;i<s.length();i++) {
			for(int j=0;j<sequence.length();j++) {
				if(s.charAt(i)==sequence.charAt(j)) {
					s1++;
				}
			}
			
			dummy+=s.charAt(i);
			freq.put(dummy, s1);
			dummy="";
			s1=0;
		}
	}
	 String GenerateDistinctChar() {
		 return GenerateFrequency.sequence.chars().distinct().collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	 }
	void sortHashMap(Map<String,Integer> hmp) {
		Comparator<Map.Entry<String, Integer>> b= (o,o1)->o.getValue().compareTo(o1.getValue());
		ls=new ArrayList<>(hmp.entrySet());
		Collections.sort(ls,b);
		System.out.println(ls);
		
		System.out.println();
	}
	Node  CombineStringAndPush(int idx,int idx1) {
		Entry<String,Integer>k=ls.get(idx);
		Entry<String,Integer>k1=ls.get(idx1);
		String concat=k.getKey()+k1.getKey();
		Integer Score =k.getValue()+k1.getValue();
		Entry<String ,Integer >k3=Map.entry(concat,Score);
		Binarytree s=new Binarytree();
		
		
		ls.add(k3);
		Node s1=s.insert(null, concat, Score);
		Node s2=s.insert(null,ls.get(idx1).getKey(),ls.get(idx1).getValue());
		Node s3=s.insert(null,ls.get(idx).getKey(),ls.get(idx).getValue() );
		if (s2.value<s3.value) {
			s1.left=s2;
			s1.right=s3;
		}
		else {
			s1.right=s2;
			s1.left=s3;
		}
		ls.remove(idx1);
		ls.remove(idx);
	    Comparator<Map.Entry<String, Integer>> b= (o,o1)->o.getValue().compareTo(o1.getValue());
		Collections.sort(ls,b);
		return s1;
	}
	
	
}

class init {
	public String encoded="";
	public List<Entry<String, String>> header;
	public void run() {
		//GenerateFrequency f1=new GenerateFrequency("A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED");
		//GenerateFrequency f1=new GenerateFrequency("BCCABBDDAECCBBAEDDCC");
		//String istream=GenerateFrequency.sequence.chars().distinct().collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		//GenerateFrequency f1=new GenerateFrequency("Hello world Hi i have sent the sample sms . What  work is left and is yet to be finished?");
		Charset utf8=Charset.forName("UTF-8");
		Charset def_char_set=Charset.defaultCharset();
		byte [] b=new byte[100];
		String tg="";
		try {
		//FileInputStream f2=new FileInputStream("E:\\Airtel-email-sms.txt");
		FileInputStream f2=new FileInputStream("C:\\Users\\Admin\\Downloads\\output.txt");
		byte [] arr=f2.readAllBytes();
		
		 tg=new String(arr,def_char_set.name());
		 new PrintStream(System.out,true,utf8.name()).println(tg);
		}catch(Exception e) {
			System.out.println("Error");
			return;
		}
		GenerateFrequency f1=new GenerateFrequency(tg);		
		f1.getCount(f1.GenerateDistinctChar());
		try {
		PrintStream p1=new PrintStream(System.out,true,utf8.name());
		//System.out.println(f1.freq.keySet());
		//System.out.println(f1.freq.values());
		//SortedSet val=new TreeSet<>(f1.freq.values());
		Binarytree s=new Binarytree();
		f1.sortHashMap(f1.freq);
		List<Entry<String, String>> dup=new ArrayList<>();
		Entry<String,String>k3,k4;
		for(int i=0;i<f1.ls.size();i++) {
			k3=Map.entry(f1.ls.get(i).getKey(),"");
			dup.add(k3);
		}
		Node slot []=s.MakeTree(dup,f1);
		int t=0;
		for(int i=0;i<slot.length;i++) {
			if(slot[i]==null) {
				break;
			}
			t++;
		}
		t--;
		Node root1=s.ConstructTree(slot, t);
		//System.out.println("\n"+t);
		p1.println("\n"+t);
		FileOutputStream f;
		try {
			f = new FileOutputStream("E:\\eclipse\\SMS_TEXT_LMS\\src\\huffman\\tree.txt");
			s.print(root1, 10);
			String k=s.x;
			
			f.write(k.getBytes());
			f.flush();
			f.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String k="";
		//s.printLeafNodes(root1, "C", k);
		System.out.println(s.c
				);
		
		//System.out.println(dup);
		List<Entry<String, String>> lsk=s.PopulateMapTable(dup, root1);
		System.out.println(lsk);
		lsk=s.PadDictionaryWithZero(lsk);
		header=lsk;
		System.out.println(lsk);
		//System.out.println(Integer.toHexString(Integer.parseInt("111011000000000", 2)));
		Entry<String, String> what;
		List<Entry<String,String>> head =new ArrayList<>();
		for(int i=0;i<lsk.size();i++) {
			//lo.
			what=Map.entry(lsk.get(i).getKey(), Integer.toHexString(Integer.parseInt(lsk.get(i).getValue(),2)));
			head.add(what);
			//System.out.println(lsk.get(i).getKey()+"= "+"0x"+Integer.toHexString(Integer.parseInt(lsk.get(i).getValue(), 2)));
		}
		System.out.println(head);
		List<Entry<String,String>> head1=s.PadDictionaryWithhexZero(head);
		//System.out.println(head1);
		//System.out.println( s.Encode("hi there", head1));
		//System.out.println(s.decode(head1, s.Encode("hi there", head1)));
		//System.out.println("Charecter bitmapping");
		//System.out.println(lsk);
		k=s.Encode(tg, lsk);
		//System.out.println("Encoded String:");
		//System.out.println(k);
		encoded=k;
		//System.out.println("--------------"+encoded.getBytes().length);
		//System.out.println("Decoded String:");//remove this comment aftr tst
		//System.out.println(s.decode(lsk, k));//
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
public class huffmanCoding {

		public static void main(String []args) {
			
			new init().run();
			// Tamil Demo;
			Charset utf8=Charset.forName("UTF-8");
			Charset def_char_set=Charset.defaultCharset();
			//String charp="\u0B85";
			//String charp="hi there";
			byte [] b=new byte[100];
			
			try {
				FileInputStream k1=new FileInputStream("E:\\tamildemo.txt");
				byte []ak=k1.readAllBytes();
				//b=charp.getBytes("UTF-8");
				b=ak;
				String n=new String(b,def_char_set.name());
				new PrintStream(System.out,true,utf8.name()).println(n);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			
		}

		
}
