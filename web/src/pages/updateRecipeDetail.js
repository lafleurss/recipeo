import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import SideNav from '../components/sidenav';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


/**
 * Logic needed for the save recipe page of the website.
 */
class UpdateRecipeDetail extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'saveRecipe', 'loadCategoryDropDown', 'checkNumberFieldLength'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
//        this.dataStore.addChangeListener(this.displayRecipeOnPage);

        document.getElementById('favorite').addEventListener('click', this.toggleHeart);
        document.getElementById('save_recipe').addEventListener('click', this.saveRecipe);
        document.getElementById('preptime').addEventListener('input', this.checkNumberFieldLength);
        document.getElementById('cooktime').addEventListener('input', this.checkNumberFieldLength);
        document.getElementById('totaltime').addEventListener('input', this.checkNumberFieldLength);
        document.getElementById('servings').addEventListener('input', this.checkNumberFieldLength);

        this.header = new Header();
        this.sidenav = new SideNav(this.dataStore);
    }

 /**
     * Once the client is loaded, display the recipe details.
     */
    async clientLoaded() {
        this.loadCategoryDropDown();

        const urlParams = new URLSearchParams(window.location.search);
        const recipeId = urlParams.get('id');

        //Get the recipe metadata for the recipeId selected
        const recipe = await this.client.getRecipe(recipeId);
        this.dataStore.set('recipe', recipe);
        this.displayRecipeOnPage();
    }

    toggleHeart() {
        if(document.getElementById('favorite').className == "fa fa-heart-o"){
            document.getElementById('favorite').className = "fa fa-heart";
        } else {
            document.getElementById('favorite').className = "fa fa-heart-o";
        }
    }

    async loadCategoryDropDown(){
        const categoriesList = await this.client.getCategoriesForUser();
        var categoryDropDown = document.getElementById('category');
        if (categoriesList) {
           for (let key of categoriesList) {
            let option = document.createElement("option");
            let optionText = document.createTextNode(key.categoryName);
            option.appendChild(optionText);
            categoryDropDown.appendChild(option);
            }
        }
    }

    checkNumberFieldLength(){
        const prepTime =  document.getElementById('preptime');
        const cookTime =  document.getElementById('cooktime');
        const totalTime =  document.getElementById('totaltime');
        const servings =  document.getElementById('servings');

        if (prepTime.value.length > 3) {
            prepTime.value = prepTime.value.slice(0,3);
        }
        if (cookTime.value.length > 3) {
            cookTime.value = cookTime.value.slice(0,3);
        }
        if (totalTime.value.length > 3) {
            totalTime.value = totalTime.value.slice(0,3);
        }
        if (servings.value.length > 2) {
            servings.value = servings.value.slice(0,2);
        }
    }

    /**
         * When the recipe is  updated in the datastore, update recipe details DOM on the page.
         */
    async displayRecipeOnPage() {
        let recipe = this.dataStore.get('recipe');
        if (!recipe) {
            return;
        }
        if (recipe.recipeName){
            document.getElementById('recipename').value = recipe.recipeName;
        }
        if (recipe.servings){
            document.getElementById('servings').value = recipe.servings;
        }
        if (recipe.prepTime){
            document.getElementById('preptime').value = recipe.prepTime;
        }
        if (recipe.cookTime){
            document.getElementById('cooktime').value = recipe.cookTime;
        }
        if (recipe.totalTime){
            document.getElementById('totaltime').value = recipe.totalTime;
        }

        if (recipe.isFavorite){
            if (recipe.isFavorite == "true"){
                document.getElementById('favorite').className = "fa fa-heart";
            }
        }

        if (recipe.tags){
            document.getElementById('tags').value = recipe.tags;
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
            for (var i in recipe.ingredients) {
              var elem = document.createElement("li");
              elem.innerText =  recipe.ingredients[i];
              list.appendChild(elem);
            }
        }

        if (recipe.instructions){
            var list = document.getElementById('instructions')
            for (var i in recipe.instructions) {
              var elem = document.createElement("li");
              elem.innerText =  recipe.instructions[i];
              list.appendChild(elem);
            }
        }
    }

/**
     * Read recipe meta data on page and call sa to database.
     */
    async saveRecipe() {
        const nameRegex = new RegExp('/^[ A-Z0-9a-z()[]+-*/%]*$/');
        const recipeName = document.getElementById('recipename').value;
        const servings =  document.getElementById('servings').value;
        const prepTime =  document.getElementById('preptime').value;
        const cookTime =  document.getElementById('cooktime').value;
        const totalTime =  document.getElementById('totaltime').value;
        const categoryElement = document.getElementById('category');
        const categoryName = categoryElement.options[categoryElement.selectedIndex].innerHTML;
        const favoriteClassName = document.getElementById('favorite').className;
        var isFavorite;
        if (favoriteClassName == "fa fa-heart-o") {
            isFavorite = "false";
        } else if (favoriteClassName == "fa fa-heart"){
            isFavorite = "true";
        }

        const instructions = document.getElementById('instructions').innerText;
        const instructionsArray = instructions.split("\n");

        const ingredients = document.getElementById('ingredients').innerText;
        const ingredientsArray = ingredients.split("\n");

        const tagsText = document.getElementById('tags').value;

        let tags;
        if (tagsText.length < 1) {
            tags = null;
        } else {
            tags = tagsText.split(/\s*,\s*/);
        }


        if (!recipeName || !servings || !preptime || !cooktime || !totaltime || !ingredients || !instructions) {
            alert("Please fill in all required fields");
            return;
        }

        if (nameRegex.test(recipeName)) {
            alert("The recipe name you entered has invalid characters");
            return;
        }

        let payload = {recipeName: recipeName, servings: servings, prepTime: prepTime, cookTime: cookTime,
        totalTime : totalTime, ingredients : ingredientsArray, instructions : instructionsArray,
        tags : tags, isFavorite : isFavorite, categoryName : categoryName};

        document.getElementById('save_recipe').disabled = true;
        document.getElementById('save_recipe').innerHTML = 'Saving Recipe...';
        document.getElementById('save_recipe').style.background='grey';

        let recipeData = this.dataStore.get('recipe');
        const recipe = await this.client.updateRecipe(recipeData.recipeId, payload);

        if (recipe) {
            window.location.href = `/viewRecipes.html?filterType=ALL`;
        }

 }

    /**
     * Add the header to the page and load the RecipeoClient.
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
    const updateRecipeDetail = new UpdateRecipeDetail();
    updateRecipeDetail.mount();
};

window.addEventListener('DOMContentLoaded', main);
