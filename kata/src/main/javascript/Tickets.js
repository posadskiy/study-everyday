const tickets = (peopleInLine) => {
	let tw = 0;
	let ff = 0;

	for (const bill of peopleInLine) {
		if (bill === 25) { tw++; continue; }
		if (bill === 50) {
			if (tw !== 0) {
				ff++;
				tw--;
				continue;
			} else { return 'NO'; }
		}
		if (bill === 100) {
			if (ff !== 0 && tw !== 0) {
				ff--;
				tw--;
				continue;
			} else if (tw >= 3) {
				tw -= 3;
				continue;
			}
			return 'NO';
		}
	}

	return 'YES';
};