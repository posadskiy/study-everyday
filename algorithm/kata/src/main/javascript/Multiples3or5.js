const solution = (number) => {
	let i = 0;
	let count = 0;
	while (i < number) {
		if (i % 3 === 0 || i % 5 === 0) {
			count += i;
		}
		++i;
	}

	return count;
};