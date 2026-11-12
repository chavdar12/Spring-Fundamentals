import { test } from 'node:test';
import assert from 'node:assert/strict';
import { sortArray } from '../src/sort-array.js';
import { argumentInfo } from '../src/argument-info.js';
import { personalBMI } from '../src/personal-bmi.js';
import { heroicInventory } from '../src/heroic-inventory.js';
import { jsonsTable } from '../src/jsons-table.js';
import { cappyJuice } from '../src/cappy-juice.js';

test('sort array', () => {
    assert.deepEqual(sortArray([14, 7, 17, 6, 8], 'asc'), [6, 7, 8, 14, 17]);
    assert.deepEqual(sortArray([14, 7, 17, 6, 8], 'desc'), [17, 14, 8, 7, 6]);
});

test('argument info', () => {
    const fn = function () { console.log('Hello world!'); };
    assert.deepEqual(argumentInfo('cat', 42, fn), [
        'string: cat',
        'number: 42',
        `function: ${fn}`,
        'string = 1',
        'number = 1',
        'function = 1',
    ]);
    assert.deepEqual(argumentInfo(1, 'a', 2).slice(3), ['number = 2', 'string = 1']);
});

test('personal BMI', () => {
    assert.deepEqual(personalBMI('Peter', 29, 75, 182), {
        name: 'Peter', personalInfo: { age: 29, weight: 75, height: 182 }, BMI: 23, status: 'normal',
    });
    assert.deepEqual(personalBMI('Honey Boo Boo', 9, 57, 137), {
        name: 'Honey Boo Boo', personalInfo: { age: 9, weight: 57, height: 137 }, BMI: 30, status: 'obese',
        recommendation: 'admission required',
    });
});

test('heroic inventory', () => {
    assert.equal(heroicInventory([
        'Isacc / 25 / Apple, GravityGun',
        'Derek / 12 / BarrelVest, DestructionSword',
        'Hes / 1 / Desolator, Sentinel, Antara',
    ]), '[{"name":"Isacc","level":25,"items":["Apple","GravityGun"]},{"name":"Derek","level":12,"items":["BarrelVest","DestructionSword"]},{"name":"Hes","level":1,"items":["Desolator","Sentinel","Antara"]}]');
    assert.equal(heroicInventory(['Jake / 1000']), '[{"name":"Jake","level":1000,"items":[]}]');
});

test('JSONs table', () => {
    const html = jsonsTable([
        '{"name":"Pesho","position":"Promenliva","salary":100000}',
        '{"name":"<b>Teo</b>","position":"Lecturer","salary":1000}',
    ]);
    assert.equal(html, [
        '<table>',
        '\t<tr>', '\t\t<td>Pesho</td>', '\t\t<td>Promenliva</td>', '\t\t<td>100000</td>', '\t</tr>',
        '\t<tr>', '\t\t<td>&lt;b&gt;Teo&lt;/b&gt;</td>', '\t\t<td>Lecturer</td>', '\t\t<td>1000</td>', '\t</tr>',
        '</table>',
    ].join('\n'));
});

test('cappy juice', () => {
    assert.equal(cappyJuice(['Orange => 2000', 'Peach => 1432', 'Banana => 450', 'Peach => 600', 'Strawberry => 549']),
        'Orange => 2\nPeach => 2');
    assert.equal(cappyJuice(['Kiwi => 234', 'Pear => 2345', 'Watermelon => 3456', 'Kiwi => 4567', 'Pear => 5678',
        'Watermelon => 6789']), 'Pear => 8\nWatermelon => 10\nKiwi => 4');
});
