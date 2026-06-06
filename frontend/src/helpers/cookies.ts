export function getCookie(name: string) {
    // console.log("getCookie", name);
    var cookieValue: string|null|undefined = null;
    // console.log("document.cookie", document.cookie);
    const value: string = `; ${document.cookie}`;
    // console.log("value", value);
    const parts: string[] = value.split(`; ${name}=`);
    // console.log("parts", parts);
    if (parts !== undefined) {
        if (parts.length === 2) {
            const mainPart = parts.pop();
            if (mainPart !== undefined) cookieValue = mainPart.split(";").shift();
        }
    }
    // console.log("cookieValue", cookieValue);
    return cookieValue;
}


