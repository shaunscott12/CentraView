/*
 * $RCSfile: PasswordGenerator.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:06 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.centraview.com/opensource/license.html
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Original Code is: CentraView Open Source. 
 * 
 * The developer of the Original Code is CentraView.  Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
 */
package com.centraview.user;

import java.util.Random;

public class PasswordGenerator {

                final	String[]    UPPER	= {"A","B","C","D","E","F","G","H","I","J","K",
"L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
                final	String[]    LOWER	= {"a","b","c","d","e","f","g","h","i","j","k",
"l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
                final	String[]    DIGIT	= {"0","1","2","3","4","5","6","7","8","9"};
                private Random      genNum 	= new Random();
                private int         arrTam	= 0;
                private int         contL	= 0;
                private int         contU	= 0;
                private int         contD	= 0;
                private String      res         = "";
                private String      pass;
                private int         iGeRaNu ;
                private String      sTam ;
                private String      sChar;

        public PasswordGenerator() {
        }

        public String generateIt() {
                pass	= "";
                for (int i=0; i<10; i++){
                        if ((contL == 2) || (contU) == 1) {
                                arrTam = 9;
                                //Adding a digit to password
                                pass = pass + getDigit(genNum());
                                contL = 0;
                                contU = 0;
                        }else {
                                arrTam = 26;
                                if(genNum() % 2 == 0) {
                                        //Adding a Upper character to password
                                        pass = pass + getUpper(genNum());
                                        contU ++;
                                } else {
                                        //Adding a Lower character to password
                                        pass = pass + getLower(genNum());
                                        contL ++;
                                }
                        }
                }

                return pass;

        }

        int genNum() {
                iGeRaNu = genNum.nextInt() % arrTam;
                sTam = new Integer(iGeRaNu).toString();
                sChar = new String(String.valueOf(sTam.charAt(0)));
                //Converting the possible negative number to positive
                if ( sChar.equals("-")){iGeRaNu = iGeRaNu * -1;}
                return iGeRaNu;
        }

        String getUpper(int val) {
                res = UPPER[val];
                return res;

        }

        String getLower(int val) {
                res = LOWER[val];
                return res;
        }

        String getDigit(int val) {
                res = DIGIT[val];
                return res;
        }

        private static Random rn = new Random();

        public static int rand(int lo, int hi)
        {
          int n = hi - lo + 1;
          int i = rn.nextInt() % n;
          if (i < 0)
            i = -i;
          return lo + i;
        }

        public static String randomstring(int lo, int hi)
        {
          int n = rand(lo, hi);
          byte b[] = new byte[n];
          for (int i = 0; i < n; i++)
            b[i] = (byte)rand('1', 'z');
          return new String(b, 0);
        }


}
