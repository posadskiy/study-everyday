package dev.posadskiy.kata;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * John has invited some friends. His list is:
 *
 * s = "Fred:Corwill;Wilfred:Corwill;Barney:Tornbull;Betty:Tornbull;Bjon:Tornbull;Raphael:Corwill;Alfred:Corwill";
 *
 * Could you make a program that
 *
 * makes this string uppercase
 * gives it sorted in alphabetical order by last name.
 * When the last names are the same, sort them by first name. Last name and first name of a guest come in the result between parentheses separated by a comma.
 *
 * So the result of function meeting(s) will be:
 *
 * "(CORWILL, ALFRED)(CORWILL, FRED)(CORWILL, RAPHAEL)(CORWILL, WILFRED)(TORNBULL, BARNEY)(TORNBULL, BETTY)(TORNBULL, BJON)"
 * It can happen that in two distinct families with the same family name two people have the same first name too.
 */
class Meeting {

	static String meeting(String s) {
		String[] friends = StringUtils.split(s, ';');
		List<String> friendsList = Arrays.asList(friends);
		friendsList.sort((o1, o2) -> {
			String[] firstFriendName = StringUtils.split(o1, ':');
			String[] secondFriendName = StringUtils.split(o2, ':');

			int compareLastNames = firstFriendName[1].compareToIgnoreCase(secondFriendName[1]);
			if (compareLastNames == 0) {
				return firstFriendName[0].compareToIgnoreCase(secondFriendName[0]);
			}

			return compareLastNames;
		});

		String result = friendsList.stream().map(names -> {
			String[] personNames = StringUtils.split(names, ':');
			return "(".concat(personNames[1].toUpperCase()).concat(", ").concat(personNames[0].toUpperCase()).concat(")");
		}).collect(Collectors.joining(""));

		return result;
	}

	static String theBestSolution(String s) {
		return Arrays.stream(s.toUpperCase().split(";"))
			.map(guest -> guest.replaceAll("(\\w+):(\\w+)", "($2, $1)"))
			.sorted()
			.collect(Collectors.joining(""));
	}

}
