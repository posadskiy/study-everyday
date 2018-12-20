const sum_pairs = (ints, s) => {
	let howClose = ints.length;
	let iTake;
	let jTake;
	for (let i = 0; i < ints.length - 1; i++) {
		for (let j = i + 1; j < ints.length; j++) {
			if ((ints[i] + ints[j]) === s) {
				if (j - i < howClose) {
					howClose = j - i;
					iTake = ints[i];
					jTake = ints[j];
				}
			}
		}
	}

	if (howClose === ints.length) return undefined;
	return [iTake, jTake];
};

const sum_pairs_faster = (ints, s) => {
	const l = ints.length;
	let howClose = ints.length;
	const result = [];
	for (let i = 0; i < l-1; i++) {
		let round = howClose !== l ? i + howClose : l;
		for (let j = i + 1; j < round; j++) {
			if ((ints[i] + ints[j]) === s) {
				howClose = j - i;
				result[0] = ints[i];
				result[1] = ints[j];
			}
		}
	}

	if (result.length !== 2) return undefined;
	return result;
};