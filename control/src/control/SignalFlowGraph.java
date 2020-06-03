package control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SignalFlowGraph {
	public static boolean isNumeric(String str) {
		  return str.matches("-?\\d+(\\.\\d+)?");  
		}
	public static boolean nonTouched (String str1, String str2) {
			for (int i =0;i<str2.length();i++) {
				if (str1.contains(str2.subSequence(i, i+1))) {
					return false;
				}				
			}
			return true;	
	}
	
		
	public static String with_values (String str,Map edges_values, int is_num) {
		String pathWithValues="";
		double value=1;
		for (int i=0;i<str.length()-1;i++) {
			String edge =str.substring(i,i+2);
			if (is_num==1) {
				value =value * Double.parseDouble((String) edges_values.get(edge));
				pathWithValues = String.valueOf(value);
			}else {
			pathWithValues = pathWithValues+edges_values.get(edge);
			if (i!=str.length()-2) {
				pathWithValues = pathWithValues+"*";
			}
			}
		}
			return pathWithValues;	
	}
	
	
	public static boolean passEdgeOnce  (String str) {
		for (int i=0;i<str.length();i++) {
			String temp =str;
			str = str.substring(0, str.length()-1);
			if ( str.contains(temp.substring(temp.length()-2,temp.length()))) {
				return false;
			}
		}
		return true;		
	}
	
	
	public static ArrayList<String> loops( ArrayList<String> strings,ArrayList<ArrayList<Integer>> out,int index,int start,String loop,  Map map) {
		if (loop.length()>1&&loop.charAt(loop.length()-1)==String.valueOf(start).charAt(0)) {
			if (!strings.contains(loop)&&passEdgeOnce  (loop)) {
				strings.add(loop);

			for (int i =1; i <loop.length()-1;i++) {
				 String l = loop.substring(i,i+1);
				 map.put(loop.substring(i,i+1), loop.substring(i,loop.length()));
			 }
			}
			return strings;
		}
		int temp_index = 0,find=0;
		//		for (int i =start;i<out.size();i++) {
			for (int j=0;j<out.get(index).size();j++) {	
				
				if (out.get(index).contains(start)) {
					out.get(index).remove((Object)start);
					index = start;           ///hbdd
					temp_index = start;		
					j--;
				}else {
			//		index = out.get(index).get(j);
					temp_index =  out.get(index).get(j);
			//	temp_index= index;
				}
				
				if (loop.contains(String.valueOf(temp_index))/*&&!map.containsKey(String.valueOf(temp_index))*/&& (start!=temp_index)) {
					continue;
					//return strings;
					} 
				
				if (map.containsKey(String.valueOf(temp_index))&&(!loop.contains(String.valueOf(temp_index)))) {      //        kanet index
			//		loop = loop+map.get(String.valueOf(temp_index));
					String temp_loop = loop+map.get(String.valueOf(temp_index));
					if (!strings.contains(temp_loop)) {
						loop = temp_loop;
					}else {
						continue;  ///hbddd
					//	return strings; /////////hbd
					}
					find=1;
				}
				
				
			/*	if (loop.contains(String.valueOf(temp_index))) {
					continue;
				}*/
				if (find==0) {
					//String last_two  = loop.charAt(loop.length()-1)+String.valueOf(temp_index);
					if (!loop.contains(String.valueOf(temp_index))||(start==temp_index)) {
					loop = loop +  String.valueOf(temp_index);
					if (start!=temp_index) {
						index = temp_index;           ////hbddd
					}
					}
				}
				
					loops(strings,out, index,start,loop,map);
					 if (loop.charAt(loop.length()-1)==String.valueOf(start).charAt(0)) {
							loop = loop.substring(0,loop.indexOf(String.valueOf(index))+1);
						}
			}
	
		return strings;		
	}
	

	public static ArrayList<String> forwardPath( ArrayList<String> strings,ArrayList<ArrayList<Integer>> out,int start,String path, Map map) {
		if (path.contains( String.valueOf(out.size()-1))) {
			if (!strings.contains(path))
			strings.add(path);
			
			for (int i =1; i <path.length()-1;i++) {
				 String l = path.substring(i,i+1);
				 map.put(path.substring(i,i+1), path.substring(i,path.length()));
			 }
			return strings;
		}
		int k;
			for (int j=0;j<out.get(start).size();j++) {	
				k = out.get(start).get(j);
				if (map.containsKey(String.valueOf(k))) {
					path = path + map.get(String.valueOf(k));
					if (!strings.contains(path))
					strings.add(path);
					return strings;
				}
				if (k>start) {
					path = path +  String.valueOf(k);
					  forwardPath(strings,out,k,path,map);
					  if (out.get(start).size()-1>j) {
							path = path.substring(0,path.length()-1);
						}
				}
					if (k == out.size()-1) {
						break;
					}
			}
	
		return strings;		
	}
	
	public static String settingOut (ArrayList<ArrayList<Integer>> out, int edges, int []from, int[] to,Object[] weight, ArrayList<String>All) {
		 Map edges_values=new HashMap();  
		 int is_num =0,c = 0;
		for (int i =0;i<edges;i++) {
			 out.get(from[i]).add(to[i]);
			 if (isNumeric((String) weight[i])  ) {
				 c++;
			 }
			 edges_values.put( String.valueOf(from[i])+String.valueOf(to[i]),weight[i]);
		 }
		 if (c==edges) {
			 is_num =1;
		 }
		
		 for (int i =0;i<out.size();i++) {
				Collections.sort(out.get(i));
			}
		 
		
		 ArrayList<String> paths =  new ArrayList<String>();
		 Map mapPath = new HashMap(); 
			ArrayList<String> strings =forwardPath(paths,out,1,"1",mapPath);
			System.out.println(strings);
			ArrayList<String> paths_no =  new ArrayList<String>();
			for (int i = 0;i<strings.size();i++) {
				paths_no.add( with_values(strings.get(i),edges_values, is_num));
			}
			
			ArrayList<String> loops_strings =  new ArrayList<String>();
			for (int i =1;i<out.size()-1;i++) {
				Map map=new HashMap();  
				ArrayList<String> loop = loops(loops_strings, out, i, i, String.valueOf(i),map);
			}
			All.addAll(loops_strings);
			System.out.print(loops_strings);
			String free;
			String delta = "1";
			double delta_num = 1;
			ArrayList<String> loops_no =  new ArrayList<String>();
			for (int i =0;i<loops_strings.size();i++) {
				loops_no.add( with_values(loops_strings.get(i),edges_values,is_num));
				free = loops_no.get(i).replace("-", "");
				if (is_num==1) {
					delta_num  =delta_num+ Double.parseDouble(free);
					delta = String.valueOf(delta_num);

				}else {
					delta = delta +"+"+ free;
				}			
			}
			
			String  [ ]deltas = new String [strings.size()];
			for (int i = 0;i<strings.size();i++) {
				deltas[i]="1";
				double deltas_num ;
				for (int j = 0;j<loops_strings.size();j++) {
					if (nonTouched(strings.get(i),loops_strings.get(j))) {
						deltas_num =1;
						if (loops_no.get(j).contains("-")) {
							free = loops_no.get(j).replace("-", "");
							if (is_num==1) {
								deltas_num  =deltas_num+ Double.parseDouble(free);
								deltas[i] = String.valueOf(deltas_num);
							}else
							
							deltas[i] = deltas[i]+"+"+free;
						}else {
							if (is_num==1) {
								deltas_num  =deltas_num - Double.parseDouble(loops_no.get(j));
								deltas[i] = String.valueOf(deltas_num);
							}else
							
							deltas[i] = deltas[i]+"-"+loops_no.get(j);
						}

					}
				}
			}
			
			ArrayList<String> non_touched_loops=  new ArrayList<String>();
			for (int i =0 ;i<loops_strings.size();i++) {
				for (int j =i+1 ;j<loops_strings.size();j++) {
					if (nonTouched(loops_strings.get(i),loops_strings.get(j))) {
						if (is_num!=1) {
						non_touched_loops.add(loops_no.get(i)+"*"+loops_no.get(j));
						free = non_touched_loops.get(non_touched_loops.size()-1).replace("-", "");
						}else {
							double subDelta =Double.parseDouble(loops_no.get(i))*Double.parseDouble(loops_no.get(j));
							free = String.valueOf(subDelta);
							non_touched_loops.add(free);
						}
						if (is_num==1) {
							delta_num  =delta_num+ Double.parseDouble(free);
							delta = String.valueOf(delta_num);

						}else {
						delta = delta +"+"+free;
						}
					}
				}
			}
	
			String bast="";
			double bast_num=0;
			for (int i = 0;i<paths_no.size();i++) {
				if (is_num==1) {
					bast_num =bast_num+ (Double.parseDouble(paths_no.get(i))*Double.parseDouble(deltas[i]));
					bast = String.valueOf(bast_num);

				}else {
				bast = bast + paths_no.get(i)+"*"+"("+deltas[i]+")"; 
				if (i!=paths_no.size()-1) {
					bast = bast +"+";
				}
				}
			}
			String TF;
			if (is_num==1) {
				double ans = bast_num/ delta_num;
				TF = String.valueOf(ans);
			}else {
				TF ='\n'+ "["+bast+"]"+'\n'+" / "+'\n'+ "[" +delta +"]";
			}
			
			return TF;
	}
	public static void setting (int nodes, int edges,ArrayList<ArrayList<Integer>> out) {
	
		 for (int i = 0; i < nodes+1; i++) {
			 ArrayList<Integer> row = new ArrayList<Integer>();
			  out.add(row);
			}		 
	}

}
