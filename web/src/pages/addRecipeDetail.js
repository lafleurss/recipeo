import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import SideNav from '../components/sidenav';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


/**
 * Logic needed for the save recipe page of the website.
 */
class AddRecipeDetail extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'saveRecipe', 'loadCategoryDropDown'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.saveRecipe);
        this.header = new Header();
        this.sidenav = new SideNav(this.dataStore);
    }

 /**
     * Once the client is loaded, display the recipe details.
     */
    async clientLoaded() {
        this.loadCategoryDropDown();
    }

    async loadCategoryDropDown(){
        const categoriesList = await this.client.getCategoriesForUser();
        var categoryDropDown = document.getElementById('category');
        if (categoriesList) {
           for (let key of categoriesList) {
              let option = document.createElement("option");
              option.setAttribute('value', key.categoryName);
              option.setAttribute('innerHTML', key.categoryName);
              let optionText = document.createTextNode(key.categoryName);
              option.appendChild(optionText);
              categoryDropDown.appendChild(option);
            }
        }

    }

/**
     * When the recipe is  saved in the datastore, update recipe details DOM on the page.
     */
    async saveRecipe() {
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
        this.sidenav.addSideNavToPage();

        this.client = new RecipeoClient();
        await this.clientLoaded();
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const addRecipeDetail = new AddRecipeDetail();
    addRecipeDetail.mount();
};

window.addEventListener('DOMContentLoaded', main);
