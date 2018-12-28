const isSolved = (board) => {
	let multLD = 1;
	let multRD = 1;
	let isZero = false;
	for (let i = 0; i < board.length; i++) {
		multLD *= board[i][i];
		multRD *= board[board.length - i - 1][i];
	}
	if (multLD === 1 || multRD === 1) return 1;
	if (multLD === 8 || multRD === 8) return 2;
	for (let i = 0; i < board.length; ++i) {
		let multH = 1;
		let multV = 1;
		for (let j = 0; j < board.length; ++j) {
			if (board[i][j] === 0) isZero = true;
			multH *= board[i][j];
			multV *= board[j][i];
		}
		if (multH === 1) return 1;
		if (multH === 8) return 2;

		if (multV === 1) return 1;
		if (multV === 8) return 2;
	}
	return isZero ? -1 : 0;

};