/*
 * This is an example test project created in Eclipse to test NotePad which is a sample 
 * project located in AndroidSDK/samples/android-9/NotePad
 * Just click on File --> New --> Project --> Android Project --> Create Project from existing source and
 * select NotePad.
 * 
 * Then you can run these test cases either on the emulator or on device. You right click
 * the test project and select Run As --> Run As Android JUnit Test
 * 
 * @author Renas Reda, renas.reda@jayway.com
 * 
 */

package com.example.android.notepad.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import asia.sonix.scirocco.SciroccoSolo;
import asia.sonix.scirocco.annotations.Scirocco;

import com.example.android.notepad.NotesList;
import com.jayway.android.robotium.solo.Solo;


public class NotePadTest extends ActivityInstrumentationTestCase2<NotesList>{

	private static SciroccoSolo solo;

	public NotePadTest() {
		super("com.example.android.notepad", NotesList.class);

	}

	public void setUp() throws Exception {
		solo = new SciroccoSolo(getInstrumentation(), getActivity());
	}

	@Scirocco(
			testProcedure="2件のノートを追加する。<br> 1件目:Note 1 <br> 2件目:Note 2",
			confirmationContents="2件のノートが追加されること")
	@Smoke
	public void testAddNote() throws Exception {
		solo.takeScreenShot();
		solo.clickOnMenuItem("Add note");
		//Assert that NoteEditor activity is opened
		solo.assertCurrentActivity("Expected NoteEditor activity", "NoteEditor"); 
//		//In text field 0, add Note 1
		solo.takeScreenShot();
		solo.enterText(0, "Note 1");
		solo.takeScreenShot();
		solo.goBackToActivity("NotesList");
		solo.takeScreenShot();
//		//Clicks on menu item
		solo.clickOnMenuItem("Add note");
		//In text field 0, add Note 2
		solo.enterText(0, "Note 2");
		//Go back to first activity named "NotesList"
		solo.goBackToActivity("NotesList"); 
		boolean expected = true;
		boolean actual = solo.searchText("Note 1") && solo.searchText("Note 2");
		//Assert that Note 1 & Note 2 are found
		assertEquals("Note 1 and/or Note 2 are not found", expected, actual); 
		solo.takeScreenShot();
	}

	@Scirocco(
			testProcedure="''Note 1'' のタイトルを''Note 1 test''に変更する",
			confirmationContents="正常にタイトルが変更されること")
	@Smoke 
	public void testEditNote() throws Exception {
		solo.takeScreenShot();
		// Click on the second list line
		solo.clickInList(2); 
		// Change orientation of activity
		solo.setActivityOrientation(Solo.LANDSCAPE);
		// Change title
		solo.clickOnMenuItem("Edit title");
		//In first text field (0), add test
		solo.enterText(0, " test");  
		solo.goBackToActivity("NotesList");
		boolean expected = true;
		// (Regexp) case insensitive
		boolean actual = solo.searchText("(?i).*?note 1 test"); 
		//Assert that Note 1 test is found
		assertEquals("Note 1 test is not found", expected, actual); 
		solo.takeScreenShot();

	}


	@Scirocco(
			testProcedure="ノートを削除する",
			confirmationContents="ノートが正常に削除されること")
	@Smoke
	public void testRemoveNote() throws Exception {
		solo.takeScreenShot();
		//(Regexp) case insensitive/text that contains "test"
		solo.clickOnText("(?i).*?test.*");
		//Delete Note 1 test
		solo.clickOnMenuItem("Delete");
		//Note 1 test & Note 2 should not be found
		boolean expected = false;   
		boolean actual = solo.searchText("Note 1 test");
		//Assert that Note 1 test is not found
		assertEquals("Note 1 Test is found", expected, actual);  
		solo.clickLongOnText("Note 2");
		//Clicks on Delete in the context menu
		solo.clickOnText("(?i).*?Delete.*");  
		actual = solo.searchText("Note 2");
		//Assert that Note 2 is not found
		assertEquals("Note 2 is found", expected, actual);  
		solo.takeScreenShot();
	}

	@Override
	public void tearDown() throws Exception {
		try {
			//Robotium will finish all the activities that have been opened
			solo.finalize(); 	
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	} 
}
