// MORSE_CODE - map where key - morse code, value - ascii symbol.
decodeMorse = function(morseCode){
	MORSE_CODE['space'] = ' ';
	return morseCode.trim().replace(/ {3}/g, ' space ').split(' ').map(c => MORSE_CODE[c]).join('');
};

const decodeMorseBetter = (morseCode) => {
	const decodeMorseLetter = (letter) => MORSE_CODE[letter];
	const decodeMorseWord = (word) => word.split(' ').map(decodeMorseLetter).join('');

	return morseCode.trim().split('   ').map(decodeMorseWord).join(' ');
};