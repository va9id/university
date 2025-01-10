/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.floor.Floor;
import com.sysc3303.project.scheduler.Scheduler;

/**
 * @author Group 9
 *
 */
class FloorTest {
	private static Floor floor;
	private static final boolean SHOULD_SLEEP = false;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		floor = new Floor(SHOULD_SLEEP);
	}
	
	@AfterAll
    public static void tearDown() {
    	floor.closeSockets();
    }

	@Test
	void test() {
		floor.addResponse("Testing");
		assertEquals("Testing", floor.getLatestResponse());
	}

}
