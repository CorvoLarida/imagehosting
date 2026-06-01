export function getCookie(name: string) {
    // console.log("getCookie", name);
    var cookieValue: string|null = null;
    // console.log("document.cookie", document.cookie);
    const value: string = `; ${document.cookie}`;
    // console.log("value", value);
    const parts: string[] = value.split(`; ${name}=`);
    // console.log("parts", parts);
    if (parts.length === 2) cookieValue = parts.pop().split(';').shift();
    return cookieValue;
}


