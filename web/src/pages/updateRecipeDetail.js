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
        this.bindClassMethods(['mount', 'saveRecipe', 'loadCategoryDropDown', 'validateFields',
        'deleteRecipe', 'cancel', 'displayRecipeOnPage'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.displayRecipeOnPage);

        document.getElementById('favorite').addEventListener('click', this.toggleHeart);

        document.getElementById('save_recipe').addEventListener('click', this.saveRecipe);
        document.getElementById('delete_recipe').addEventListener('click', this.deleteRecipe);
        document.getElementById('cancel').addEventListener('click', this.cancel);

        document.getElementById('preptime').addEventListener('input', this.validateFields);
        document.getElementById('cooktime').addEventListener('input', this.validateFields);
        document.getElementById('servings').addEventListener('input', this.validateFields);

        this.header = new Header();
        this.sidenav = new SideNav(this.dataStore);
    }

 /**
     * Once the client is loaded, display the recipe details.
     */
    async clientLoaded() {
        await this.loadCategoryDropDown();

        const urlParams = new URLSearchParams(window.location.search);
        const recipeId = urlParams.get('id');

        //Get the recipe metadata for the recipeId selected
        const recipe = await this.client.getRecipe(recipeId);
        this.dataStore.set('recipe', recipe);

    }

    toggleHeart() {
        if(document.getElementById('favorite').className == "fa fa-heart-o"){
            document.getElementById('favorite').className = "fa fa-heart";
        } else {
            document.getElementById('favorite').className = "fa fa-heart-o";
        }
    }

    async loadCategoryDropDown(){
        var categoriesList = await this.client.getCategoriesForUser();
        var categoryDropDown = document.getElementById('category');

        if (!categoriesList ){
            categoriesList = [
                    { userId: "userId",
                      categoryName: "Uncategorized",
                      categoryDescription: "Uncategorized"
                    } ];
        } else if ( !categoriesList.includes("Uncategorized"))  {
             categoriesList.push(
                     { userId: "userId",
                       categoryName: "Uncategorized",
                       categoryDescription: "Uncategorized"
                     });
        }

        if (categoriesList) {
           for (let key of categoriesList) {
                let option = document.createElement("option");
                let optionText = document.createTextNode(key.categoryName);
                option.appendChild(optionText);
                categoryDropDown.appendChild(option);
            }
        }

    }

    validateFields(){
        const prepTime =  document.getElementById('preptime');
        const cookTime =  document.getElementById('cooktime');
        const servings =  document.getElementById('servings');

        if (servings.value < 0) {
            servings.value = servings.value * -1;
        }

        if (prepTime.value < 0) {
            prepTime.value = prepTime.value * -1;
        }
        if (cookTime.value < 0) {
            cookTime.value = cookTime.value * -1;
        }

        if (prepTime.value.length > 3) {
            prepTime.value = prepTime.value.slice(0,3);
        }
        if (cookTime.value.length > 3) {
            cookTime.value = cookTime.value.slice(0,3);
        }

        if (servings.value.length > 2) {
            servings.value = servings.value.slice(0,2);
        }

        document.getElementById('totaltime').value = Number(prepTime.value) + Number(cookTime.value);
    }

    /**
         * When the recipe is  updated in the datastore, update recipe details DOM on the page.
         */
    displayRecipeOnPage() {

        document.getElementById('spinner-recipe').style.display = "inline-block";

        const recipe = this.dataStore.get('recipe');
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
            for ( var i = 0; i < select.options.length; i++ ) {
                if ( select.options[i].innerText == recipe.categoryName ) {
                    select.options[i].selected = true;
                    break;
                }
            }
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
    document.getElementById('spinner-recipe').style.display = "none";

    }

    cancel(){
        const urlParams = new URLSearchParams(window.location.search);
        const recipeId = urlParams.get('id');
        window.location.href = `/viewRecipeDetail.html?id=${recipeId}`;
    }


/**
     * Read recipe meta data on page and call sa to database.
     */
    async saveRecipe() {
       const nameRegex = new RegExp('[\"\\\\\\`]');
       const recipeName = document.getElementById('recipename').value;
       if (nameRegex.test(recipeName)) {
            alert("The recipe name you entered has invalid characters");
            return;
        }

        const servings =  document.getElementById('servings').value;
        const prepTime =  document.getElementById('preptime').value;
        const cookTime =  document.getElementById('cooktime').value;
        const totalTime =  Number(prepTime) + Number(cookTime);
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
        document.getElementById('instructions').value = instructions;
        const instructionsTrimmed = document.getElementById('instructions').value.replace(/\n{2,}/g, '\n');
        const instructionsArray = instructionsTrimmed.split("\n");

        for (let i in instructionsArray){
            if ((!instructionsArray[i]) || instructionsArray[i] && instructionsArray[i].length === 0) {
                    alert("Please enter valid instructions");
                    return;
            }
        }

        const ingredients = document.getElementById('ingredients').innerText;
        document.getElementById('ingredients').value = ingredients;
        const ingredientsTrimmed = document.getElementById('ingredients').value.replace(/\n{2,}/g, '\n');
        const ingredientsArray = ingredientsTrimmed.split("\n");

        for (let i in ingredientsArray){
            if ((!ingredientsArray[i]) || ingredientsArray[i] && ingredientsArray[i].length === 0) {
                    alert("Please enter valid ingredients");
                    return;
            }
        }

        const tagsText = document.getElementById('tags').value;

        let tags;
        if (tagsText.length < 1) {
            tags = null;
        } else {
            tags = tagsText.split(/\s*,\s*/);
        }


        if (!recipeName || !servings || !prepTime || !cookTime || !totalTime || !ingredients || !instructions) {
            alert("Please fill in all required fields");
            return;
        }

        let payload = {recipeName: recipeName, servings: servings, prepTime: prepTime, cookTime: cookTime,
        totalTime : totalTime, ingredients : ingredientsArray, instructions : instructionsArray,
        tags : tags, isFavorite : isFavorite, categoryName : categoryName};

        document.getElementById('spinner-recipe').style.display = "inline-block";

        document.getElementById('save_recipe').disabled = true;
        document.getElementById('save_recipe').value = 'Saving Recipe...';
        document.getElementById('save_recipe').style.background='grey';

        let recipeData = this.dataStore.get('recipe');
        const recipe = await this.client.updateRecipe(recipeData.recipeId, payload);
        document.getElementById('spinner-recipe').style.display = "none";

        if (recipe) {
            window.location.href = `/viewRecipes.html?filterType=ALL`;
        }

 }

     async deleteRecipe() {

        if (confirm("Are you sure you want to delete recipe?") == true) {
            const urlParams = new URLSearchParams(window.location.search);
            const recipeId = urlParams.get('id');

            document.getElementById('delete_recipe').disabled = true;
            document.getElementById('delete_recipe').value = 'Deleting Recipe...';
            document.getElementById('delete_recipe').style.background='grey';

            const recipe = await this.client.deleteRecipe(recipeId);
            if (recipe) {
                window.location.href = `/viewRecipes.html?filterType=ALL`;
            }
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
