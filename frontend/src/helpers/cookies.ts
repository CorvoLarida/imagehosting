export function getCookie(name: string) {
    var cookieValue: string = "";
    const value: string = `; ${document.cookie}`;
    const parts: string[] = value.split(`; ${name}=`);
    if (parts.length === 2) cookieValue = parts.pop().split(';').shift();
    return cookieValue;
}


