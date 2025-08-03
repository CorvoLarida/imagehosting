import axios from "axios";
import { getCookie } from "@/helpers/cookies";

const httpClient = axios.create({
    baseURL: "http://localhost:8899",
    xsrfCookieName: "XSRF-TOKEN",
    xsrfHeaderName: "X-XSRF-TOKEN",
});

// Where you would set stuff like your 'Authorization' header, etc ...
httpClient.defaults.headers.common['Content-Type'] = 'application/json';

function getXSRFToken() {
    const csrfHeaderName = httpClient.defaults.xsrfCookieName;
    const token = getCookie(csrfHeaderName);
    if (token == null) {
        httpClient.get("/api/csrf", {withCredentials: true,});
        const csrfToken = getCookie("XSRF-TOKEN");
        console.log(csrfToken);
        return getCookie(csrfHeaderName);
    } else return token;
}
// httpClient.defaults.headers.post["Accept"] = 'application/json';
// Also add/ configure interceptors && all the other cool stuff
httpClient.interceptors.request.use((config) => {
    console.log(config);
    if (config.method === "post") {
        const token = getXSRFToken();
        if (token) {
            config.headers[httpClient.defaults.xsrfHeaderName] = token;
        }
    }
    return config;
})

export default httpClient;
