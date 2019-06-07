package dev.posadskiy.kata;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class MeetingTest {

	@Test
	public void test() {
		String one = "Alexis:Wahl;John:Bell;Victoria:Schwarz;Abba:Dorny;Grace:Meta;Ann:Arno;Madison:STAN;Alex:Cornwell;Lewis:Kern;Megan:Stan;Alex:Korn";
		String two = "John:Gates;Michael:Wahl;Megan:Bell;Paul:Dorries;James:Dorny;Lewis:Steve;Alex:Meta;Elizabeth:Russel;Anna:Korn;Ann:Kern;Amber:Cornwell";
		String three = "Alex:Arno;Alissa:Cornwell;Sarah:Bell;Andrew:Dorries;Ann:Kern;Haley:Arno;Paul:Dorny;Madison:Kern";

		String oneResult = Meeting.meeting(one);
		String twoResult = Meeting.meeting(two);
		String threeResult = Meeting.meeting(three);

		assertEquals(oneResult, "(ARNO, ANN)(BELL, JOHN)(CORNWELL, ALEX)(DORNY, ABBA)(KERN, LEWIS)(KORN, ALEX)(META, GRACE)(SCHWARZ, VICTORIA)(STAN, MADISON)(STAN, MEGAN)(WAHL, ALEXIS)");
		assertEquals(twoResult, "(BELL, MEGAN)(CORNWELL, AMBER)(DORNY, JAMES)(DORRIES, PAUL)(GATES, JOHN)(KERN, ANN)(KORN, ANNA)(META, ALEX)(RUSSEL, ELIZABETH)(STEVE, LEWIS)(WAHL, MICHAEL)");
		assertEquals(threeResult, "(ARNO, ALEX)(ARNO, HALEY)(BELL, SARAH)(CORNWELL, ALISSA)(DORNY, PAUL)(DORRIES, ANDREW)(KERN, ANN)(KERN, MADISON)");
	}

	@Test
	public void testTheBestSolution() {
		String one = "Alexis:Wahl;John:Bell;Victoria:Schwarz;Abba:Dorny;Grace:Meta;Ann:Arno;Madison:STAN;Alex:Cornwell;Lewis:Kern;Megan:Stan;Alex:Korn";
		String two = "John:Gates;Michael:Wahl;Megan:Bell;Paul:Dorries;James:Dorny;Lewis:Steve;Alex:Meta;Elizabeth:Russel;Anna:Korn;Ann:Kern;Amber:Cornwell";
		String three = "Alex:Arno;Alissa:Cornwell;Sarah:Bell;Andrew:Dorries;Ann:Kern;Haley:Arno;Paul:Dorny;Madison:Kern";

		String oneResult = Meeting.theBestSolution(one);
		String twoResult = Meeting.theBestSolution(two);
		String threeResult = Meeting.theBestSolution(three);

		assertEquals(oneResult, "(ARNO, ANN)(BELL, JOHN)(CORNWELL, ALEX)(DORNY, ABBA)(KERN, LEWIS)(KORN, ALEX)(META, GRACE)(SCHWARZ, VICTORIA)(STAN, MADISON)(STAN, MEGAN)(WAHL, ALEXIS)");
		assertEquals(twoResult, "(BELL, MEGAN)(CORNWELL, AMBER)(DORNY, JAMES)(DORRIES, PAUL)(GATES, JOHN)(KERN, ANN)(KORN, ANNA)(META, ALEX)(RUSSEL, ELIZABETH)(STEVE, LEWIS)(WAHL, MICHAEL)");
		assertEquals(threeResult, "(ARNO, ALEX)(ARNO, HALEY)(BELL, SARAH)(CORNWELL, ALISSA)(DORNY, PAUL)(DORRIES, ANDREW)(KERN, ANN)(KERN, MADISON)");
	}

	@Test
	public void speedTest() {
		long startTest = System.nanoTime();
		for (int i = 0; i < 100000; ++i) {
			Meeting.meeting(createRandomString());
		}
		long finishTest = System.nanoTime();
		long firstResult = finishTest - startTest;

		startTest = System.nanoTime();
		for (int i = 0; i < 100; ++i) {
			Meeting.theBestSolution(createRandomString());
		}
		finishTest = System.nanoTime();
		long secondResult = finishTest - startTest;

		System.out.println(firstResult);
		System.out.println(secondResult);
	}

	private String createRandomString() {
		int size = ThreadLocalRandom.current().nextInt(1, 100);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < size; ++i) {
			int firstName = ThreadLocalRandom.current().nextInt(1, 100);
			int lastName = ThreadLocalRandom.current().nextInt(1, 100);
			result.append(RandomStringUtils.randomAlphabetic(firstName))
				.append(':')
				.append(RandomStringUtils.randomAlphabetic(lastName))
				.append(';');
		}
		return result.toString();
	}

}