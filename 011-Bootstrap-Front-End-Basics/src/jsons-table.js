const escapeHtml = value => String(value)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');

/** Turns JSON employee records into an HTML table, escaping every value. */
export function jsonsTable(input) {
    const rows = input.map(json => {
        const { name, position, salary } = JSON.parse(json);
        const cells = [name, position, salary].map(v => `\t\t<td>${escapeHtml(v)}</td>`).join('\n');
        return `\t<tr>\n${cells}\n\t</tr>`;
    });
    return ['<table>', ...rows, '</table>'].join('\n');
}
