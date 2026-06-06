import { defineStore } from 'pinia'
import type { AxiosError, AxiosResponse } from "axios";
import { User } from '@/models/user';
import { ApiError } from '@/models/api/apiError';
import { ref, computed } from 'vue';
import httpClient from '@/configs/axios';

export const useUserStore = defineStore("userStore", () => {

    const user = ref<User|null>(null);
    const isSessionChecked = ref<boolean>(false);

    const isAuthenticated = computed(() =>{return !(user.value === null);});

    async function register(username: string, password: string) {
        if (isAuthenticated.value) {
            // console.log("user registered", user.value?.username);
            return;
        }
        const registerDTO = {
            "username": username,
            "password": password,
        }
        // console.log(registerDTO);
        try {
            const { data, status }: AxiosResponse<User> = await httpClient.post("/api/register",
                registerDTO,
                {
                    headers: {
                        "Accept": 'application/json',
                    },
                    withCredentials: true,
                },
            )
            // user.value = data;
            // console.log("data", data);
            // console.log("status", status);
            return null;
        } catch (error: any) {
            // console.log(error);
            const apiError: ApiError = error.response.data;
            return apiError;
        } finally {
            // console.log("user", user.value);
        }
    }

    async function login(username: string, password: string) {
        const loginDto = {
            "username": username,
            "password": password,
        }
        // console.log(loginDto);
        try {
            const { data, status } = await httpClient.post<User>("/api/login",
                loginDto,
                {
                    headers: {
                        "Accept": 'application/json',
                    },
                    withCredentials: true,
                },
            )
            user.value = data;
            // console.log("data", data);
            // console.log("status", status);
            return null;
        } catch (error: any) {
            // console.log(error);
            // console.log(error.response.data);
            const apiError: ApiError = error.response.data;
            return apiError;
        } finally {
            // console.log("user", user.value);
        }
    }

    async function logout() {
        // console.log("logout");
        try {
            const response = await httpClient.post("/api/logout", 
                null,
                {
                    headers: {
                        "Accept": 'application/json',
                    },
                    withCredentials: true,
                }  
            );
            user.value = null;
            // console.log(response);
            // console.log(response.data);
            return null;
        } catch (error: any) {
            const apiError: ApiError = error.response.data;
            return apiError;
        }
    }

    async function getSession() {
        // console.log("test, test");
        if (user.value == null) {
            try {
                const response = await httpClient.post<User>("/api/session",
                    null,
                    {
                        headers: {
                            "Accept": 'application/json',
                        },
                        withCredentials: true,
                    },
                );
                // console.log(response);
                const userData = response.data as User;
                // console.log(userData);
                const isValid = User.checkData(userData);
                // console.log(isValid);
                if (isValid) {
                    user.value = userData;
                }
                // console.log(user.value);
            } catch (error: any) {
                console.error(error);
            } finally {
                isSessionChecked.value = true;
                // console.log("isSessionChecked", isSessionChecked.value);
            }
        }
    }

    return {
        user,
        isAuthenticated,
        isSessionChecked,
        login,
        register,
        logout,
        getSession,
    }
})