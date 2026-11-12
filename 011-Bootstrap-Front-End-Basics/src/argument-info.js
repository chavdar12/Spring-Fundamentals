/** Prints "type: value" per argument, then a "type = count" tally, most frequent first. */
export function argumentInfo(...args) {
    const lines = [];
    const counts = new Map();
    for (const arg of args) {
        const type = typeof arg;
        lines.push(`${type}: ${arg}`);
        counts.set(type, (counts.get(type) ?? 0) + 1);
    }
    // sort is stable, so equal counts keep their order of appearance
    [...counts].sort((a, b) => b[1] - a[1]).forEach(([type, count]) => lines.push(`${type} = ${count}`));
    lines.forEach(line => console.log(line));
    return lines;
}
