/** Parses "{name} / {level} / {item1}, {item2}..." lines into a JSON array of heroes. */
export function heroicInventory(input) {
    const heroes = input.map(line => {
        const [name, level, items] = line.split(' / ');
        return { name, level: Number(level), items: items ? items.split(', ') : [] };
    });
    return JSON.stringify(heroes);
}
