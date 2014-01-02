package org.jdamico.yapea.tests;

import static org.junit.Assert.*;

import org.jdamico.yapea.commons.Utils;
import org.junit.Test;

public class DefaultTests {

	@Test
	public void testGetSalt() {
		String a = "test.jpg";
		String b = a.substring(a.length()-4, a.length()-1);
		System.out.println(b);
	}

}
