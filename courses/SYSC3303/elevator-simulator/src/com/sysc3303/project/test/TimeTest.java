/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.utils.Time;

/**
 * @author Group 9
 *
 */
class TimeTest {
	private static Time time, time2, time3;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		time = new Time("1", "1", "1", "1");
		time2 = new Time("1", "1", "1", "10");
		time3 = new Time("1", "1", "2", "1"); 
	}

	@Test
	void timeDifferenceTest() {
		assertEquals(9, time.getTimeDifferenceInMS(time2));
		assertEquals(1000, time.getTimeDifferenceInMS(time3));
		assertEquals(991, time2.getTimeDifferenceInMS(time3));
	}
	@Test
	void isTimeBeforeTest() {
		assertEquals(true, time.isTimeBefore(new Time("1", "1", "1", "2")));
		assertEquals(true, time.isTimeBefore(new Time("1", "1", "2", "1")));
		assertEquals(true, time.isTimeBefore(new Time("1", "2", "1", "1")));
		assertEquals(true, time.isTimeBefore(new Time("2", "1", "1", "1")));
		
		assertEquals(false, time.isTimeBefore(new Time("0", "1", "1", "1")));
		assertEquals(false, time.isTimeBefore(new Time("1", "0", "1", "1")));
		assertEquals(false, time.isTimeBefore(new Time("1", "1", "0", "1")));
		assertEquals(false, time.isTimeBefore(new Time("1", "1", "1", "0")));
		
		assertEquals(false, time.isTimeBefore(new Time("1", "1", "1", "1")));
	}
	
	@Test
	void toStringTest() {
		System.out.println(time.toString());
		assertEquals("1:1:1.1", time.toString());
	}
	
	@Test
	void createFromTimeStringTest() {
		Time test = Time.createFromTimeString("1:1:1.1");
		assertEquals(time.getHours(), test.getHours());
		assertEquals(time.getMinutes(), test.getMinutes());
		assertEquals(time.getSeconds(), test.getSeconds());
		assertEquals(time.getMilliseconds(), test.getMilliseconds());
	}

}
