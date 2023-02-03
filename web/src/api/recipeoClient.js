import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the Recipeo Service.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
  */
export default class RecipeoClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getRecipe', 'getRecipesForUser'
                               ,'getCategoriesForUser'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();;
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
     * Gets the recipe for the given ID.
     * @param id Unique identifier for a recipe
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The recipe's metadata.
     */
    async getRecipe(id, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can get recipes.");
            const response = await this.axiosClient.get(`recipes/${id}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.recipe;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Gets the recipe for the logged in User ID.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The recipe's metadata.
     */
    async getRecipesForUser(filterType, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can get recipes.");

            const identity = await this.getIdentity();
            const response = await this.axiosClient.get(`recipes/user?filterType=${filterType}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            return response.data.recipes;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

   /**
     * Gets the categories for the logged in User ID.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The categories' metadata.
     */
    async getCategoriesForUser(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can get categories.");

            const identity = await this.getIdentity();
            const response = await this.axiosClient.get(`category/user`,
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            return response.data.categories;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
         * Add a category for the logged in User ID.
         * @param errorCallback (Optional) A function to execute if the call fails.
         * @returns The categories' metadata.
         */
        async addCategory(payload, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Only authenticated users can add categories.");

                const identity = await this.getIdentity();
                const response = await this.axiosClient.post(`category`, payload,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                return response.data.category;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}
