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
        this.bindClassMethods(['mount', 'saveRecipe', 'loadCategoryDropDown', 'validateFields'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();

        document.getElementById('favorite').addEventListener('click', this.toggleHeart);
        document.getElementById('save_recipe').addEventListener('click', this.saveRecipe);
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
        document.getElementById('spinner-recipe').style.display = "none";

        let categoriesList = await this.client.getCategoriesForUser();
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
           let i=0;
           for (let key of categoriesList) {
                let option = document.createElement("option");
                let optionText = document.createTextNode(key.categoryName);
                option.appendChild(optionText);
                categoryDropDown.appendChild(option);
                if ( key.categoryName == "Uncategorized" ) {
                    categoryDropDown.options[i].selected = true;
                }
                i++;
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
     * Read recipe meta data on page and call sa to database.
     */
    async saveRecipe() {


        const nameRegex = new RegExp('/^[ A-Z0-9a-z()[]+-*/%]*$/');

        const recipeName = document.getElementById('recipename').value;
        const servings =  document.getElementById('servings').value;
        const prepTime =  document.getElementById('preptime').value;
        const cookTime =  document.getElementById('cooktime').value;
        const totalTime =  Number(prepTime) + Number(cookTime);
        document.getElementById('cooktime').value = totalTime;
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

        document.getElementById('spinner-recipe').style.display = "inline-block";
        document.getElementById('save_recipe').disabled = true;
        document.getElementById('save_recipe').innerHTML = 'Saving Recipe...';
        document.getElementById('save_recipe').style.background='grey';
        const recipe = await this.client.addRecipe(payload);

        document.getElementById('spinner-recipe').style.display = "none";

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
    const addRecipeDetail = new AddRecipeDetail();
    addRecipeDetail.mount();
};

window.addEventListener('DOMContentLoaded', main);
