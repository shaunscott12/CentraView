/*
 * $RCSfile: Html2Text.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:03 $ - $Author: mking_cv $
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
package com.centraview.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class Html2Text extends StringReader {

	HashMap specialTag=new HashMap();
	boolean EscapeCharacter=false;
	HashMap basicTag=new HashMap();

	StringBuffer plainText = new StringBuffer();

	/**
	 * @param htmlString
	 */
	public Html2Text(String htmlString) {
		super(htmlString);
		initSpecialTag();
		initBasicTag();

		int c;
		try {
			while((c=this.read())!=-1) {
				plainText.append((char)c);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String getPlainText(){
		return plainText.toString();
	}

	/**
	 * initialize  all the basicHtmlTag and there Action
	 */
	private void initBasicTag() {
		basicTag.put(new String("br"),new Character('\n'));
		basicTag.put(new String("/br"),new Character('\r'));
		basicTag.put(new String("p"),new Character(' '));
		basicTag.put(new String("/p"),new Character('\r'));
		basicTag.put(new String("blockquote"),new Character('\n'));
		basicTag.put(new String("/blockquote"),new Character('\r'));
		basicTag.put(new String("pre"),new Character('\n'));
		basicTag.put(new String("/pre"),new Character('\r'));
		basicTag.put(new String("h1"),new Character('\n'));
		basicTag.put(new String("/h1"),new Character('\r'));
		basicTag.put(new String("h2"),new Character('\n'));
		basicTag.put(new String("/h2"),new Character('\r'));
		basicTag.put(new String("h3"),new Character('\n'));
		basicTag.put(new String("/h3"),new Character('\r'));
		basicTag.put(new String("h4"),new Character('\n'));
		basicTag.put(new String("/h4"),new Character('\r'));
		basicTag.put(new String("h5"),new Character('\n'));
		basicTag.put(new String("/h5"),new Character('\r'));
		basicTag.put(new String("h6"),new Character('\n'));
		basicTag.put(new String("/h6"),new Character('\r'));
		basicTag.put(new String("hr"),new Character('\n'));
		basicTag.put(new String("/hr"),new Character('\r'));
		basicTag.put(new String("img"),new Character('\n'));
		basicTag.put(new String("/img"),new Character('\r'));
		basicTag.put(new String("area"),new Character('\r'));
		basicTag.put(new String("/area"),new Character('\n'));
		basicTag.put(new String("map"),new Character('\r'));
		basicTag.put(new String("/map"),new Character('\n'));
		basicTag.put(new String("table"),new Character('\n'));
		basicTag.put(new String("/table"),new Character('\r'));
		basicTag.put(new String("tr"),new Character('\n'));
		basicTag.put(new String("/tr"),new Character('\r'));
		basicTag.put(new String("td"),new Character('\t'));
		basicTag.put(new String("th"),new Character('\n'));
		basicTag.put(new String("/th"),new Character('\r'));
		basicTag.put(new String("ul"),new Character('\n'));
		basicTag.put(new String("/ul"),new Character('\r'));
		basicTag.put(new String("li"),new Character('\t'));
		basicTag.put(new String("/li"),new Character('\n'));
		basicTag.put(new String("form"),new Character(' '));
		basicTag.put(new String("input"),new Character(' '));
		basicTag.put(new String("select"),new Character(' '));
		basicTag.put(new String("option"),new Character(' '));
		basicTag.put(new String("textarea"),new Character(' '));
		basicTag.put(new String("embed"),new Character('\n'));
		basicTag.put(new String("/embed"),new Character('\r'));
		basicTag.put(new String("aplet"),new Character('\n'));
		basicTag.put(new String("/applet"),new Character('\r'));
	}

	/**
	 * initialize  all the SpecialTag
	 */
	private void initSpecialTag() {
		specialTag.put(new String("quot"),new Character('"'));
		specialTag.put(new String("amp"),new Character('&'));
		specialTag.put(new String("lt"),new Character('<'));
		specialTag.put(new String("gt"),new Character('>'));
		specialTag.put(new String("nbsp"),new Character(' '));
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#read()
	 */
	public int read() throws IOException {
		int c=super.read();

		while(c==(int)'<') {
			char [] basicTagValue=new char[20];
			int indice=0;

			if((basicTagValue[indice++]=(char)super.read())=='!') {
				while(((char)super.read())!='>');
				return read();
			}
			while((basicTagValue[indice]=(char)super.read())!='>' && basicTagValue[indice++]!=' ');
			if(basicTagValue[indice-1]==' ') {
				while(((char)super.read())!='>');
			}
			Character specialTagValue=(Character) basicTag.get(new String(basicTagValue,0,indice).toLowerCase());
			if(specialTagValue==null) {
				return read();
			}
			return specialTagValue.charValue();
		}

		if(c=='&') {
			char [] specialTagValue=new char[10];
			int tmpValue,indice=0;
			while((tmpValue=super.read())!=-1 && tmpValue!=';') {
				specialTagValue[indice++]=(char) tmpValue;
			}
			if(tmpValue==-1) {
				return -1;
			}
			return specialTagValue[0]!='#' ?	((Character)specialTag.get(new String(specialTagValue,0,indice))).charValue():
									(char) new Integer(new String(specialTagValue,1,indice-1)).intValue();
		}

		if(EscapeCharacter==true && Character.isWhitespace((char) c)) {
			EscapeCharacter=true;
			return read();
		}
		else if(Character.isWhitespace((char) c)) {
			EscapeCharacter=true;
			c=' ';
		}
		else {
			EscapeCharacter=false;
		}

		return c;
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#read(char[], int, int)
	 */
	public int read(char[] tab, int off, int len) throws IOException {
		int indice=off;
		int c;
		while((indice-off)<len) {
			c=read();
			if(c!=-1) {
				tab[indice++]=(char) c;
			}
			else {
				return -1;
			}
		}
		return indice-off;
	}
}



