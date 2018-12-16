const tribonacci = (arr,n) => {
	if (arr.length > n) return arr.slice(0, n);
	while(arr.length < n) {
		arr.push(arr[arr.length - 1] + arr[arr.length - 2] + arr[arr.length - 3]);
	}
	return arr;
};