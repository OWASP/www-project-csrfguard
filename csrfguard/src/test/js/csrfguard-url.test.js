const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const test = require('node:test');
const vm = require('node:vm');

const source = fs.readFileSync(path.join(__dirname, '../../main/resources/csrfguard.js'), 'utf8');

function loadIsValidUrl(href, domainStrict = true) {
    // Run the original URL check and its helpers before the script installs DOM hooks.
    const end = source.indexOf('/* parse uri from url */');
    assert.notEqual(end, -1);
    const prefix = source.slice(0, end)
        .replace("const DOMAIN_STRICT = '%DOMAIN_STRICT%';", `const DOMAIN_STRICT = ${domainStrict};`)
        .replace('(function () {', 'globalThis.isValidUrlForTest = (function () {');
    const context = {URL, location: new URL(href)};
    vm.runInNewContext(`${prefix}return isValidUrl; })(); }`, context);
    return context.isValidUrlForTest;
}

test('tokens are allowed on relative and same-origin URLs', () => {
    const isValidUrl = loadIsValidUrl('https://example.test/app/page');
    for (const url of ['/submit', 'submit', '?next=1', 'https://example.test/submit']) {
        assert.equal(isValidUrl(url), true, url);
    }
});

test('tokens are not added to external or non-HTTP URLs', () => {
    const isValidUrl = loadIsValidUrl('https://example.test/app/page');
    for (const url of [
        'https://example.test:443@attacker.test/submit',
        'https://example.test@attacker.test/submit',
        'https://attacker.test/submit',
        'https://example.test:8443/submit',
        'http://example.test/submit',
        '//attacker.test/submit',
        '\\\\attacker.test/submit',
        '  //attacker.test/submit',
        'javascript:alert(1)',
        '#section',
        'http://[invalid',
    ]) {
        assert.equal(isValidUrl(url), false, url);
    }
});

test('non-strict domain matching still permits a parent domain on the same scheme and port', () => {
    const isValidUrl = loadIsValidUrl('https://app.example.test/page', false);
    assert.equal(isValidUrl('https://example.test/submit'), true);
    assert.equal(isValidUrl('https://attacker.test/submit'), false);
});
