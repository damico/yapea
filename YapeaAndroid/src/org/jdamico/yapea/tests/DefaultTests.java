package org.jdamico.yapea.tests;

/*
 * This file is part of YAPEA.
 * 
 *    YAPEA is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License (version 2) 
 *    as published by the Free Software Foundation.
 *
 *    YAPEA is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with YAPEA.  If not, see <http://www.gnu.org/licenses/>.
 */

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
