function spiralize (n) {
  let array = Array(n).fill(0).map(()=>Array(n).fill(0))

  let lastX = array.length;
  let lastY = array.length;
  let firstX = 0;
  let firstY = 0;

  while (lastX > firstX && lastY > firstY) {
    const start = firstY === 0 ? firstY : firstY - 1;
    for (let i = start; i < lastY; ++i) {
      array[firstX][i] = 1;
    }
    if (lastY - start < 2) return array;
    firstX += 2;

    for (let i = firstX - 1; i < lastX; ++i) {
      array[i][lastY - 1] = 1;
    }
    if (lastX - firstY - 1 < 2) return array;
    lastY -= 2;

    for (let i = lastY + 1; i > firstY; --i) {
      array[lastX - 1][i - 1] = 1;
    }
    if (lastY + 1 - firstY < 2) return array;
    lastX -= 2;

    for (let i = lastX + 1; i > firstX; --i) {
      array[i - 1][firstY] = 1;
    }
    if (lastX + 1 - firstX < 2) return array;
    firstY += 2;
  }

  return array;
}
