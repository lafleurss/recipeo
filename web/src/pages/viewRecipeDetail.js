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

    async addCategory(){
        document.getElementById('add_category').innerHTML = recipe.recipeName;
    }

/**
     * When the recipe is  updated in the datastore, update recipe details DOM on the page.
     */
    async displayRecipeOnPage() {
        const recipe = this.dataStore.get('recipe');

        if (!recipe) {
            return;
        }

        if (recipe.recipeName){
            document.getElementById('recipetitle').innerHTML = recipe.recipeName;
        }
        if (recipe.servings){
            document.getElementById('servings').innerHTML = recipe.servings;
        }
        if (recipe.prepTime){
            document.getElementById('preptime').innerHTML = recipe.prepTime;
        }
        if (recipe.cookTime){
            document.getElementById('cooktime').innerHTML = recipe.cookTime;
        }
        if (recipe.totalTime){
            document.getElementById('totaltime').innerHTML = recipe.totalTime;
        }

        if (recipe.isFavorite){
            if (recipe.isFavorite == "true"){
                document.getElementById('favorite').className = "fa fa-heart";
            }
        }

        if (recipe.categoryName){
            var select = document.getElementById('category');
            var opt = document.createElement('option');
            opt.value = recipe.categoryName;
            opt.innerHTML = recipe.categoryName;
            select.appendChild(opt);
        }

        if (recipe.ingredients){
            var list = document.getElementById('ingredients')
            var counter = 0;
            for (var i in recipe.ingredients) {
              var elem = document.createElement("li");
              elem.innerText =  recipe.ingredients[i];
              list.appendChild(elem);
            }
        }

        if (recipe.instructions){
            var list = document.getElementById('instructions')
            var counter = 0;
            for (var i in recipe.instructions) {
              var elem = document.createElement("li");
              elem.innerText =  recipe.instructions[i];
              list.appendChild(elem);
            }
        }

 }



    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    async mount() {
        this.header.addHeaderToPage();
        this.header.loadCategories();
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
