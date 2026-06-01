<script setup lang="ts">
import { ref } from 'vue';
import { useUserStore } from '@/stores/user.stores';
import router from '@/router';
import { ApiError } from '@/models/api/apiError';
import { routeHome } from '@/router/routes';


const DEFAULT_API_RESPONSE = "";

const tResp = ref(DEFAULT_API_RESPONSE);
const userStore = useUserStore();

async function signIn(){
    const username = (<HTMLInputElement>document.getElementById("username")).value;
    const password = (<HTMLInputElement>document.getElementById("password")).value;
    const loginError: ApiError|null = await userStore.login(username, password);
    if (loginError != null) {
        tResp.value = loginError.detail;
    }
    else router.push({"name": routeHome.name});
}

</script>

<template>
    <div class="container">
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
            <div v-if="tResp !== DEFAULT_API_RESPONSE" 
                class="alert alert-danger mt-2 mb-2 p-2">
                <p>{{ tResp }}</p>
            </div>
        </form>
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
