async function fetchHello() {
    const name = document.getElementById('nameInput').value || 'mundo';
    const res = await fetch('/App/hello?name=' + encodeURIComponent(name));
    const text = await res.text();
    // Extraer solo el contenido del body de la respuesta HTML
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, 'text/html');
    document.getElementById('result').textContent = doc.body.textContent.trim();
}
