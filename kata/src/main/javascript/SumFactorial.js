const perimeter = (n) => {
	if (n === 0) return 0;
	if (n === 1) return 1;

	const fib = [1, 1];
	let i = 2;

	while (i <= n) {
		fib[i] = fib[i-1] + fib[i-2];
		++i;
	}

	return fib.reduce((init, acc) => init + acc) * 4;
};