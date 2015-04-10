package com.equinox;
import java.util.Scanner;
import 
public class Test {

	class Solution {
	    public int solution(int[] A) {
	        // write your code in Java SE 8
	        for(int i=0; i<A.length-2; i++){
	            Boolean foundDecreasing = false;
	            Boolean foundIncreasing = false;
	            int startIndex;
	            int deepestIndex;
	            int deepestValue;
	            int highestIndex;
	            int highestValue;
	            ArrayList<Double> list = new ArrayList<Double>();
	            
	            //Found decreasing segment
	            if(A[i]>A[i+1]){
	                foundDecreasing = true;
	                
	                //Find deepest value
	                deepestValue = A[i+1];
	                deepestIndex = i+1;
	                while(A[i]>A[i+1] && i<A.length-1){
	                    deepestIndex = i+1;
	                    deepestValue = A[i+1];
	                    i++;
	                
	                }
	                //Found increasing segment
	                    int k = deepestindex + 1;
	                    if(A[k]>A[k-1]){
	                        foundIncreasing = true;
	                        highestValue = A[k];
	                        highestIndex = k;
	                        while(A[k]>A[k-1] && k<A.length){
	                            highestValue = A[k];
	                            highestIndex = k;
	                            k++;
	                        }
	                    }    
	                }
	                
	                if(foundIncreasing==true && foundDecreasing == true){
	                  int depth1 = A[startIndex] - deepestValue;
	                  int depth2 = A[highestIndex] - highestValue;
	                  list.add(Math.min(depth1, depth2));
	                }
	              
	            }    
	        }
	    }
	}

