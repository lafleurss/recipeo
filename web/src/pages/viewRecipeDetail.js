import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


/**
 * Logic needed for the view playlist page of the website.
 */
class ViewRecipeDetail extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }

 /**
     * Once the client is loaded, get the recipe details.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const recipeId = urlParams.get('id');

        //Get the recipe metadata for the recipeId selected
        const recipe = await this.client.getRecipe(recipeId, true);
        this.dataStore.set('recipe', recipe);
        await this.displayRecipeOnPage();
    }

/**
     * When the recipe is  updated in the datastore, update recipe details DOM on the page.
     */
    async displayRecipeOnPage() {
        const recipe = this.dataStore.get('recipe');
 }



    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    async mount() {
        this.header.addHeaderToPage();
        this.client = new RecipeoClient();
        await this.clientLoaded();
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewRecipeDetail = new ViewRecipeDetail();
    viewRecipeDetail.mount();
};

window.addEventListener('DOMContentLoaded', main);
