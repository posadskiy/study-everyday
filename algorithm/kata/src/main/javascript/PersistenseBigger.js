const persistence = (num) => {
	let count = 0;
	while (num > 9) {
		count++;
		num = ('' + num).split('').reduce((a, b) => a * +b, 1);
	}
	return count;
};