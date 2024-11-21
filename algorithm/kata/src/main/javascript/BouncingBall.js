const bouncingBall = (h,  bounce,  window) => {
	if (h <= 0 || bounce <= 0 || bounce >= 1 || window >= h) return -1;

	let count = 1;
	h *= bounce;

	while (h >= window) {
		count += 2;
		h *= bounce;
	}

	return count;
};