/** Sums juice quantities; every full 1000 is a bottle. Bottles print in the order they were first made. */
export function cappyJuice(input) {
    const quantities = new Map();
    const bottles = new Map();
    for (const line of input) {
        const [juice, amount] = line.split(' => ');
        const total = (quantities.get(juice) ?? 0) + Number(amount);
        const made = Math.floor(total / 1000);
        if (made > 0) {
            bottles.set(juice, (bottles.get(juice) ?? 0) + made);
        }
        quantities.set(juice, total % 1000);
    }
    return [...bottles].map(([juice, count]) => `${juice} => ${count}`).join('\n');
}
