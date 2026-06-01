<script setup lang="ts">
import { ref } from 'vue';
import { ApiError } from '@/models/api/apiError';
import { useUserStore } from '@/stores/user.stores';
import router from '@/router';
import { routeHome } from '@/router/routes';


const DEFAULT_API_RESPONSE = "";

const tResp = ref(DEFAULT_API_RESPONSE);
const userStore = useUserStore();

async function register(){
    const username = (<HTMLInputElement>document.getElementById("username")).value;
    const password = (<HTMLInputElement>document.getElementById("password")).value;
    const loginError: ApiError|null|undefined = await userStore.register(username, password);
    if (loginError != null) {
        tResp.value = loginError.detail;
    }
    else router.push({"name": routeHome.name});
}

</script>

<template>
    <div class="container">
        <div>
            <h1 class="text-center">Register</h1>
            <form @submit.prevent="register" autocomplete="off">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required
                        placeholder="Enter username">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" name="password"  required
                        placeholder="Enter password">
                </div>
                <input type="submit" class="btn btn-primary btn-block btn-lg" value="Register"/>
                <div v-if="tResp !== DEFAULT_API_RESPONSE" 
                    class="alert alert-danger mt-2 mb-2 p-2">
                    <p>{{ tResp }}</p>
                </div>
            </form>
        </div>
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
