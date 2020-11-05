package com.web.produce.service.internal;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
         String a = "fyx	2020-11-03 16:23:27	255	1		0	0	";
         String[] b = a.split("	");
         String a1 = "fyx	2020-11-03 16:23:27";
         
         String[] times = b[1].split(" ");
         //System.out.println(times[1]);
         
         String test = "513907	2020-10-29 15:18:18	255	1		0	0	\n"+
                       "fyx	2020-11-03 16:23:27	255	1		0	0	\n"+
                       "fyx	2020-11-04 10:04:54	255	1		0	0	\n"+
                       "0	2020-11-04 10:11:38	201	1		0	0	\n"+
                       "fyx	2020-11-04 10:18:54	255	1		0	0	\n";
         System.out.println(test);
         String[] pa = test.split("\n");
         System.out.println(pa.length);
         for(String p:pa){
        	 System.out.println(p);
         }
         
	}

}
