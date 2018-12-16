const inArray = (array1,array2) => array1
	.filter(item => array2.some(fullStr => fullStr.includes(item)))
	.sort()