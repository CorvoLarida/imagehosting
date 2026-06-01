<script setup lang="ts">
import { RouterLink, RouterView, useRoute } from 'vue-router'
import { useUserStore } from './stores/user.stores';
import { storeToRefs } from 'pinia';
import { watch } from 'vue';
import router from './router';
import { PATHS_NOT_ALLOWED_WHEN_REGISTERED, routeHome } from './router/routes';

const userStore = useUserStore();
userStore.getSession();

const {isSessionChecked, user} = storeToRefs(userStore);

const route = useRoute();

watch(
    [
        () => isSessionChecked.value,
        () => route.path,
    ],
    ([isSessionChecked, path]) => {
        if (isSessionChecked === true) {
            // console.log("check route", path, user.value);
            if (user.value !== null) {
                if (PATHS_NOT_ALLOWED_WHEN_REGISTERED.includes(path)) router.push({"name": routeHome.name});
            }
        }
    },
)

</script>

<template>
<!--   
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/login">Test Login</RouterLink>
      </nav>
    </div>
  </header> 
  -->
  <div v-if="userStore.isSessionChecked">
    <RouterView />
  </div>
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>
