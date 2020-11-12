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
         //System.out.println(test);
         String[] pa = test.split("\n");
         System.out.println("Sk1TUzIxAAADDg8ECAUHCc7QAAAvD5EBAAA+gjMZfQ7OADJdhgA8ACVqlACUAIdkXgAWD6ZkygAFAWRk1A6JAB1kXgCiAHBqdwBGAH9knAAlDnpklgDLAORkpQ60AJJkdwDTAS1qdAAiAbNk9AC8Dk9CWwA4AWhkvQ5nAI9kagCgAZFfgQDDAFFOoAACDzNUawCYAK5eog4kARpkiwDvAaZsvwA6AZdbVgBaD5BVoQBDANVkF+1IEooPrvgX9YGPJ/V2H085yIw2CYOBU/1Hie4OCZ3Gj9+b6vaCCcz1Xw4Cc6YH7/9EBRZv7dLZ8vyTSn2fDWJCmH8w/3aFlYfdEjIJ2uc8ETb/uv2yg84cGXmXg1ML5gNm5MThdSM9MiLCtAdFA3+BEudHHAr5GnrcdoKF1Y6s9rYNh32P8VMJowOo4tb97pOWgxqf83XaASApAQEFF54LAXYSDFwJxVIaDVXBQggAX+MAT/HBwQwAw0JVhWnPwFwFAKNCw/79zwcAdEOAbwUJA5VCjHDCcwXFokQe/k8KAHxIxjXDX/4HAHRJeqNxBQ5aZXfBgA3Fu2aeZnh1ZwoApmb+8f/AwD3+BsVcaH/BhBEAInQi//0zOUX//koDxdeIKsAPAJOTj1KAwc/BiP8UAGpTcZKGa8HAwsCROsMADnOX/f4JAFyZFEr+wEcWAOoWosLxwcF4hsN3ScBzGwFqnGmMwkVn/Jx0jRQAHJ0nwDtP/v3ARjtw0ACgvZKPxMHCw7LC/M3Bi5QKADIESWWKwcAOAHzEm8WehW+aFADpt2fB/M/Awv+Dk3WZCANwyFPGwXnBwgCaxB03wP8VAN6+1TD//sEu/j47SloIAZbPJP/BoQsDcNU0i1nDwAUDE2gFNMETEOXJnsPxW4bBxcjBBsPCzMAXEE0Yt4TB/fAo/fz9wPsFwfzxwFUcAOb/ZcBSjsHDwseXwb3EcmHDBhCxTYw4/1YHEdsfk/5PBD4JDofVK8HA/wd8wg0RoSkiwwXVXz4sg1JCABJDxAECDoUA2CwAANdFUQ4=".length());
         for(String p:pa){
        	 //System.out.println(p);
         }
         
	}

}
