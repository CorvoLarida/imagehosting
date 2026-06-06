import axios from "axios";
import { getCookie } from "@/helpers/cookies";

const CookieXSRF = "XSRF-TOKEN";
const HeaderXSRF = "X-XSRF-TOKEN";

const httpClient = axios.create({
    baseURL: "http://" + import.meta.env.VITE_FRONTEND_BACKEND_CONNECTION_STRING,
    xsrfCookieName: CookieXSRF,
    xsrfHeaderName: HeaderXSRF,
});

// Where you would set stuff like your 'Authorization' header, etc ...
httpClient.defaults.headers.common['Content-Type'] = 'application/json';

async function getXSRFToken() {
    const token = getCookie(CookieXSRF);
    if (token != null) return token;
    else {
        // console.log("httpClient.get/api/csrf");
        await httpClient.get("/api/csrf", {withCredentials: true,});
        return getCookie(CookieXSRF);
    }
}

// httpClient.defaults.headers.post["Accept"] = 'application/json';
// Also add/ configure interceptors && all the other cool stuff
httpClient.interceptors.request.use(
    async function (config) {
        const token = await getXSRFToken();
        config.headers[HeaderXSRF] = token;
        return config;
    },
    null,
    {
        runWhen: (config) => {return config.method === "post"},
    }
);

export default httpClient;
