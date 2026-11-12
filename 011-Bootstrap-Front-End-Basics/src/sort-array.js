/** Returns a sorted copy: 'asc' smallest first, 'desc' largest first. */
export function sortArray(numbers, order) {
    const direction = order === 'desc' ? -1 : 1;
    return [...numbers].sort((a, b) => (a - b) * direction);
}
