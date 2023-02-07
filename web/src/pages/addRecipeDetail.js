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

        document.getElementById('favorite').addEventListener('click', this.toggleHeart);
        document.getElementById('save_recipe').addEventListener('click', this.saveRecipe);
        this.header = new Header();
        this.sidenav = new SideNav(this.dataStore);
    }

 /**
     * Once the client is loaded, display the recipe details.
     */
    async clientLoaded() {
        this.loadCategoryDropDown();
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
              option.setAttribute('value', key.categoryName);
              option.setAttribute('innerHTML', key.categoryName);
              let optionText = document.createTextNode(key.categoryName);
              option.appendChild(optionText);
              categoryDropDown.appendChild(option);
            }
        }
    }

/**
     * Read recipe meta data on page and call sa to database.
     */
    async saveRecipe() {
        const nameRegex = new RegExp('[^a-zA-Z\\s-\'.]');
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


        if (!recipeName || !servings || !preptime || !cooktime || !totaltime || !ingredients || !instructions) {
            alert("Please fill in all required fields");
            return;
        }

        if (nameRegex.test(recipeName)) {
            alert("The first name you entered has invalid characters");
            return;
        }

        let payload = {recipeName: recipeName, servings: servings, prepTime: prepTime, cookTime: cookTime,
        totalTime : totalTime, ingredients : ingredientsArray, instructions : instructionsArray,
        tags : null, isFavorite : isFavorite, categoryName : categoryName};

        document.getElementById('save_recipe').disabled = true;
        document.getElementById('save_recipe').innerHTML = 'Saving Recipe...';
        document.getElementById('save_recipe').style.background='grey';
        const recipe = await this.client.addRecipe(payload);

        if (recipe) {
            window.location.href = `/viewRecipes.html`;
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
    const addRecipeDetail = new AddRecipeDetail();
    addRecipeDetail.mount();
};

window.addEventListener('DOMContentLoaded', main);
