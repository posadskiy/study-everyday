const findOdd = (arr) => {
	const seen = {};
	for (let i = 0; i < arr.length; ++i) {
		seen[arr[i]] = seen[arr[i]] !== undefined ? seen[arr[i]] + 1 : 1;
	}

	for (const item in seen) {
		if (seen[item] % 2 === 1) return +item;
	}
};

const findOddBetter = (xs) => xs.reduce((a, b) => a ^ b);