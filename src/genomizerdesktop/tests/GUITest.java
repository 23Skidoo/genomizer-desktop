package genomizerdesktop.tests;

import static org.junit.Assert.*;
import genomizerdesktop.GUI;

import org.junit.Before;
import org.junit.Test;

public class GUITest {
	
	private GUI gui;

	@Before
	public void setUp() throws Exception {
		gui = new GUI();
	}

	@Test
	public void testConstructor() {
		assertNotNull(gui);
	}

}
