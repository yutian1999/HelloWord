package cn.itcast.base.dao;

import java.util.Arrays;

public class Demo {
	
	public static void main(String[] args) {
		int[] arr = {1,2,3,4,5};
		for(int j = 0; j < arr.length; j++) {
		for (int i = 0; i < arr.length -1; i++) {
			if (arr[i]<arr[i+1]) {
				int temp = arr[i];
				arr[i] = arr[i+1];
				arr[i] = temp;
			}
		}
	}
		System.out.println(Arrays.toString(arr));
	}
}
