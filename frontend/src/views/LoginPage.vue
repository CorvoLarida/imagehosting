<script setup lang="ts">
import { ref } from 'vue';
import httpClient from '@/configs/axios';
import { AxiosResponse } from "axios";

const props = defineProps({
  error: { type: Object, },
})
const isLoggedOut = ref(false);
const tResp = ref("testtestest");

function getCookie(name: string) {
  const value = `; ${document.cookie}`;
  const parts: string[] = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}

async function signIn(){
    const username = (<HTMLInputElement>document.getElementById("username")).value;
    const password = (<HTMLInputElement>document.getElementById("password")).value;
    const loginDto = {
        "username": username,
        "password": password,
    }
    console.log(loginDto);
    try {
        const { data, status }: AxiosResponse<String> = await httpClient.post("/api/login",
            loginDto,
            {
                headers: {
                    "Accept": 'application/json',
                },
                withCredentials: true,
            },
        )
        tResp.value = data;
        console.log(data);
        console.log(status);
    } catch (error) {
        console.log(error);
        tResp.value = error.data;
    }

}

</script>

<template>
    <div class="container">
        <p>{{ tResp }}</p>
        <div v-if="props.error">
            ERROR
            <!-- <li th:each="err : ${#fields.errors('*')}" th:text="${err}" class="error" /> -->
        </div>
        <div v-if="isLoggedOut == true">
            You have been logged out.
        </div>
        <form @submit.prevent="signIn" class="form-signin" method="post" role="form">
            <h2 class="form-signin-heading">Please sign in</h2>
            <p>
                <label for="username" class="sr-only">Username</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus>
            </p>
            <p>
                <label for="password" class="sr-only">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
            </p>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            
        </form>
        <button @click="isLoggedOut = true" class="btn btn-lg btn-primary btn-block">Logout</button>
        
    </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>